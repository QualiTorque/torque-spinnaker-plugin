package com.quali.colony.plugins.spinnaker.tasks

import com.google.gson.Gson
import com.netflix.spinnaker.orca.api.pipeline.RetryableTask
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
import java.util.concurrent.TimeUnit

@Extension
class ColonyVerifySandboxIsReadyTask(private val config: ColonyConfig) : ColonyBaseTask, RetryableTask {
    private val log = LoggerFactory.getLogger(ColonyVerifySandboxIsReadyTask::class.java)
    private var taskTimeout = TimeUnit.HOURS.toMillis(2)
    private val endSandboxTask = ColonyEndSandboxTask(config)

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

        if (sandbox.sandboxStatus.equals(SandboxStatus.ENDED) || 
            sandbox.sandboxStatus.equals(SandboxStatus.ENDING) ||
            sandbox.sandboxStatus.equals(SandboxStatus.ENDED_WITH_ERROR))
            throw Throwable("Sandbox with id ${sandbox.id} has been ended")

        throw Throwable("Sandbox with id ${sandbox.id} has unknown sandbox status ${sandbox.sandboxStatus}")
    }

    private fun formatAppsDeploymentStatuses(sandbox: SingleSandbox): String {
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
        log.info("VerifySandboxIsActive task is running")

        val sandboxId = stage.context["sandboxId"] as String
        val space = stage.context["space"] as String
        val timeoutMinutes = stage.context["timeoutMinutes"] as Int
        val token = stage.context["token"] as String
        val url = config.colonyUrl

        // Rewrite task timeout with user input
        taskTimeout = TimeUnit.MINUTES.toMillis(timeoutMinutes.toLong())

        val api = ColonyAuth(token, url).getAPI()

        log.info("Getting sandbox")
        val sandbox = api.getSandboxById(space, sandboxId)
        val sandboxData = Gson().fromJson(sandbox.rawBodyJson, SingleSandbox::class.java)

        val isActive: Boolean

        try {
            isActive = isSandboxActive(sandboxData)
        }
        catch (e: Throwable) {
            addExceptionToOutput(stage, e)
            return TaskResult.builder(ExecutionStatus.TERMINAL)
                .context(stage.context)
                .outputs(stage.outputs)
                .build()
        }

        return if (isActive) {
            log.info("Sandbox is active. Finishing task")
            stage.outputs["SandboxDetails"] = sandboxData
            stage.outputs["QuickLinks"] = sandboxData.applications
            TaskResult.builder(ExecutionStatus.SUCCEEDED)
                    .context(stage.context)
                    .outputs(stage.outputs)
                    .build()
        }
        else {
            TaskResult.builder(ExecutionStatus.RUNNING).build()
        }
    }

    override fun onCancel(stage: StageExecution) {
        addObjectToStageContext(stage, "sandboxId", stage.context["sandboxId"])
        this.endSandboxTask.execute(stage)
    }

    override fun onTimeout(stage: StageExecution): TaskResult {
        addErrorMessage(stage, "Sandbox is not active after ${stage.context["timeoutMinutes"]} minutes.")
        //TODO: maybe we need to stop sandbox as well
        return TaskResult.builder(ExecutionStatus.TERMINAL)
                .context(stage.context)
                .outputs(stage.outputs)
                .build()
    }

    override fun getBackoffPeriod(): Long {
        return TimeUnit.SECONDS.toMillis(15)
    }

    override fun getTimeout(): Long {
        return taskTimeout
    }
}