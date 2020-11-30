package com.quali.colony.plugins.spinnaker.service

/**
 * Created by shay-k on 18/06/2017.
 */
class SandboxServiceConnection(val address: String, val token: String, val connectionTimeoutSec: Int, val readTimeoutSec: Int) {
    val authorizationHeader: String
        get() = String.format("Bearer %s", token)

}