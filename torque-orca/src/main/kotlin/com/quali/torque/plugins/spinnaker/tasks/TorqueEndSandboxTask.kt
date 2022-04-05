package com.quali.torque.plugins.spinnaker.tasks

import com.netflix.spinnaker.orca.api.pipeline.TaskResult
import com.netflix.spinnaker.orca.api.pipeline.models.ExecutionStatus
import com.netflix.spinnaker.orca.api.pipeline.models.StageExecution
import com.quali.torque.plugins.spinnaker.TorqueAuth
import com.quali.torque.plugins.spinnaker.TorqueBaseTask
import com.quali.torque.plugins.spinnaker.TorqueConfig
import org.pf4j.Extension
import org.slf4j.LoggerFactory

@Extension
class TorqueEndSandboxTask(private val config: TorqueConfig) : TorqueBaseTask {

    data class TorqueEndSandboxTaskContext(
            val sandboxId: String,
            val space: String,
            val token: String = ""
    )

    private val log = LoggerFactory.getLogger(TorqueEndSandboxTask::class.java)

    override fun execute(stage: StageExecution): TaskResult {
        val ctx = stage.mapTo(TorqueEndSandboxTaskContext::class.java)
        log.info("Task TorqueEndSandboxTask started")

        // first read from params than from config
        val token: String = ctx.token.ifEmpty {config.torqueToken }

        // if it's still empty fail the build
        if (token.isEmpty()){
            val errorMsg = "The token was provided neither in the stage parameters nor in the config"
            addErrorMessage(stage, errorMsg)
            throw IllegalArgumentException(errorMsg)
        }

        val url = this.config.torqueUrl
        val api = TorqueAuth(token, url).getAPI()

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