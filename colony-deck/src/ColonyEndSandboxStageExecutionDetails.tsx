import React from 'react';

import {
    ExecutionDetailsSection,
    IExecutionDetailsSectionProps,
    StageFailureMessage
} from '@spinnaker/core'

export function ColonyEndSandboxStageExecutionDetails(props: IExecutionDetailsSectionProps) {
  return (
    <ExecutionDetailsSection name={props.name} current={props.current}>
      <StageFailureMessage stage={props.stage} message={props.stage.outputs.failureMessage} />
      <div>
        <p>Sandbox <b> {props.stage.outputs.sandboxId} </b> has been stopped</p>
      </div>
    </ExecutionDetailsSection>
  );
}

export namespace ColonyEndSandboxStageExecutionDetails {
  export const title = 'colonyEndSandbox';
}