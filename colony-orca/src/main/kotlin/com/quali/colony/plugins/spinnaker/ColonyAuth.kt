package com.quali.colony.plugins.spinnaker

import com.quali.colony.plugins.spinnaker.service.SandboxAPIServiceImpl
import com.quali.colony.plugins.spinnaker.service.SandboxServiceConnection

class ColonyAuth(private val config: ColonyConfig) {
    fun getAPI(): SandboxAPIServiceImpl {
        val token = this.config.colonyToken
        val url = this.config.colonyUrl

        val apiConnection = SandboxServiceConnection(url, token, 10, 30)
        // TODO(ddovbii) add some connection testing
        return SandboxAPIServiceImpl(apiConnection)
    }
}