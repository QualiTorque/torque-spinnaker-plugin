import React from 'react';

import {
    ExecutionDetailsSection,
    IExecutionDetailsSectionProps,
} from '@spinnaker/core'

export function ColonySandboxStageExecutionDetails(props: IExecutionDetailsSectionProps) {
  return (
    <ExecutionDetailsSection name={props.name} current={props.current}>
      <div>
        <p>Result of stage is {props.stage.outputs.resultSandbox}</p>
      </div>
    </ExecutionDetailsSection>
  );
}

export namespace ColonySandboxStageExecutionDetails {
  export const title = 'colonySandbox';
}