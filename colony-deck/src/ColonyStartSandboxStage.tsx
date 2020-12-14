import React from 'react';

import {
  ExecutionDetailsTasks,
  IStageTypeConfig,
} from '@spinnaker/core';

import { ColonyStartSandboxStageExecutionDetails } from './ColonyStartSandboxStageExecutionDetails';
import { ColonyStartSandboxStageConfig, validate } from './ColonyStartSandboxStageConfig';


/*
  Define Spinnaker Stages with IStageTypeConfig.
  Required options: https://github.com/spinnaker/deck/master/app/scripts/modules/core/src/domain/IStageTypeConfig.ts
  - label -> The name of the Stage
  - description -> Long form that describes what the Stage actually does
  - key -> A unique name for the Stage in the UI; ties to Orca backend
  - component -> The rendered React component
  - validateFn -> A validation function for the stage config form.
 */
export const colonyStartSandboxStage: IStageTypeConfig = {
  key: 'colonyStartSandbox',
  label: `Start Sandbox`,
  description: 'Starts a Sandbox from a given Blueprint inside the specified Space. Allows for multiple artifacts and inputs (parameters) using key value pairs in csv format. Please set values for all the artifacts and inputs defined in the Blueprint, default values in the Blueprint that are visible in the UI are not supported. Timeout value needs to be a sufficient number of minutes to allow the Sandbox to finish deployment and become active. Duration parameter sets a TTL policy for how long the Sandbox stays active once deployed (includes deployment time) before it’s automatically deprovisioned',
  component: ColonyStartSandboxStageConfig, // stage config
  executionDetailsSections: [ColonyStartSandboxStageExecutionDetails, ExecutionDetailsTasks],
  validateFn: validate,
};
