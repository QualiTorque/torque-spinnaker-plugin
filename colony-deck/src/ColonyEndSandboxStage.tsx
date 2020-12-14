import React from 'react';

import {
  ExecutionDetailsTasks,
  IStageTypeConfig,
} from '@spinnaker/core';

import { ColonyEndSandboxStageExecutionDetails } from './ColonyEndSandboxStageExecutionDetails';
import { ColonyEndSandboxStageConfig } from './ColonyEndSandboxStageConfig';


/*
  Define Spinnaker Stages with IStageTypeConfig.
  Required options: https://github.com/spinnaker/deck/master/app/scripts/modules/core/src/domain/IStageTypeConfig.ts
  - label -> The name of the Stage
  - description -> Long form that describes what the Stage actually does
  - key -> A unique name for the Stage in the UI; ties to Orca backend
  - component -> The rendered React component
  - validateFn -> A validation function for the stage config form.
 */
export const colonyEndSandboxStage: IStageTypeConfig = {
  key: 'colonyEndSandbox',
  label: `End Sandbox`,
  description: 'Ends the specified Sandbox with the provided Space and Sandbox ID. Recommend using environment variables to pull in values from Start Sandbox stage',
  component: ColonyEndSandboxStageConfig, // stage config
  executionDetailsSections: [ColonyEndSandboxStageExecutionDetails, ExecutionDetailsTasks],
};
