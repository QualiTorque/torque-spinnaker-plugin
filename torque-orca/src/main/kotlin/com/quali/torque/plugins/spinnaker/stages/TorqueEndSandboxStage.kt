package com.quali.torque.plugins.spinnaker.stages

import com.netflix.spinnaker.orca.api.pipeline.graph.StageDefinitionBuilder
import com.netflix.spinnaker.orca.api.pipeline.graph.TaskNode
import com.netflix.spinnaker.orca.api.pipeline.models.StageExecution
import com.quali.torque.plugins.spinnaker.tasks.TorqueEndSandboxTask
import org.pf4j.Extension

@Extension
class TorqueEndSandboxStage : StageDefinitionBuilder {
    override fun taskGraph(stage: StageExecution, builder: TaskNode.Builder) {
        builder.withTask("endSandbox", TorqueEndSandboxTask::class.java)
    }
}