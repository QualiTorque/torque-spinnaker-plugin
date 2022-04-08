package com.quali.torque.plugins.spinnaker.service

import com.google.gson.GsonBuilder
import com.quali.torque.plugins.spinnaker.utils.StatusService
import com.quali.torque.plugins.spinnaker.api.CreateSandboxRequest
import com.quali.torque.plugins.spinnaker.api.CreateSandboxResponse
import com.quali.torque.plugins.spinnaker.api.ResponseData
import com.quali.torque.plugins.spinnaker.api.ResponseData.Companion.error
import com.quali.torque.plugins.spinnaker.api.ResponseData.Companion.ok
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class SandboxAPIServiceImpl(private val connection: SandboxServiceConnection) : SandboxAPIService {
    private val sandboxAPI: SandboxAPISpec

    @Throws(RuntimeException::class, IOException::class)
    override fun getSandboxById(spaceName: String?, sandboxId: String): ResponseData<Any?> {
        return execute(sandboxAPI.getSandboxById(connection.authorizationHeader, spaceName, sandboxId))
    }

    @Throws(IOException::class)
    override fun createSandbox(spaceName: String?, req: CreateSandboxRequest?): ResponseData<CreateSandboxResponse?> {
        return execute(sandboxAPI.createSandbox(connection.authorizationHeader, spaceName, req))
    }

    @Throws(IOException::class)
    override fun deleteSandbox(spaceName: String?, sandboxId: String): ResponseData<Void?> {
        return execute(sandboxAPI.deleteSandbox(connection.authorizationHeader, spaceName, sandboxId))
    }

    @Throws(IOException::class)
    fun <T> execute(call: Call<T>?): ResponseData<T> {
        return parseResponse(call!!.execute())
    }

    companion object {
        @Throws(IOException::class)
        private fun <T> parseResponse(response: Response<T>): ResponseData<T> {
            val message = response.message()
            if (!response.isSuccessful) {
                val err = response.errorBody()!!.string()
                return error<Any>(response.code(), err).setMessage(message)
            }
            val rawBodyJson = GsonBuilder().setPrettyPrinting().create().toJson(response.body())
            return ok(response.body(), response.code(), rawBodyJson).setMessage(message)
        }

        private fun <T> createErrorResponse(e: Exception): ResponseData<T> {
            val errorMessage = e.toString()
            return error(-1, errorMessage)
        }
    }

    init {
        val gson = GsonBuilder()
                .setLenient()
                .create()
        val builder = OkHttpClient.Builder()

        // add user-agent header for usage reports
        val version = StatusService().getVersion()
        builder.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("User-Agent", "Torque-Plugin-Spinnaker/${version}")

            val request = requestBuilder.build()
            chain.proceed(request)
        }

        builder.connectTimeout(connection.connectionTimeoutSec.toLong(), TimeUnit.SECONDS)
        builder.readTimeout(connection.readTimeoutSec.toLong(), TimeUnit.SECONDS)

        val client = builder.build()
        val baseUrl = String.format("%1\$s", connection.address)
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
        sandboxAPI = retrofit.create(SandboxAPISpec::class.java)
    }
}