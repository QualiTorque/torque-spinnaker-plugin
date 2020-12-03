package com.quali.colony.plugins.spinnaker

import com.netflix.spinnaker.orca.api.pipeline.graph.StageDefinitionBuilder
import com.netflix.spinnaker.orca.api.pipeline.graph.TaskNode
import com.netflix.spinnaker.orca.api.pipeline.models.StageExecution
import com.quali.colony.plugins.spinnaker.tasks.ColonyStartSandboxTask
import com.quali.colony.plugins.spinnaker.tasks.ColonyVerifySandboxIsReadyTask
import org.pf4j.Extension


@Extension
class ColonyStartSandboxStage : StageDefinitionBuilder {

    override fun taskGraph(stage: StageExecution, builder: TaskNode.Builder) {
        builder.withTask("colonyStartSandbox", ColonyStartSandboxTask::class.java)
        builder.withTask("colonyVerifySandboxActive", ColonyVerifySandboxIsReadyTask::class.java)
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

