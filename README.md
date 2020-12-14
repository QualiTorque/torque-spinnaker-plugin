![quali](quali.png)

## CloudShell Colony Plugin for Spinnaker

This plugin provides support for CloudShell Colony via Pipelines in Spinnaker 1.22.x and below

For Spinnaker 1.23.0 and above please use this [branch](https://github.com/QualiSystemsLab/colony-spinnaker-plugin) 

### Version Compatibility
| Plugin  | Spinnaker Platform |
|:----------- | :--------- |
| 1.22.x Branch |  1.22.x |



## Plugin Deployment Guide for Spinnaker >=1.22.x 

1. Add the following to the Halyard config (typically found at `~/.hal/config`) to load the Orca backend
```yaml
  spinnaker:
    extensibility:
      plugins:
        Quali.ColonySandboxPlugin:
          id: Quali.ColonySandboxPlugin
          enabled: true
          version: <<VERSION NUMBER>>
          extensions:
            quali.colonySandboxStage:
              enabled: true
              config:
                colonyToken: <<API Token (Required) >>
                colonyUrl:  <<API URL (Not required) Default= https://cloudshellcolony.com >>
                account: <<Colony Sub Domain (Not required) EX= colony-demo >>
      repositories:
        ColonyRepo:
          id: S3Repo
          url: <<Plugin.json location>>
```
2. Add the following to `gate-local.yml` in the necessary [profile](https://spinnaker.io/reference/halyard/custom/#custom-profiles) to load the Deck frontend
```yaml
spinnaker:
 extensibility:
    deck-proxy:
      enabled: true
      plugins:
        Quali.ColonySandboxPlugin:
          enabled: true
          version: <<VERSION NUMBER>>
    repositories:
      ColonyRepo:
        url: <<Plugin.json location>>
```
3. Execute `hal deploy apply` to deploy the changes.
4. You should now be able to see 2 new stages provided by this plugin (Colony Start and End Sandbox) in the Deck UI when adding a new stage to your pipeline.

## License
[Apache License 2.0](https://github.com/QualiSystems/shellfoundry/blob/master/LICENSE)
