package com.quali.colony.plugins.spinnaker.stages

import com.netflix.spinnaker.orca.api.pipeline.graph.StageDefinitionBuilder

import com.netflix.spinnaker.orca.api.pipeline.graph.TaskNode
import com.netflix.spinnaker.orca.api.pipeline.models.StageExecution
import com.quali.colony.plugins.spinnaker.tasks.ColonyStartSandboxTask
import com.quali.colony.plugins.spinnaker.tasks.ColonyVerifySandboxIsReadyTask
import org.pf4j.Extension

@Extension
class ColonyStartSandboxStage(config: ColonyConfig) : StageDefinitionBuilder{

    override fun taskGraph(stage: StageExecution, builder: TaskNode.Builder) {
        builder.withTask("startSandbox", ColonyStartSandboxTask::class.java)
        builder.withTask("verifyAndWaitSandboxActive", ColonyVerifySandboxIsReadyTask::class.java)
    }
}

