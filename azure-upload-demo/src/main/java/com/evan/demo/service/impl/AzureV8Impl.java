package com.evan.demo.service.impl;

import com.evan.demo.entity.AzureConfig;
import com.evan.demo.service.AbstractFileService;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.security.InvalidKeyException;

@Slf4j
public class AzureV8Impl extends AbstractFileService {

    public AzureV8Impl() {
        super();
    }

    @Override
    public void upload() {
        log.info("start upload...");

        AzureConfig azureConfig = getAzureConfig();
        CloudBlockBlob blob;
        try {
            blob = getCloudBlockBlob(azureConfig);

            File file = new File(azureConfig.getFile());

            long contentLength = file.length();

            try (InputStream inputStream = Files.newInputStream(file.toPath());
                 BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
                blob.upload(bufferedInputStream, contentLength, null /* accessCondition */,
                        null /* options */, null);
            } catch (IOException e) {
                e.printStackTrace();
            }

            log.info("upload success.");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete() {

        log.info("start delete...");
        AzureConfig azureConfig = getAzureConfig();
        CloudBlockBlob blob;
        try {
            blob = getCloudBlockBlob(azureConfig);

            blob.deleteIfExists();
            log.info("delete success.");
        } catch (URISyntaxException | InvalidKeyException | StorageException e) {
            throw new RuntimeException(e);
        }
    }

    private CloudBlockBlob getCloudBlockBlob(AzureConfig azureConfig) throws URISyntaxException, InvalidKeyException, StorageException {

        CloudStorageAccount account = CloudStorageAccount.parse(azureConfig.getConnectionString());

        CloudBlobClient serviceClient = account.createCloudBlobClient();

        return serviceClient.getContainerReference(azureConfig.getContainerName()).getBlockBlobReference(azureConfig.getOperateFileId());
    }
}
