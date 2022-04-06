package com.quali.torque.plugins.spinnaker.utils

import java.util.*

class StatusService {
    private val versionProperties = Properties()
    init {
        versionProperties.load(this.javaClass.getResourceAsStream("/version.properties"))
    }
    fun getVersion() : String = versionProperties.getProperty("version") ?: "no version"
}