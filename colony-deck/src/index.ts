import { IDeckPlugin } from '@spinnaker/core';
import { colonySandboxStage } from './ColonySandboxStage';
import { colonyEndSandboxStage } from './ColonyEndSandboxStage'
import { initialize } from './initialize';

export const plugin: IDeckPlugin = {
  initialize,
  stages: [colonySandboxStage, colonyEndSandboxStage