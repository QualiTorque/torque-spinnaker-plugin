package com.quali.torque.plugins.spinnaker.tasks

import com.netflix.spinnaker.orca.api.pipeline.TaskResult
import com.netflix.spinnaker.orca.api.pipeline.models.ExecutionStatus
import com.netflix.spinnaker.orca.api.pipeline.models.StageExecution
import com.quali.torque.plugins.spinnaker.TorqueAuth
import com.quali.torque.plugins.spinnaker.TorqueBaseTask
import com.quali.torque.plugins.spinnaker.TorqueConfig
import com.quali.torque.plugins.spinnaker.api.CreateSandboxRequest
import org.pf4j.Extension
import org.slf4j.LoggerFactory
import java.util.HashMap
import java.net.URL


@Extension
class TorqueStartSandboxTask(private val config: TorqueConfig) : TorqueBaseTask {
    data class TorqueStartSandboxStageContext(
            val space: String,
            val blueprintName: String,
            val sandboxName: String,
            val duration: Int = 1,
            val timeoutMinutes: Int = 20,
            val artifacts: String = "",
            val inputs: String = "",
            val token: String = ""
    )

    private val log = LoggerFactory.getLogger(TorqueStartSandboxTask::class.java)

    private fun parseParamsString(paramsStr: String) : Map<String,String> {
        val holder = HashMap<String,String>()

        if ((paramsStr.trim().isNotEmpty()) || (paramsStr.contains("="))) {
            val keyValues = paramsStr.split(",")
            for (keyV in keyValues) {
                val parts = keyV.trim().split("=", limit = 2)
                holder[parts[0]] = parts[1]
            }
        }
        return holder
    }

    override fun execute(stage: StageExecution): TaskResult {
        val ctx = stage.mapTo(TorqueStartSandboxStageContext::class.java)

        log.info("parsing artifacts string ${ctx.artifacts}")
        val artifacts = parseParamsString(ctx.artifacts)

        log.info("parsing inputs string ${ctx.inputs}")
        val inputs = parseParamsString(ctx.inputs)

        val duration = "PT${ctx.duration}M"
        val startReq = CreateSandboxRequest(
                ctx.blueprintName,
                ctx.sandboxName,
                artifacts,
                true,
                inputs,
                duration)

        // first read from params than from config
        val token: String = ctx.token.ifEmpty {config.torqueToken }

        // if it's still empty fail the build
        if (token.isEmpty()) {
            val errorMsg = "The token was provided neither in the stage parameters nor in the config"
            addErrorMessage(stage, errorMsg)
            throw IllegalArgumentException(errorMsg)
        }

        this.addObjectToStageContext(stage, "token", token)

        val url = this.config.torqueUrl

        val api = TorqueAuth(token, url).getAPI()
        try {
            val res = api.createSandbox(ctx.space, startReq)
            return if (res.isSuccessful) {

                val sandboxId = res.data?.id
                // TODO: Remove hardcode
                if (config.account != "") {
                    val urlObj = URL(url)
                    addToOutput(
                        stage,
                        "sandboxUrl",
                        "${urlObj.protocol}://${config.account}.${urlObj.authority}/${ctx.space}/sandboxes/$sandboxId")
                }
                log.info("Sandbox $sandboxId has been launched")
                TaskResult.builder(ExecutionStatus.SUCCEEDED)
                        .context(stage.context)
                        .outputs(mutableMapOf("sandboxId" to sandboxId))
                        .build()
            } else {
                addErrorMessage(stage, res.error)
                TaskResult.builder(ExecutionStatus.TERMINAL)
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