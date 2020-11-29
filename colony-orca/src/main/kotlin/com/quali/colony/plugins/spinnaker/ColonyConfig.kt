package com.quali.colony.plugins.spinnaker

import com.netflix.spinnaker.kork.plugins.api.ExtensionConfiguration


/**
 * Data in this class maps to the plugin configuration in a service's config YAML.
 * The data can be key/value pairs or an entire configuration tree.
 *
 */
@ExtensionConfiguration("quali.colonySandboxStage")
data class ColonyConfig(
        var colonyToken: String,
        var colonyUrl: String = "https://cloudshellcolony.com"
)
