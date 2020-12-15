package com.quali.colony.plugins.spinnaker

import com.netflix.spinnaker.kork.plugins.tck.PluginsTck
import com.netflix.spinnaker.kork.plugins.tck.serviceFixture
import com.quali.colony.plugins.spinnaker.stages.ColonyEndSandboxStage
import com.quali.colony.plugins.spinnaker.stages.ColonyStartSandboxStage
import dev.minutest.rootContext
import strikt.api.expect
import strikt.assertions.isEqualTo

/**
 * This test demonstrates that the ColonySandboxPlugin can be loaded by Orca
 * and that ColonySandboxStages StageDefinitionBuilder can be retrieved at runtime.
 */
class ColonySandboxStageTckTest : PluginsTck<ColonySandboxPluginsFixture>() {

    fun tests() = rootContext<ColonySandboxPluginsFixture> {
        context("a running Orca instance") {
            serviceFixture {
                ColonySandboxPluginsFixture()
            }

            defaultPluginTests()

            test("ColonyStartSandboxStage extension is resolved to the correct type") {
                val stageDefinitionBuilder = stageResolver.getStageDefinitionBuilder(
                    ColonyStartSandboxStage::class.java.simpleName, "colonyStartSandbox")
                expect {
                    that(stageDefinitionBuilder.type).isEqualTo("colonyStartSandbox")
                }
            }

            test("ColonyEndSandboxStage extension is resolved to the correct type") {
                val stageDefinitionBuilder = stageResolver.getStageDefinitionBuilder(
                    ColonyEndSandboxStage::class.java.simpleName, "colonyEndSandbox")
                expect {
                    that(stageDefinitionBuilder.type).isEqualTo("colonyEndSandbox")
                }
            }
        }
    }
}