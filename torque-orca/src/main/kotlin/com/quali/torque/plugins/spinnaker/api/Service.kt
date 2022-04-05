package com.quali.torque.plugins.spinnaker.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Service : Serializable {
    @SerializedName("name")
    var name: String? = null

    @SerializedName("shortcuts")
    var shortcuts: List<String>? = null

    @SerializedName("status")
    var status: String? = null
}