package com.quali.colony.plugins.spinnaker.tasks

import com.netflix.spinnaker.orca.api.pipeline.TaskResult
import com.netflix.spinnaker.orca.api.pipeline.models.ExecutionStatus
import com.netflix.spinnaker.orca.api.pipeline.models.StageExecution
import com.quali.colony.plugins.spinnaker.ColonyAuth
import com.quali.colony.plugins.spinnaker.ColonyBaseTask
import com.quali.colony.plugins.spinnaker.ColonyConfig
import com.quali.colony.plugins.spinnaker.api.CreateSandboxRequest
import org.pf4j.Extension
import org.slf4j.LoggerFactory
import java.util.HashMap

@Extension
class ColonyStartSandboxTask(private val config: ColonyConfig) : ColonyBaseTask {
    data class ColonyStartSandboxStageContext(
            val space: String,
            val blueprintName: String,
            val sandboxName: String,
            val duration: Int = 1,
            val timeoutMinutes: Int = 20,
            val artifacts: String = "",
            val inputs: String = ""
    )

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