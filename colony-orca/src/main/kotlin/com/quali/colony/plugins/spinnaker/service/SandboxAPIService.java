package com.quali.colony.plugins.spinnaker.service;

import com.quali.colony.plugins.spinnaker.api.CreateSandboxRequest;
import com.quali.colony.plugins.spinnaker.api.CreateSandboxResponse;
import com.quali.colony.plugins.spinnaker.api.ResponseData;

import java.io.IOException;

public interface SandboxAPIService
{
    ResponseData<CreateSandboxResponse> createSandbox(String spaceName, final CreateSandboxRequest req) throws IOException;
    ResponseData<Void> deleteSandbox(String spaceName, String sandboxId) throws IOException;
    ResponseData<Object> getSandboxById(String spaceName, String sandboxId) throws IOException;
}
