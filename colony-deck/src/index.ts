import { IDeckPlugin } from '@spinnaker/core';
import { colonySandboxStage } from './ColonySandboxStage';
import { initialize } from './initialize';

export const plugin: IDeckPlugin = {
  initialize,
  stages: [colonySandboxStage],
};
