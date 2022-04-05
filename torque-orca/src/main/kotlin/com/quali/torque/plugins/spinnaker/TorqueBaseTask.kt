package com.quali.torque.plugins.spinnaker

import com.netflix.spinnaker.orca.api.pipeline.Task
import com.netflix.spinnaker.orca.api.pipeline.models.StageExecution


interface TorqueBaseTask : Task{
    fun addObjectToStageContext(stage: StageExecution, key: String?, value: Any?) {
        stage.context[key] = value
    }

    fun getStageContext(stage: StageExecution): Map<*, *>? {
        return stage.context
    }

    fun addToOutput(stage: StageExecution, key: String?, value: Any?) {
        stage.outputs[key] = value
    }

    fun addErrorMessage(stage: StageExecution, errorMessage: String?) {
        stage.outputs["failureMessage"] = errorMessage
    }

    fun addExceptionToOutput(stage: StageExecution, e: Throwable) {
        stage.outputs["failureMessage"] = e.message
    }
}