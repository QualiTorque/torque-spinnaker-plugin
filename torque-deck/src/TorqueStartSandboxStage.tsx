import React from 'react';

import {
  ExecutionDetailsTasks,
  IStageTypeConfig,
} from '@spinnaker/core';

import { TorqueStartSandboxStageExecutionDetails } from './TorqueStartSandboxStageExecutionDetails';
import { TorqueStartSandboxStageConfig, validate } from './TorqueStartSandboxStageConfig';


/*
  Define Spinnaker Stages with IStageTypeConfig.
  Required options: https://github.com/spinnaker/deck/master/app/scripts/modules/core/src/domain/IStageTypeConfig.ts
  - label -> The name of the Stage
  - description -> Long form that describes what the Stage actually does
  - key -> A unique name for the Stage in the UI; ties to Orca backend
  - component -> The rendered React component
  - validateFn -> A validation function for the stage config form.
 */
export const torqueStartSandboxStage: IStageTypeConfig = {
  key: 'torqueStartSandbox',
  label: `Torque Start Sandbox`,
  description: 'Starts a Sandbox from a given Blueprint inside the specified Space. Please set values for all inputs defined in the Blueprint, default values in the Blueprint that are visible in the UI are not supported.',
  component: TorqueStartSandboxStageConfig, // stage config
  executionDetailsSections: [TorqueStartSandboxStageExecutionDetails, ExecutionDetailsTasks],
  validateFn: validate,
};
