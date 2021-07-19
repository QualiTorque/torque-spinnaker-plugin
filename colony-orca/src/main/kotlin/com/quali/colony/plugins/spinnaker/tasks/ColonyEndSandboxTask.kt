package com.quali.colony.plugins.spinnaker.tasks

import com.netflix.spinnaker.orca.api.pipeline.TaskResult
import com.netflix.spinnaker.orca.api.pipeline.models.ExecutionStatus
import com.netflix.spinnaker.orca.api.pipeline.models.StageExecution
import com.quali.colony.plugins.spinnaker.ColonyAuth
import com.quali.colony.plugins.spinnaker.ColonyBaseTask
import com.quali.colony.plugins.spinnaker.ColonyConfig
import org.pf4j.Extension
import org.slf4j.LoggerFactory

@Extension
class ColonyEndSandboxTask(private val config: ColonyConfig) : ColonyBaseTask {

    data class ColonyEndSandboxTaskContext(
            val sandboxId: String,
            val space: String,
            val token: String = ""
    )

    private val log = LoggerFactory.getLogger(ColonyEndSandboxTask::class.java)

    override fun execute(stage: StageExecution): TaskResult {
        val ctx = stage.mapTo(ColonyEndSandboxTaskContext::class.java)
        log.info("Task ColonyEndSandboxTask started")

        // first read from params than from config
        val token: String = ctx.token.ifEmpty {config.colonyToken }

        // if it's still empty fail the build
        if (token.isEmpty()){
            val errorMsg = "The token was provided neither in the stage parameters nor in the config"
            addErrorMessage(stage, errorMsg)
            throw IllegalArgumentException(errorMsg)
        }

        val url = this.config.colonyUrl
        val api = ColonyAuth(token, url).getAPI()

        log.info("Stopping sandbox: ${ctx.sandboxId}")
        val res = api.deleteSandbox(ctx.space, ctx.sandboxId)

        return if (res.isSuccessful) {
            log.info("Sandbox ${ctx.sandboxId} has been stopped")
            TaskResult.builder(ExecutionStatus.SUCCEEDED)
                    .context(stage.context)
                    .outputs(stage.outputs)
                    .build()
        } else {
            addErrorMessage(stage, res.error)
            TaskResult.builder(ExecutionStatus.TERMINAL)
                    .context(stage.context)
                    .build()
        }
    }
}