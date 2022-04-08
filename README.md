[![Build](https://github.com/QualiTorque/torque-spinnaker-plugin/actions/workflows/build.yml/badge.svg)](https://github.com/QualiTorque/torque-spinnaker-plugin/actions/workflows/build.yml)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

![quali](quali.png)

## Torque Plugin for Spinnaker

This plugin provides support for Torque via Pipelines in Spinnaker 1.23.0 and above.

For Spinnaker 1.22.x and below please use this [branch](https://github.com/QualiSystemsLab/torque-spinnaker-plugin/tree/1.22.x). 

### Version Compatibility
| Plugin  | Spinnaker Platform |
|:----------- | :--------- |
| Master Branch |  1.23.x |



## Plugin Deployment Guide for Spinnaker <=1.23.x 

1. Add the following to the Halyard config (typically found at `~/.hal/config`) to load the Orca backend. 
```yaml
  spinnaker:
    extensibility:
      plugins:
        Quali.TorqueSandboxPlugin:
          id: Quali.TorqueSandboxPlugin
          enabled: true
          version: <<VERSION NUMBER>>
          config:
              torqueToken: <<API Token (Not Required) >>
              torqueUrl:  <<API URL (Not required) Default= https://qtorque.io >>
              account: <<Torque Sub Domain (Not required) EX= demo >>
      repositories:
        TorqueRepo:
          id: S3Repo
          url: <<Plugin.json location>>
```
>Note: torqueToken is the authentication token used by default but is no longer required. You can specify the Torque token used by each stage in the Spinnaker pipeline.

2. Add the following to `gate-local.yml` in the necessary [profile](https://spinnaker.io/reference/halyard/custom/#custom-profiles) to load the Deck frontend
```yaml
spinnaker:
 extensibility:
    deck-proxy:
      enabled: true
      plugins:
        Quali.TorqueSandboxPlugin:
          enabled: true
          version: <<VERSION NUMBER>>
    repositories:
      TorqueRepo:
        url: <<Plugin.json location>>
```
3. Execute `hal deploy apply` to deploy the changes.
4. You should now be able to see 2 new stages provided by this plugin (Torque Start and End Sandbox) in the Deck UI when adding a new stage to your pipeline.

## License
[Apache License 2.0](https://github.com/QualiSystems/shellfoundry/blob/master/LICENSE)
