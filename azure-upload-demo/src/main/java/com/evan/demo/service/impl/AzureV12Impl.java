package com.evan.demo.service.impl;

import com.azure.core.http.rest.Response;
import com.azure.core.util.BinaryData;
import com.azure.core.util.Context;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.models.BlockBlobItem;
import com.azure.storage.blob.options.BlobParallelUploadOptions;
import com.azure.storage.blob.options.BlockBlobSimpleUploadOptions;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.evan.demo.entity.AzureConfig;
import com.evan.demo.service.AbstractFileService;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@Slf4j
public class AzureV12Impl extends AbstractFileService {

    public AzureV12Impl() {
        super();
    }

    @Override
    public void upload() {

        log.info("start upload...");
        AzureConfig azureConfig = getAzureConfig();

        BlockBlobClient blockBlobClient = getBlockBlobClient(azureConfig);

        File file = new File(azureConfig.getFile());

        long contentLength = file.length();

        try (InputStream inputStream = Files.newInputStream(file.toPath());
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)
        ) {
//        try (MarkableFileStream markableFileStream = new MarkableFileStream(new FileInputStream(file))) {

            // old
            BlockBlobSimpleUploadOptions uploadOptions = new BlockBlobSimpleUploadOptions(bufferedInputStream, contentLength);

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
    public void upload_binaryData_fromFile() {
        log.info("start upload_binaryData_fromFile...");
        AzureConfig azureConfig = getAzureConfig();

        BlockBlobClient blockBlobClient = getBlockBlobClient(azureConfig);

        File file = new File(azureConfig.getFile());

        BinaryData binaryData = BinaryData.fromFile(file.toPath());

        BlockBlobSimpleUploadOptions uploadOptions = new BlockBlobSimpleUploadOptions(binaryData);

        Response<BlockBlobItem> response = blockBlobClient.uploadWithResponse(uploadOptions, null, Context.NONE);

        if (response.getStatusCode() == 201) {
            log.info("upload_binaryData_fromFile success");
        } else {
            log.info("upload_binaryData_fromFile failed.");
        }
    }

    @Override
    public void upload_binaryData_fromInputStream() {

        log.info("start upload_binaryData_fromInputStream...");
        AzureConfig azureConfig = getAzureConfig();

        BlockBlobClient blockBlobClient = getBlockBlobClient(azureConfig);

        File file = new File(azureConfig.getFile());

        long contentLength = file.length();

        try (InputStream inputStream = Files.newInputStream(file.toPath());
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {

            BinaryData binaryData = BinaryData.fromStream(inputStream, contentLength);

            BlockBlobSimpleUploadOptions uploadOptions = new BlockBlobSimpleUploadOptions(binaryData);

            Response<BlockBlobItem> response = blockBlobClient.uploadWithResponse(uploadOptions, null, Context.NONE);

            if (response.getStatusCode() == 201) {
                log.info("upload_binaryData_fromInputStream success");
            } else {
                log.info("upload_binaryData_fromInputStream failed.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void upload_binaryData_blobClient(){
        log.info("start upload_binaryData_blobClient...");

        AzureConfig azureConfig = getAzureConfig();

        BlobClient blobClient = getBlobClient(azureConfig);

        File file = new File(azureConfig.getFile());

//        long contentLength = file.length();

        try (InputStream inputStream = Files.newInputStream(file.toPath());
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {

//            BinaryData binaryData = BinaryData.fromFile(file.toPath());
            BinaryData binaryData = BinaryData.fromStream(inputStream);

            BlobParallelUploadOptions parallelUploadOptions = new BlobParallelUploadOptions(binaryData);

            Response<BlockBlobItem> response = blobClient.uploadWithResponse(parallelUploadOptions, null, Context.NONE);

            if (response.getStatusCode() == 201) {
                log.info("upload_binaryData_fromInputStream success");
            } else {
                log.info("upload_binaryData_fromInputStream failed.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete() {
        log.info("start delete...");
        AzureConfig azureConfig = getAzureConfig();

        BlockBlobClient blockBlobClient = getBlockBlobClient(azureConfig);

        blockBlobClient.deleteIfExists();
        log.info("delete success");
    }

    private BlockBlobClient getBlockBlobClient(AzureConfig azureConfig) {
        return getBlobClient(azureConfig)
                .getBlockBlobClient();
    }

    private BlobClient getBlobClient(AzureConfig azureConfig) {
        return new BlobContainerClientBuilder()
                .connectionString(azureConfig.getConnectionString())
                .containerName(azureConfig.getContainerName())
                .buildClient()
                .getBlobClient(azureConfig.getOperateFileId());
    }

}
