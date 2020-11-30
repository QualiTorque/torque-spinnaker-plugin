package com.quali.colony.plugins.spinnaker.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Sandbox : Serializable {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("blueprint")
    var blueprint: String? = null

    @SerializedName("services")
    var services: List<Service>? = null

    @SerializedName("sandbox_status")
    var sandboxStatus: String? = null
}