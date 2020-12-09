import React from 'react';

import {
    ExecutionDetailsSection,
    IExecutionDetailsSectionProps,
    StageFailureMessage
} from '@spinnaker/core'

export function ColonyStartSandboxStageExecutionDetails(props: IExecutionDetailsSectionProps) {
  var apps = props.stage.outputs.QuickLinks;
  var sburl = props.stage.outputs.sandboxUrl;

  const items=[];

  if(apps){
    for(let app of apps) {
        const href=[]
        for(let link of app.shortcuts){
          href.push(<li><a href={link}>{link}</a></li>)
        }
        items.push(<p><b>{app.name}: </b>{href}</p>)
    }
  }

  return (
    <ExecutionDetailsSection name={props.name} current={props.current}>
      <StageFailureMessage stage={props.stage} message={props.stage.outputs.failureMessage} />
      <div>

        <p>Started sandbox with id: <b> <a href={sburl}> {props.stage.outputs.sandboxId} </a></b></p>
        <div>
            {items}
        </div>
      </div>
    </ExecutionDetailsSection>
  );
}

export namespace ColonyStartSandboxStageExecutionDetails {
  export const title = 'colonyStartSandbox';
}