package com.quali.torque.plugins.spinnaker.stages

import com.netflix.spinnaker.orca.api.pipeline.graph.StageDefinitionBuilder

import com.netflix.spinnaker.orca.api.pipeline.graph.TaskNode
import com.netflix.spinnaker.orca.api.pipeline.models.StageExecution
import com.quali.torque.plugins.spinnaker.tasks.TorqueStartSandboxTask
import com.quali.torque.plugins.spinnaker.tasks.TorqueVerifySandboxIsReadyTask
import org.pf4j.Extension

@Extension
class TorqueStartSandboxStage : StageDefinitionBuilder{

    override fun taskGraph(stage: StageExecution, builder: TaskNode.Builder) {
        builder.withTask("startSandbox", TorqueStartSandboxTask::class.java)
        builder.withTask("verifyAndWaitSandboxActive", TorqueVerifySandboxIsReadyTask::class.java)
    }
}

