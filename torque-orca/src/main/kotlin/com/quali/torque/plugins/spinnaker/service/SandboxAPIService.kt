package com.quali.torque.plugins.spinnaker.service

import com.quali.torque.plugins.spinnaker.api.CreateSandboxRequest
import com.quali.torque.plugins.spinnaker.api.CreateSandboxResponse
import com.quali.torque.plugins.spinnaker.api.ResponseData
import java.io.IOException

interface SandboxAPIService {
    @Throws(IOException::class)
    fun createSandbox(spaceName: String?, req: CreateSandboxRequest?): ResponseData<CreateSandboxResponse?>

    @Throws(IOException::class)
    fun deleteSandbox(spaceName: String?, sandboxId: String): ResponseData<Void?>

    @Throws(IOException::class)
    fun getSandboxById(spaceName: String?, sandboxId: String): ResponseData<Any?>
}