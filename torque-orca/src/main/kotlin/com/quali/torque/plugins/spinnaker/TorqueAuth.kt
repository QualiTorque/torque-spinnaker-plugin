package com.quali.torque.plugins.spinnaker

import com.quali.torque.client.ApiClient
import com.quali.torque.client.Configuration
import com.quali.torque.plugins.spinnaker.utils.StatusService
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

class TorqueAuth(private val token: String, private val url: String) {
    fun getClient(): ApiClient {
        val version = StatusService().getVersion()
        val defaultClient: ApiClient = Configuration.getDefaultApiClient()
        defaultClient.basePath = url
        defaultClient.setUserAgent("Torque-Plugin-Bamboo/$version")
        val f = DateTimeFormatterBuilder()
            .appendPattern("[yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX][yyyy-MM-dd'T'HH:mm:ss.SSSSSXXX][yyyy-MM-dd'T'HH:mm:ss.SSSSSS][yyyy-MM-dd'T'HH:mm:ss.SSSSS][yyyy-MM-dd'T'HH:mm:ss]")
            .parseLenient()
            .parseDefaulting(ChronoField.OFFSET_SECONDS, 0)
            .toFormatter()
        defaultClient.setOffsetDateTimeFormat(f)
        defaultClient.setApiKey(String.format("Bearer %s", token))
        defaultClient.connectTimeout = 60 * 1000
        defaultClient.readTimeout = 60 * 1000
        return defaultClient
    }
}