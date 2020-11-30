package com.quali.colony.plugins.spinnaker.api

import groovy.transform.builder.Builder
import java.io.Serializable

/**
 * Created by shay-k on 21/06/2017.
 */
@Builder
class ResponseData<T> : Serializable {
    var data: T? = null
        private set
    var statusCode: Int
        private set
    var rawBodyJson: String? = null
        private set
    var error: String? = null
        private set
    var message: String? = null
        private set

    private constructor(statusCode: Int) {
        this.statusCode = statusCode
    }

    private constructor(data: T, statusCode: Int, rawBodyJson: String) {
        this.data = data
        this.statusCode = statusCode
        this.rawBodyJson = rawBodyJson
    }

    private constructor(statusCode: Int, error: String) {
        this.statusCode = statusCode
        this.error = error
    }

    val isSuccessful: Boolean
        get() = error == null

    fun <T> setMessage(message: String?): ResponseData<T> {
        this.message = message
        return this as ResponseData<T>
    }

    companion object {
        @JvmStatic
        fun <T> ok(data: T, statusCode: Int, rawBodyJson: String): ResponseData<T> {
            return ResponseData(data, statusCode, rawBodyJson)
        }

        @JvmStatic
        fun <T> error(statusCode: Int, error: String): ResponseData<T> {
            return ResponseData(statusCode, error)
        }
    }
}