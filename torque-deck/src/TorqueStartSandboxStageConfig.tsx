import React from 'react';

import {
  FormikFormField,
  FormikStageConfig,
  FormValidator,
  HelpField,
  IStage,
  IStageConfigProps,
  NumberInput,
  TextInput,
} from '@spinnaker/core';

import './TorqueStartSandboxStage.less';

export function TorqueStartSandboxStageConfig(props: IStageConfigProps) {
  return (
    <div className="TorqueStartSandboxStageConfig">
      <FormikStageConfig
        {...props}
        validate={validate}
        onChange={props.updateStage}
        render={
          props => (
      <>

            <FormikFormField name="blueprintName" label="Blueprint Name"
              input={props => <TextInput {...props} />}
              help={<HelpField id="quali.torqueStartSandboxStage.blueprintName" />}
            />
            <FormikFormField
              name="sandboxName"
              label="Sandbox Name"
              input={props => <TextInput {...props} />}
              help={<HelpField id="quali.torqueStartSandboxStage.sandboxName" />}
            />
            <FormikFormField
                name="space"
                label="Space Name"
                input={(props) => <TextInput {...props} />}
                help={<HelpField id="quali.torqueStartSandboxStage.space"/>}
            />

             <FormikFormField
                 name="artifacts"
                 label="Artifacts"
                 input={(props) => <TextInput {...props} />}
                 help={<HelpField id="quali.torqueStartSandboxStage.artifacts"/>}
                 required={false}
             />

            <FormikFormField
                name="inputs"
                label="Inputs"
                input={(props) => <TextInput {...props} />}
                help={<HelpField id="quali.torqueStartSandboxStage.inputs"/>}
                required={false}
            />

          <FormikFormField
                  name="timeoutMinutes"
                  label="Timeout (minutes)"
                  input={(props) => <NumberInput placeholder="20" value = '20' {...props} />}
                  help={<HelpField id="quali.torqueStartSandboxStage.timeout"/>}
          />
          <FormikFormField
              name="duration"
              label="Duration (minutes)"
              input={(props) => <NumberInput placeholder="30" {...props} />}
              help={<HelpField id="quali.torqueStartSandboxStage.duration"/>}
          />
          <FormikFormField
              name="token"
              label="Token"
              input={(props) => <TextInput type="password" {...props} />}
              help={<HelpField id="quali.torqueStartSandboxStage.token"/>}
              required={false}
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
    .withValidators((value, label) => (value <= 0 ? `${label} must be positive` : undefined));
  
  validator
    .field('timeoutMinutes')
    .required()
    .withValidators((value, label) => (value <= 0 ? `${label} must be positive` : undefined));

  return validator.validateForm();
}