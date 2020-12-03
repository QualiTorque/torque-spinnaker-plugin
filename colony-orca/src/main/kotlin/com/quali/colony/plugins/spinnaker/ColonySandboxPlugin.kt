package com.quali.colony.plugins.spinnaker

import org.pf4j.Plugin
import org.pf4j.PluginWrapper
import org.slf4j.LoggerFactory



class ColonySandboxPlugin(wrapper: PluginWrapper) : Plugin(wrapper) {
    private val logger = LoggerFactory.getLogger(ColonySandboxPlugin::class.java)

    override fun start() {
        logger.info("ColonySandboxPlugin.start()")
    }

    override fun stop() {
        logger.info("ColonySandboxPlugin.stop()")
    }
}
