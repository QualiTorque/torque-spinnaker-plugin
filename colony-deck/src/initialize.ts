import { HelpContentsRegistry } from '@spinnaker/core'

export const initialize = () => {
  HelpContentsRegistry.register('quali.colonySandboxStage.sandboxId', 'Provide sandbox ID.');
};