package com.quali.colony.plugins.spinnaker

import com.quali.colony.plugins.spinnaker.service.SandboxAPIServiceImpl
import com.quali.colony.plugins.spinnaker.service.SandboxServiceConnection

class ColonyAuth(private val token: String, private val url: String) {
    fun getAPI(): SandboxAPIServiceImpl {
        val apiConnection = SandboxServiceConnection(url, token, 10, 30)
        // TODO(ddovbii) add some connection testing
        return SandboxAPIServiceImpl(apiConnection)
    }
}