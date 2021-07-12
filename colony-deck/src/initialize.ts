import { HelpContentsRegistry } from '@spinnaker/core'

export const initialize = () => {
  HelpContentsRegistry.register('quali.colonyStartSandboxStage.blueprintName', 'Provide Colony blueprint name. Must be inside specified Space.');
  HelpContentsRegistry.register('quali.colonyStartSandboxStage.sandboxName',
    'Provide name of Sandbox. Common use case for sandbox names is to use the build id as part of the name. Example: ${ execution["name"] }-${ execution["id"] }');
  HelpContentsRegistry.register('quali.colonyStartSandboxStage.space', 'Colony Space Name.');
  HelpContentsRegistry.register('quali.colonyStartSandboxStage.artifacts',
    'Comma separated list of artifacts with paths where artifacts are defined per application. The artifact name is the name of the application. Example: appName1=path1, appName2=path2');
  HelpContentsRegistry.register('quali.colonyStartSandboxStage.inputs',
    'Comma separated list of key-values. Example: key1=value1, key2=value2');
  HelpContentsRegistry.register('quali.colonyStartSandboxStage.duration', 'Sandbox will automatically deprovision at the end of the provided duration. Includes time it takes to provision.');
  HelpContentsRegistry.register('quali.colonyStartSandboxStage.timeout', 'Set the timeout in minutes for the sandbox to become active.');
  HelpContentsRegistry.register('quali.colonyEndSandboxStage.sandboxId', 'Specify sandbox ID. In most cases you will take it from Start Sandbox Stage context. Example: ${ #stage("Colony Start Sandbox")["outputs"]["sandboxId"]}');
  HelpContentsRegistry.register('quali.colonyEndSandboxStage.space', 'Colony Space Name (please use one from Start Sandbox Stage). Example: ${ #stage("Colony Start Sandbox")["context"]["space"]}');
  HelpContentsRegistry.register('quali.colonyStartSandboxStage.token', 'Colony Token. Set, if you want to override token in config file');

};