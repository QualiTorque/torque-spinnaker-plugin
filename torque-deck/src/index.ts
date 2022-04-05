import { IDeckPlugin } from '@spinnaker/core';
import { torqueStartSandboxStage } from './TorqueStartSandboxStage';
import { torqueEndSandboxStage } from './TorqueEndSandboxStage'
import { initialize } from './initialize';

export const plugin: IDeckPlugin = {
  initialize,
  stages: [torqueStartSandboxStage, torqueEndSandboxStage]
}