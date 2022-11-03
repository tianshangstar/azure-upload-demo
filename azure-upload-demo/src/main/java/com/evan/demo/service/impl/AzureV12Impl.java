package com.evan.demo.service.impl;

import com.azure.core.http.rest.Response;
import com.azure.core.util.Context;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.models.BlockBlobItem;
import com.azure.storage.blob.options.BlockBlobSimpleUploadOptions;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.evan.demo.entity.AzureConfig;
import com.evan.demo.service.AbstractFileService;
import com.microsoft.azure.storage.core.MarkableFileStream;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
public class AzureV12Impl extends AbstractFileService {

    public AzureV12Impl() {
        super();
    }

    @Override
    public void upload() {

        log.info("start upload...");
        AzureConfig azureConfig = getAzureConfig();

        BlockBlobClient blockBlobClient = getBlobContainerClient(azureConfig);

        File file = new File(azureConfig.getFile());

        long contentLength = file.length();

//        try (InputStream inputStream = Files.newInputStream(file.toPath());
//             MarkableFileStream markableFileStream = new MarkableFileStream(new FileInputStream(file));
//             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
        try (MarkableFileStream markableFileStream = new MarkableFileStream(new FileInputStream(file))) {
            BlockBlobSimpleUploadOptions uploadOptions = new BlockBlobSimpleUploadOptions(markableFileStream, contentLength);

            Response<BlockBlobItem> response = blockBlobClient.uploadWithResponse(uploadOptions, null, Context.NONE);

            if (response.getStatusCode() == 201) {
                log.info("upload success");
            } else {
                log.info("upload failed.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete() {
        log.info("start delete...");
        AzureConfig azureConfig = getAzureConfig();

        BlockBlobClient blockBlobClient = getBlobContainerClient(azureConfig);

        blockBlobClient.deleteIfExists();
        log.info("delete success");
    }

    private BlockBlobClient getBlobContainerClient(AzureConfig azureConfig) {
        return new BlobContainerClientBuilder()
                .connectionString(azureConfig.getConnectionString())
                .containerName(azureConfig.getContainerName())
                .buildClient()
                .getBlobClient(azureConfig.getOperateFileId())
                .getBlockBlobClient();
    }

}
