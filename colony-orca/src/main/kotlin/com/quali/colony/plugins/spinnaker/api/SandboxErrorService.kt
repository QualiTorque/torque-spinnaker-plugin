package com.quali.colony.plugins.spinnaker.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

//Error from the sandbox get by id response
class SandboxErrorService : Serializable {
    @SerializedName("time")
    var time: String? = null

    @SerializedName("code")
    var code: String? = null

    @SerializedName("message")
    var message: String? = null
}