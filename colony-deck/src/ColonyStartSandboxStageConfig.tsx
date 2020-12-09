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

import './ColonyStartSandboxStage.less';

export function ColonyStartSandboxStageConfig(props: IStageConfigProps) {
  return (
    <div className="ColonyStartSandboxStageConfig">
      <FormikStageConfig
        {...props}
        validate={validate}
        onChange={props.updateStage}
        render={
          props => (
      <>

            <FormikFormField name="blueprintName" label="Colony Blueprint Name"
              input={props => <TextInput {...props} />}
              help={<HelpField id="quali.colonyStartSandboxStage.blueprintName" />}
            />
            <FormikFormField
              name="sandboxName"
              label="Colony Sandbox Name"
              input={props => <TextInput {...props} />}
              help={<HelpField id="quali.colonyStartSandboxStage.sandboxName" />}
            />
            <FormikFormField
                name="space"
                label="Colony Space Name"
                input={(props) => <TextInput {...props} />}
                help={<HelpField id="quali.colonyStartSandboxStage.space"/>}
            />

             <FormikFormField
                 name="artifacts"
                 label="Artifacts"
                 input={(props) => <TextInput {...props} />}
                 help={<HelpField id="quali.colonyStartSandboxStage.artifacts"/>}
                 required={false}
             />

            <FormikFormField
                name="inputs"
                label="Inputs"
                input={(props) => <TextInput {...props} />}
                help={<HelpField id="quali.colonyStartSandboxStage.inputs"/>}
                required={false}
            />

          <FormikFormField
                  name="timeoutMinutes"
                  label="Timeout"
                  input={(props) => <NumberInput placeholder="20" value = '20' {...props} />}
                  help={<HelpField id="quali.colonyStartSandboxStage.timeout"/>}
          />
              <FormikFormField
                  name="duration"
                  label="Duration"
                  input={(props) => <NumberInput placeholder="1" {...props} />}
                  help={<HelpField id="quali.colonyStartSandboxStage.duration"/>}
              />

      </>
          )}
      />
    </div>
  );
}

export function validate(stageConfig: IStage) {
  const validator = new FormValidator(stageConfig);

  validator
    .field('duration')
    .required()
    .withValidators((value, label) => (value < 0 ? `${label} must be non-negative` : undefined));
  
  validator
    .field('timeoutMinutes')
    .required()
    .withValidators((value, label) => (value < 0 ? `${label} must be non-negative` : undefined));

  return validator.validateForm();
}