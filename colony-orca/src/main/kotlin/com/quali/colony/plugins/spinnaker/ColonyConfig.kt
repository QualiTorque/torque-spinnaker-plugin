package com.quali.colony.plugins.spinnaker

//import com.netflix.spinnaker.kork.plugins.api.ExtensionConfiguration
import com.netflix.spinnaker.kork.plugins.api.PluginConfiguration


/**
 * Data in this class maps to the plugin configuration in a service's config YAML.
 * The data can be key/value pairs or an entire configuration tree.
 *
 */
//@ExtensionConfiguration("quali.colonySandboxStage")
@PluginConfiguration("quali.colonySandboxStage")
//@PluginConfiguration
data class ColonyConfig(
        var colonyToken: String,
        var colonyUrl: String = "https://cloudshellcolony.com"
)
