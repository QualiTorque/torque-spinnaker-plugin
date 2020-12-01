import { HelpContentsRegistry } from '@spinnaker/core'

export const initialize = () => {
  HelpContentsRegistry.register('quali.colonyStartSandboxStage.blueprintName', 'Provide Colony blueprint name.');
  HelpContentsRegistry.register('quali.colonyStartSandboxStage.sandboxName', 'Provide name of sandbox you are going to start.');
  HelpContentsRegistry.register('quali.colonyStartSandboxStage.space', 'Desired Colony space');
  HelpContentsRegistry.register('quali.colonyStartSandboxStage.artifacts',
    'Comma separated list of artifacts names with paths. Example: artifact1=path1, artifact2=path2');
  HelpContentsRegistry.register('quali.colonyStartSandboxStage.inputs',
    'Comma separated list of key-values. Example: key1=value1, key2=value2');
  HelpContentsRegistry.register('quali.colonyStartSandboxStage.duration', 'Provide humber of hours you want to reserve');
  HelpContentsRegistry.register('quali.colonyStartSandboxStage.timeout', 'Set the timeout for the sandbox to become active');
};