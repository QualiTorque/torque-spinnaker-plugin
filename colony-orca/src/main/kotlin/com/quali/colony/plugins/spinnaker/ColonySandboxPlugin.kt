package com.quali.colony.plugins.spinnaker

import com.google.gson.Gson
import com.netflix.spinnaker.orca.api.pipeline.Task
import com.netflix.spinnaker.orca.api.pipeline.TaskResult
import com.netflix.spinnaker.orca.api.pipeline.graph.StageDefinitionBuilder
import com.netflix.spinnaker.orca.api.pipeline.graph.TaskNode
import com.netflix.spinnaker.orca.api.pipeline.models.ExecutionStatus.SUCCEEDED
import com.netflix.spinnaker.orca.api.pipeline.models.ExecutionStatus.TERMINAL
import com.netflix.spinnaker.orca.api.pipeline.models.StageExecution
import com.quali.colony.plugins.spinnaker.api.CreateSandboxRequest
import com.quali.colony.plugins.spinnaker.api.SandboxStatus
import com.quali.colony.plugins.spinnaker.api.SingleSandbox
import org.pf4j.Extension
import org.pf4j.Plugin
import org.pf4j.PluginWrapper
import org.slf4j.LoggerFactory
import java.util.*


class ColonySandboxPlugin(wrapper: PluginWrapper) : Plugin(wrapper) {
    private val logger = LoggerFactory.getLogger(ColonySandboxPlugin::class.java)

    override fun start() {
        logger.info("ColonySandboxPlugin.start()")
    }

    override fun stop() {
        logger.info("ColonySandboxPlugin.stop()")
    }
}

/**
 * By implementing StageDefinitionBuilder, your stage is available for use in Spinnaker.
 * @see com.netflix.spinnaker.orca.api.pipeline.graph.StageDefinitionBuilder
 */
@Extension
class ColonyStartSandboxStage : StageDefinitionBuilder {

    /**
     * This function describes the sequence of substeps, or "tasks" that comprise this
     * stage. The task graph is generally linear though there are some looping mechanisms.
     *
     * This method is called just before a stage is executed. The task graph can be generated
     * programmatically based on the stage's context.
     */

    override fun taskGraph(stage: StageExecution, builder: TaskNode.Builder) {
        builder.withTask("colonyStartSandbox", ColonyStartSandboxTask::class.java)
        builder.withTask("colonyVerifySandboxActive", ColonyVerifySandboxIsReadyTask::class.java)
    }
}

@Extension
class ColonyEndSandboxStage : StageDefinitionBuilder {

    /**
     * This function describes the sequence of substeps, or "tasks" that comprise this
     * stage. The task graph is generally linear though there are some looping mechanisms.
     *
     * This method is called just before a stage is executed. The task graph can be generated
     * programmatically based on the stage's context.
     */

    override fun taskGraph(stage: StageExecution, builder: TaskNode.Builder) {
        builder.withTask("colonyEndSandbox", ColonyEndSandboxTask::class.java)
    }
}

data class ColonyStartSandboxStageContext(
        val space: String,
        val blueprintName: String,
        val sandboxName: String,
        val duration: Int = 1,
        val timeoutMinutes: Int = 20,
        val artifacts: String = "",
        val inputs: String = ""
)

@Extension
class ColonyStartSandboxTask(private val config: ColonyConfig) : Task {
    private val log = LoggerFactory.getLogger(ColonyStartSandboxTask::class.java)

