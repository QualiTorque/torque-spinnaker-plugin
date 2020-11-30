import { IDeckPlugin } from '@spinnaker/core';
import { colonyStartSandboxStage } from './ColonyStartSandboxStage';
import { colonyEndSandboxStage } from './ColonyEndSandboxStage'
import { initialize } from './initialize';

export const plugin: IDeckPlugin = {
  initialize,
  stages: [colonyStartSandboxStage, colonyEndSandboxStage]
}