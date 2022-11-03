package com.evan.demo.entity;

import lombok.Data;

@Data
public class AzureConfig {

    String accountName;

    String containerUri;

    String containerName;

    String file;

    String fileFolder;

    String sasToken;

    public String getOperateFileId() {
        return fileFolder + "/" + file;
    }

    public String getConnectionString() {
        String blobEndpoint = "BlobEndpoint=" + containerUri.replace(containerName, "");
        String sharedAccessSignature = "SharedAccessSignature=" + sasToken;
        return String.join(";", blobEndpoint, sharedAccessSignature);
    }
}
