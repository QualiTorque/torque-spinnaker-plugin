package com.quali.colony.plugins.spinnaker.stages

import com.netflix.spinnaker.orca.api.pipeline.graph.StageDefinitionBuilder
import com.netflix.spinnaker.orca.api.pipeline.graph.TaskNode
import com.netflix.spinnaker.orca.api.pipeline.models.StageExecution
import com.quali.colony.plugins.spinnaker.tasks.ColonyEndSandboxTask
import org.pf4j.Extension

@Extension
class ColonyEndSandboxStage : StageDefinitionBuilder {
    override fun taskGraph(stage: StageExecution, builder: TaskNode.Builder) {
        builder.withTask("endSandbox", ColonyEndSandboxTask::class.java)
    }
}