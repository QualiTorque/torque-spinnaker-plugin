package com.quali.colony.plugins.spinnaker

import com.netflix.spinnaker.orca.api.pipeline.Task
import com.netflix.spinnaker.orca.api.pipeline.TaskResult
import com.netflix.spinnaker.orca.api.pipeline.graph.StageDefinitionBuilder
import com.netflix.spinnaker.orca.api.pipeline.graph.TaskNode
import com.netflix.spinnaker.orca.api.pipeline.models.StageExecution
import org.pf4j.Extension
import org.pf4j.Plugin
import org.pf4j.PluginWrapper
import org.slf4j.LoggerFactory

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
class ColonySandboxStage : StageDefinitionBuilder {

    /**
     * This function describes the sequence of substeps, or "tasks" that comprise this
     * stage. The task graph is generally linear though there are some looping mechanisms.
     *
     * This method is called just before a stage is executed. The task graph can be generated
     * programmatically based on the stage's context.
     */
    override fun taskGraph(stage: StageExecution, builder: TaskNode.Builder) {
        builder.withTask("colonyPlugin", ColonySandboxTask::class.java)
    }
}

@Extension
class ColonySandboxTask(private val config: ColonyConfig) : Task {

    private val log = LoggerFactory.getLogger(ColonySandboxTask::class.java)

    /**
     * This method is called when the task is executed.
     */
    override fun execute(stage: StageExecution): TaskResult {
        log.info("Task ColonySandboxTask started")
    }
}
