import React from 'react';

import {
    FormikFormField,
    FormikStageConfig,
    HelpField,
    IStageConfigProps,
    TextInput,
} from '@spinnaker/core';

import './TorqueEndSandboxStage.less';

export function TorqueEndSandboxStageConfig(props: IStageConfigProps) {
  return (
    <div className="TorqueEndSandboxStageConfig">
      <FormikStageConfig
        {...props}
        onChange={props.updateStage}
        render={(props) => (
          <>
           <FormikFormField
            name="space"
            label="Space Name"
            input={(props) => <TextInput {...props} />}
            help={<HelpField id="quali.torqueEndSandboxStage.space"/>}
           />
          <FormikFormField
            name="sandboxId"
            label="Sandbox ID"
            input={(props) => <TextInput {...props} />}
            help={<HelpField id="quali.torqueEndSandboxStage.sandboxId"/>}
          />
          <FormikFormField
              name="token"
              label="Token"
              input={(props) => <TextInput type="password" {...props} />}
              help={<HelpField id="quali.torqueStartSandboxStage.token"/>}
          />
          </>
        )}
      />
    </div>
  );
}
