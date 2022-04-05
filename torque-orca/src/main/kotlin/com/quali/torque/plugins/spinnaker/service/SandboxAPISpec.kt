package com.quali.torque.plugins.spinnaker.service

import com.quali.torque.plugins.spinnaker.api.CreateSandboxRequest
import com.quali.torque.plugins.spinnaker.api.CreateSandboxResponse
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by shay-k on 21/06/2017.
 */
interface SandboxAPISpec {
    @POST("api/spaces/{spaceName}/sandbox")
    fun createSandbox(@Header("Authorization") token: String?,
                      @Path("spaceName") spaceName: String?,
                      @Body request: CreateSandboxRequest?): Call<CreateSandboxResponse?>?

    @DELETE("api/spaces/{spaceName}/sandbox/{sandboxId}")
    fun deleteSandbox(@Header("Authorization") token: String?,
                      @Path("spaceName") spaceName: String?,
                      @Path("sandboxId") sandboxId: String?): Call<Void?>?

    @GET("api/spaces/{spaceName}/sandbox/{sandboxId}")
    fun getSandboxById(@Header("Authorization") token: String?,
                       @Path("spaceName") spaceName: String?,
                       @Path("sandboxId") sandboxId: String?): Call<Any?>?
}