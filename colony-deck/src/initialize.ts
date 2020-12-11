import { HelpContentsRegistry } from '@spinnaker/core'

export const initialize = () => {
  HelpContentsRegistry.register('quali.colonyStartSandboxStage.blueprintName', 'Provide Colony blueprint name.');
  HelpContentsRegistry.register('quali.colonyStartSandboxStage.sandboxName', 'Provide name of Sandbox.');
  HelpContentsRegistry.register('quali.colonyStartSandboxStage.space', 'Colony Space Name.');
  HelpContentsRegistry.register('quali.colonyStartSandboxStage.artifacts',
    'Comma separated list of artifacts names with paths. Example: artifact1=path1, artifact2=path2');
  HelpContentsRegistry.register('quali.colonyStartSandboxStage.inputs',
    'Comma separated list of key-values. Example: key1=value1, key2=value2');
  HelpContentsRegistry.register('quali.colonyStartSandboxStage.duration', 'Provide Sandbox duration in minutes.');
  HelpContentsRegistry.register('quali.colonyStartSandboxStage.timeout', 'Set the timeout in minutes for the sandbox to become active.');
  HelpContentsRegistry.register('quali.colonyEndSandboxStage.sandboxId', 'Specify sandbox ID.');
  HelpContentsRegistry.register('quali.colonyEndSandboxStage.space', 'Desired Colony space (please use one from start).');
};