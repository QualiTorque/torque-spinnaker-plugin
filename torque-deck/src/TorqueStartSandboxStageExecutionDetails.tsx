import React from 'react';

import {
    ExecutionDetailsSection,
    IExecutionDetailsSectionProps,
    StageFailureMessage
} from '@spinnaker/core'

export function TorqueStartSandboxStageExecutionDetails(props: IExecutionDetailsSectionProps) {
  let links: Map<string,string> = props.stage.outputs.QuickLinks;
  let sandboxUrl = props.stage.outputs.sandboxUrl;

  const items: JSX.Element[] = [];

  links.forEach((name: string, link: string) => {
      items.push(<p><b>{name}: </b><a href={link}>{link}</a></p>)
  })

  return (
    <ExecutionDetailsSection name={props.name} current={props.current}>
      <StageFailureMessage stage={props.stage} message={props.stage.outputs.failureMessage} />
      <div>

        <p>Started sandbox with id: <b> <a href={sandboxUrl}> {props.stage.outputs.sandboxId} </a></b></p>
        <div>
            {items}
        </div>
      </div>
    </ExecutionDetailsSection>
  );
}

export namespace TorqueStartSandboxStageExecutionDetails {
  export const title = 'torqueStartSandbox';
}