    private fun parseParamsString(paramsStr: String) : Map<String,String> {
        val holder = HashMap<String,String>()

        if  ((paramsStr.trim().length > 0 ) || (paramsStr.contains("="))) {
            val keyValues = paramsStr.split(", ")
            for (keyV in keyValues) {
                val parts = keyV.split("=", limit = 2)
                holder[parts[0]] = parts[1]
            }
        }
        return holder
    }
    /**
     * This method is called when the task is executed.
     */
    override fun execute(stage: StageExecution): TaskResult {
        val ctx = stage.mapTo(ColonyStartSandboxStageContext::class.java)

        log.info("parsing artifacts string ${ctx.artifacts}")
        val artifacts = parseParamsString(ctx.artifacts)

        log.info("parsing inputs string ${ctx.inputs}")
        val inputs = parseParamsString(ctx.inputs)

        val duration = "PT${ctx.duration}H"

        val startReq = CreateSandboxRequest(
                ctx.blueprintName,
                ctx.sandboxName,
                artifacts,
                true,
                inputs,
                duration)

        val api = ColonyAuth(config).getAPI()
        try {
            val res = api.createSandbox(ctx.space, startReq)
            return if (res.isSuccessful) {
                val sandboxId = res.data?.id
                log.info("Sandbox $sandboxId has been launched")
                TaskResult.builder(SUCCEEDED)
                        .context(stage.context)
                        .outputs(mutableMapOf("sandboxId" to sandboxId))
                        .build()
            } else {
                TaskResult.builder(TERMINAL)
                        .context(stage.context)
                        .build()
            }
        }
        catch (e: Exception) {
            log.error("Unable to start sandbox ${ctx.sandboxName}. Reason ${e.message}")
            throw e
        }
    }
}


@Extension
class ColonyVerifySandboxIsReadyTask(private val config: ColonyConfig) : Task {
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
//        val ctx = stage.mapTo(
//                ColonyStartSandboxStageContext::class.java
//        )

        log.info("VerifySandboxIsActive task started")
        val sandboxId = stage.context["sandboxId"] as String
        val space = stage.context["space"] as String
        var timeoutMinutes = 20
        if ("timeoutMinutes" in stage.context.keys) {
            timeoutMinutes = stage.context["timeoutMinutes"] as Int
        }


        var prevStatus = ""
        val api = ColonyAuth(config).getAPI()

        val startTime = System.currentTimeMillis()
        while ((System.currentTimeMillis() - startTime) < timeoutMinutes*1000*60) {
            log.info("Getting sandbox")
            val sandbox = api.getSandboxById(space, sandboxId)
            if (sandbox != null) {
                val sandboxData = Gson().fromJson(sandbox.rawBodyJson, SingleSandbox::class.java)

                if (!sandboxData.sandboxStatus.equals(prevStatus)) {
                    prevStatus = sandboxData.sandboxStatus.toString()
                }
                if (isSandboxActive(sandboxData)) {
                    log.info("Sandbox is active. Finishing task")
                    stage.outputs["SandboxDetails"] = sandboxData
                    stage.outputs["QuickLinks"] = sandboxData.applications
                    return TaskResult.builder(SUCCEEDED)
                            .context(stage.context)
                            .outputs(stage.outputs)
                            .build()
                }
            }
            log.info("**** Waiting for sandbox $sandboxId become active ****")
            log.info("**** Current status is: $prevStatus")
            Thread.sleep(10000);
        }
        return TaskResult.builder(TERMINAL).context(stage.context)
                .build()
    }
}


@Extension
class ColonyEndSandboxTask(private val config: ColonyConfig) : Task {

    data class ColonyEndSandboxTaskContext(
            val sandboxId: String,
            val space: String
    )

    private val log = LoggerFactory.getLogger(ColonyEndSandboxTask::class.java)

    /**
     * This method is called when the task is executed.
     */
    override fun execute(stage: StageExecution): TaskResult {
        val ctx = stage.mapTo(ColonyEndSandboxTaskContext::class.java)
        log.info("Task ColonyEndSandboxTask started")
        val api = ColonyAuth(config).getAPI()

        log.info("Stopping sandbox: ${ctx.sandboxId}")
        val res = api.deleteSandbox(ctx.space, ctx.sandboxId)

        return if (res.isSuccessful) {
            log.info("Sandbox ${ctx.sandboxId} has been stopped")
            TaskResult.builder(SUCCEEDED)
                    .context(stage.context)
                    .outputs(stage.outputs)
                    .build()
        } else {
            TaskResult.builder(TERMINAL)
                    .context(stage.context)
                    .build()
        }
    }
}