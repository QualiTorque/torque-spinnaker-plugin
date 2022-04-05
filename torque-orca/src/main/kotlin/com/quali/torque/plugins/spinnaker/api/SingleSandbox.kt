package com.quali.torque.plugins.spinnaker.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SingleSandbox : Serializable {
    @JvmField
    @SerializedName("id")
    var id: String? = null

    @JvmField
    @SerializedName("name")
    var name: String? = null

    @SerializedName("blueprint_name")
    var blueprint_name: String? = null

    @SerializedName("applications")
    var applications: List<Service>? = null

    @JvmField
    @SerializedName("sandbox_status")
    var sandboxStatus: String? = null

    @SerializedName("errors")
    var sandboxErrors: List<SandboxErrorService>? = null
}