package com.quali.torque.plugins.spinnaker.tasks

import com.google.gson.Gson
import com.netflix.spinnaker.orca.api.pipeline.RetryableTask
import com.netflix.spinnaker.orca.api.pipeline.TaskResult
import com.netflix.spinnaker.orca.api.pipeline.models.ExecutionStatus
import com.netflix.spinnaker.orca.api.pipeline.models.StageExecution
import com.quali.torque.client.api.EnvironmentApi
import com.quali.torque.client.models.QualiColonyGatewayApiModelResponsesEnvironmentResponse
import com.quali.torque.plugins.spinnaker.TorqueAuth
import com.quali.torque.plugins.spinnaker.TorqueBaseTask
import com.quali.torque.plugins.spinnaker.TorqueConfig
import org.pf4j.Extension
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit


@Extension
class TorqueVerifySandboxIsReadyTask(private val config: TorqueConfig) : TorqueBaseTask, RetryableTask {
    private val log = LoggerFactory.getLogger(TorqueVerifySandboxIsReadyTask::class.java)
    private var taskTimeout = TimeUnit.HOURS.toMillis(2)
    private val endSandboxTask = TorqueEndSandboxTask(config)

    object SandboxStatus {
        const val LAUNCHING = "Launching"
        const val ACTIVE = "Active"
        const val ACTIVE_WITH_ERROR = "ActiveWithError"
        const val ENDED = "Ended"
        const val ENDED_WITH_ERROR = "EndedWithError"
        const val TERMINATING = "Terminating"
    }

    private fun isSandboxActive(environment: QualiColonyGatewayApiModelResponsesEnvironmentResponse): Boolean {
        val status = environment.details.computedStatus
        if (status.equals(SandboxStatus.LAUNCHING))
            return false

        if (status.equals(SandboxStatus.ACTIVE))
            return true

        if (status.equals(SandboxStatus.ACTIVE_WITH_ERROR)) {
            val grainsStatusesString = formatAppsDeploymentStatuses(environment)
            throw Throwable("Sandbox deployment failed with status ${environment.details.computedStatus}, " +
                    "grains deployment statuses are: $grainsStatusesString")
        }

        if (status.equals(SandboxStatus.ENDED) ||
            status.equals(SandboxStatus.TERMINATING) ||
            status.equals(SandboxStatus.ENDED_WITH_ERROR))
            throw Throwable("Sandbox with id ${environment.details.id} has been ended")

        throw Throwable("Sandbox with id ${environment.details.id} has unknown sandbox status $status")
    }

    private fun formatAppsDeploymentStatuses(sandbox: QualiColonyGatewayApiModelResponsesEnvironmentResponse): String {
        val builder = StringBuilder()
        var isFirst = true

        for (grain in sandbox.details.state.grains){
            if (isFirst) isFirst = false else builder.append(", ")
            builder.append(java.lang.String.format("%s: %s", grain.name, grain.state.currentState))
        }

        if (sandbox.details.state.errors.isNotEmpty()) {
            builder.append(System.getProperty("line.separator"))
            builder.append("Sandbox Errors: ")
            for (error in sandbox.details.state.errors) {
                if (isFirst) isFirst = false else builder.append(", ")
                builder.append(String.format("%s", error.message))
            }
        }
        return builder.toString()
    }

    private fun getSandboxEndpoints(
        environment: QualiColonyGatewayApiModelResponsesEnvironmentResponse
    ): Map<String, String> {
        val sc: MutableMap<String, String> = HashMap()
        for (output in environment.details.state.outputs) {
            if (output.kind === "link") {
                log.info(String.format("Link output %s found: %s", output.name, output.value))
                sc[output.name] = output.value
            }
        }
        return sc
    }
    override fun execute(stage: StageExecution): TaskResult {
        log.info("VerifySandboxIsActive task is running")

        val sandboxId = stage.context["sandboxId"] as String
        val space = stage.context["space"] as String
        val timeoutMinutes = stage.context["timeoutMinutes"] as Int
        val token = stage.context["token"] as String
        val url = config.torqueUrl

        // Rewrite task timeout with user input
        taskTimeout = TimeUnit.MINUTES.toMillis(timeoutMinutes.toLong())

        val api = EnvironmentApi(TorqueAuth(token, url).getClient())

        log.info("Getting sandbox")
        val sandboxResponse = api.apiSpacesSpaceNameEnvironmentsEnvironmentIdGet(space, sandboxId)
//        val sandboxData = Gson().fromJson(sandbox.rawBodyJson, SingleSandbox::class.java)

        val isActive: Boolean

        try {
            isActive = isSandboxActive(sandboxResponse)
        }
        catch (e: Throwable) {
            addExceptionToOutput(stage, e)
            return TaskResult.builder(ExecutionStatus.TERMINAL)
                .context(stage.context)
                .outputs(stage.outputs)
                .build()
        }

        return if (isActive) {
            log.info("Sandbox is active. Finishing task")
            val jsonRes = Gson().toJson(sandboxResponse)
            stage.outputs["SandboxDetails"] = jsonRes
            stage.outputs["QuickLinks"] = getSandboxEndpoints(sandboxResponse)
            TaskResult.builder(ExecutionStatus.SUCCEEDED)
                    .context(stage.context)
                    .outputs(stage.outputs)
                    .build()
        }
        else {
            TaskResult.builder(ExecutionStatus.RUNNING).build()
        }
    }

    override fun onCancel(stage: StageExecution) {
        addObjectToStageContext(stage, "sandboxId", stage.context["sandboxId"])
        this.endSandboxTask.execute(stage)
    }

    override fun onTimeout(stage: StageExecution): TaskResult {
        addErrorMessage(stage, "Sandbox is not active after ${stage.context["timeoutMinutes"]} minutes.")
        //TODO: maybe we need to stop sandbox as well
        return TaskResult.builder(ExecutionStatus.TERMINAL)
                .context(stage.context)
                .outputs(stage.outputs)
                .build()
    }

    override fun getBackoffPeriod(): Long {
        return TimeUnit.SECONDS.toMillis(15)
    }

    override fun getTimeout(): Long {
        return taskTimeout
    }
}