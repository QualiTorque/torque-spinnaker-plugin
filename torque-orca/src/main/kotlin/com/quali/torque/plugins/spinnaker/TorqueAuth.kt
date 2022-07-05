package com.quali.torque.plugins.spinnaker

import com.quali.torque.client.ApiClient
import com.quali.torque.client.Configuration
import com.quali.torque.plugins.spinnaker.utils.StatusService

class TorqueAuth(private val token: String, private val url: String) {
    fun getClient(): ApiClient {
        val version = StatusService().getVersion()
        val defaultClient: ApiClient = Configuration.getDefaultApiClient()
        defaultClient.basePath = url
        defaultClient.setUserAgent("Torque-Plugin-Bamboo/$version")
        defaultClient.setApiKey(String.format("Bearer %s", token))
        defaultClient.connectTimeout = 60 * 1000
        defaultClient.readTimeout = 60 * 1000
        return defaultClient

    }
}