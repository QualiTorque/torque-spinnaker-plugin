import React from 'react';

import {
    ExecutionDetailsSection,
    IExecutionDetailsSectionProps,
    StageFailureMessage
} from '@spinnaker/core'

export function ColonyStartSandboxStageExecutionDetails(props: IExecutionDetailsSectionProps) {
  return (
    <ExecutionDetailsSection name={props.name} current={props.current}>
      <StageFailureMessage stage={props.stage} message={props.stage.outputs.failureMessage} />
      <div>
        <p>Started sandbox with id: <b> {props.stage.outputs.sandboxId} </b></p>
      </div>
    </ExecutionDetailsSection>
  );
}

export namespace ColonyStartSandboxStageExecutionDetails {
  export const title = 'colonyStartSandbox';
}