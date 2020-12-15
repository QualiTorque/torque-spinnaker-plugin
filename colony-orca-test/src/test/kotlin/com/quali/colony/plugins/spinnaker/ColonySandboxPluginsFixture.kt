package com.quali.colony.plugins.spinnaker

import com.netflix.spinnaker.kork.plugins.SpinnakerPluginManager
import com.netflix.spinnaker.kork.plugins.internal.PluginJar
import com.netflix.spinnaker.kork.plugins.tck.PluginsTckFixture
import com.netflix.spinnaker.orca.StageResolver
import com.netflix.spinnaker.orca.api.test.OrcaFixture
import com.quali.colony.plugins.spinnaker.stages.ColonyStartSandboxStage
import com.quali.colony.plugins.spinnaker.stages.ColonyEndSandboxStage
import java.io.File
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.TestPropertySource

@TestPropertySource(properties = [
  "spinnaker.extensibility.plugins.Quali.ColonySandboxPlugin.enabled=true",
  "spinnaker.extensibility.plugins.Quali.ColonySandboxPlugin.config.colonyToken=abc",
  "spinnaker.extensibility.plugins.Quali.ColonySandboxPlugin.disabled=true",
  "spinnaker.extensibility.plugins.Quali.ColonySandboxPlugin-not-supported.enabled=true",
  "spinnaker.extensibility.plugins-root-path=build/plugins"
])
class ColonySandboxPluginsFixture : PluginsTckFixture, OrcaFixture() {
  final override val plugins = File("build/plugins")
  final override val enabledPlugin: PluginJar
  final override val disabledPlugin: PluginJar
  final override val versionNotSupportedPlugin: PluginJar

  override val extensionClassNames: MutableList<String> = mutableListOf(
    ColonyStartSandboxStage::class.java.name, ColonyEndSandboxStage::class.java.name
  )

  final override fun buildPlugin(pluginId: String, systemVersionRequirement: String): PluginJar {
    return PluginJar.Builder(plugins.toPath().resolve("$pluginId.jar"), pluginId)
      .pluginClass(ColonySandboxPlugin::class.java.name)
      .pluginVersion("1.0.0")
      .manifestAttribute("Plugin-Requires", "orca$systemVersionRequirement")
      .extensions(extensionClassNames)
      .build()
  }

  @Autowired
  override lateinit var spinnakerPluginManager: SpinnakerPluginManager

  @Autowired
  lateinit var stageResolver: StageResolver

  init {
    plugins.delete()
    plugins.mkdir()
    enabledPlugin = buildPlugin("Quali.ColonySandboxPlugin.enabled", ">=1.0.0")
    disabledPlugin = buildPlugin("Quali.ColonySandboxPlugin.disabled", ">=1.0.0")
    versionNotSupportedPlugin = buildPlugin("Quali.ColonySandboxPlugin-not-supported", ">=5000.0.0")
  }
}