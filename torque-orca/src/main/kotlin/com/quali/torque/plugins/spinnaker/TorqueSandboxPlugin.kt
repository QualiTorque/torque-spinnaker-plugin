package com.quali.torque.plugins.spinnaker

import org.pf4j.Plugin
import org.pf4j.PluginWrapper
import org.slf4j.LoggerFactory



class TorqueSandboxPlugin(wrapper: PluginWrapper) : Plugin(wrapper) {
    private val logger = LoggerFactory.getLogger(TorqueSandboxPlugin::class.java)

    override fun start() {
        logger.info("TorqueSandboxPlugin.start()")
    }

    override fun stop() {
        logger.info("TorqueSandboxPlugin.stop()")
    }
}
