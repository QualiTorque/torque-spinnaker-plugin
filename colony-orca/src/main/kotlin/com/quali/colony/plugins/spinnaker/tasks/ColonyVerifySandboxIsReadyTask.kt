package com.quali.colony.plugins.spinnaker.tasks

import com.google.gson.Gson
import com.netflix.spinnaker.orca.api.pipeline.Task
import com.netflix.spinnaker.orca.api.pipeline.TaskResult
import com.netflix.spinnaker.orca.api.pipeline.models.ExecutionStatus
import com.netflix.spinnaker.orca.api.pipeline.models.StageExecution
import com.quali.colony.plugins.spinnaker.ColonyAuth
import com.quali.colony.plugins.spinnaker.ColonyBaseTask
import com.quali.colony.plugins.spinnaker.ColonyConfig
import com.quali.colony.plugins.spinnaker.api.SandboxStatus
import com.quali.colony.plugins.spinnaker.api.SingleSandbox
import org.pf4j.Extension
import org.slf4j.LoggerFactory
import java.lang.RuntimeException

@Extension
class ColonyVerifySandboxIsReadyTask(private val config: ColonyConfig) : ColonyBaseTask {
    private val log = LoggerFactory.getLogger(ColonyVerifySandboxIsReadyTask::class.java)

    data class ColonyVerifySandboxIsReadyTaskContext(
            val sandboxId: String,
            val space: String,
            val timeoutMinutes: Int = 20
    )

    private fun isSandboxActive(sandbox: SingleSandbox): Boolean {
        if (sandbox.sandboxStatus.equals(SandboxStatus.LAUNCHING))
            return false
        if (sandbox.sandboxStatus.equals(SandboxStatus.ACTIVE))
            return true
        if (sandbox.sandboxStatus.equals(SandboxStatus.ACTIVE_WITH_ERROR)) {
            val appStatusesString = formatAppsDeploymentStatuses(sandbox)
            throw Throwable("Sandbox deployment failed with " +
                    "status ${sandbox.sandboxStatus}, apps deployment statuses are: $appStatusesString")
        }

        throw Throwable("Sandbox with id ${sandbox.id} has unknown sandbox status ${sandbox.sandboxStatus}")
    }

    private fun formatAppsDeploymentStatuses(sandbox: SingleSandbox): String? {
        val builder = StringBuilder()
        var isFirst = true
        for (service in sandbox.applications!!) {
            if (isFirst) isFirst = false else builder.append(", ")
            builder.append(java.lang.String.format("%s: %s", service.name, service.status))
        }
        if (!sandbox.sandboxErrors!!.isEmpty()) {
            builder.append(System.getProperty("line.separator"))
            builder.append("Sandbox Errors: ")
            for (service in sandbox.sandboxErrors!!) {
                if (isFirst) isFirst = false else builder.append(", ")
                builder.append(String.format("%s: %s", service.time, service.code, service.message))
            }
        }
        return builder.toString()
    }

    override fun execute(stage: StageExecution): TaskResult {
        log.info("VerifySandboxIsActive task started")
        val sandboxId = stage.context["sandboxId"] as String
        val space = stage.context["space"] as String
        var timeoutMinutes = 20
        if ("timeoutMinutes" in stage.context.keys)
            timeoutMinutes = stage.context["timeoutMinutes"] as Int

        else
            addToObjectToStageContext(stage,"timeoutMinutes", timeoutMinutes)

        var prevStatus = ""
        val api = ColonyAuth(config).getAPI()

        val startTime = System.currentTimeMillis()
        while ((System.currentTimeMillis() - startTime) < timeoutMinutes*1000*60) {
            log.info("Getting sandbox")
            val sandbox = api.getSandboxById(space, sandboxId)
            val sandboxData = Gson().fromJson(sandbox.rawBodyJson, SingleSandbox::class.java)

            if (!sandboxData.sandboxStatus.equals(prevStatus)) {
                prevStatus = sandboxData.sandboxStatus.toString()
            }
            val isActive = false

            try {
                val isActive = isSandboxActive(sandboxData)
            }
            catch (e: Throwable) {
                addExceptionToOutput(stage, e)
                return TaskResult.builder(ExecutionStatus.TERMINAL)
                    .context(stage.context)
                    .outputs(stage.outputs)
                    .build()
            }
            
            if (isActive) {
                log.info("Sandbox is active. Finishing task")
                stage.outputs["SandboxDetails"] = sandboxData
                stage.outputs["QuickLinks"] = sandboxData.applications
                return TaskResult.builder(ExecutionStatus.SUCCEEDED)
                        .context(stage.context)
                        .outputs(stage.outputs)
                        .build()
            }
            log.info("**** Waiting for sandbox $sandboxId become active ****")
            log.info("**** Current status is: $prevStatus")
            Thread.sleep(10000);
        }
        addErrorMessage(stage, "Sandbox is not active after $timeoutMinutes minutes")
        return TaskResult.builder(ExecutionStatus.TERMINAL)
                .context(stage.context)
                .outputs(stage.outputs)
                .build()
    }
}