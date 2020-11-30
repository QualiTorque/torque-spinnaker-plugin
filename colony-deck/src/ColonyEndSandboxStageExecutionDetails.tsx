import React from 'react';

import {
    ExecutionDetailsSection,
    IExecutionDetailsSectionProps,
} from '@spinnaker/core'

export function ColonyEndSandboxStageExecutionDetails(props: IExecutionDetailsSectionProps) {
  return (
    <ExecutionDetailsSection name={props.name} current={props.current}>
      <div>
        <p>Result of stage is {props.stage.outputs.finalSandbox}</p>
      </div>
    </ExecutionDetailsSection>
  );
}

export namespace ColonyEndSandboxStageExecutionDetails {
  export const title = 'colonyEndSandbox';
}