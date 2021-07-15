import React from 'react';

import {
  FormikFormField,
  FormikStageConfig,
  HelpField,
  IStageConfigProps,
  TextInput,
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
            label="Space Name"
            input={(props) => <TextInput {...props} />}
            help={<HelpField id="quali.colonyEndSandboxStage.space"/>}
           />
          <FormikFormField
            name="sandboxId"
            label="Sandbox ID"
            input={(props) => <TextInput {...props} />}
            help={<HelpField id="quali.colonyEndSandboxStage.sandboxId"/>}
          />
          <FormikFormField
              name="token"
              label="Token"
              input={(props) => <TextInput {...props} />}
              help={<HelpField id="quali.colonyStartSandboxStage.token"/>}
          />
          </>
        )}
      />
    </div>
  );
}
