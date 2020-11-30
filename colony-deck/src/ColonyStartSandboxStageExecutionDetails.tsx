import React from 'react';

import {
    ExecutionDetailsSection,
    IExecutionDetailsSectionProps,
} from '@spinnaker/core'

export function ColonyStartSandboxStageExecutionDetails(props: IExecutionDetailsSectionProps) {
  return (
    <ExecutionDetailsSection name={props.name} current={props.current}>
      <div>
        <p>Started sandbox with id {props.stage.outputs.sandboxId}</p>
      </div>
    </ExecutionDetailsSection>
  );
}

export namespace ColonyStartSandboxStageExecutionDetails {
  export const title = 'colonyStartSandbox';
}