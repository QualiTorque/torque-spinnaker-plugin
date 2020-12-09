import React from 'react';

import {
  ExecutionDetailsSection,
  ExecutionDetailsTasks,
  FormikFormField,
  FormikStageConfig,
  FormValidator,
  HelpContentsRegistry,
  HelpField,
  IExecutionDetailsSectionProps,
  IStage,
  IStageConfigProps,
  IStageTypeConfig,
  NumberInput,
  TextInput,
  Validators,
} from '@spinnaker/core';

import './ColonyEndSandboxStage.less';

export function ColonyEndSandboxStageConfig(props: IStageConfigProps) {
  return (
    <div className="ColonyEndSandboxStageConfig">
      <FormikStageConfig
        {...props}
        onChange={props.updateStage}
        render={(props) => (
          <>
           <FormikFormField
            name="space"
            label="Colony Space"
            input={(props) => <TextInput {...props} />}
            help={<HelpField id="quali.colonyEndSandboxStage.space"/>}
           />
          <FormikFormField
            name="sandboxId"
            label="Colony Sandbox ID"
            input={(props) => <TextInput {...props} />}
            help={<HelpField id="quali.colonyEndSandboxStage.sandboxId"/>}
          />
          </>
        )}
      />
    </div>
  );
}
