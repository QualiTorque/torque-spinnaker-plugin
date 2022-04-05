package com.quali.torque.plugins.spinnaker.api

class CreateSandboxRequest(val blueprint_name: String,
                           val sandbox_name: String,
                           val artifacts: Map<String, String>,
                           val automation: Boolean,
                           val inputs: Map<String, String>,
                           val duration: String) {
    val description = "JenkinsOriginSandbox"

}