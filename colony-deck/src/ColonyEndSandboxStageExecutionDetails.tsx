import React from 'react';

import {
    ExecutionDetailsSection,
    IExecutionDetailsSectionProps,
} from '@spinnaker/core'

export function ColonyEndSandboxStageExecutionDetails(props: IExecutionDetailsSectionProps) {
  return (
    <ExecutionDetailsSection name={props.name} current={props.current}>
      <div>
        <p>Sandbox has been stopped</p>
      </div>
    </ExecutionDetailsSection>
  );
}

export namespace ColonyEndSandboxStageExecutionDetails {
  export const title = 'colonyEndSandbox';
}