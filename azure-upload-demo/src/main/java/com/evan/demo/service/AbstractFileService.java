package com.evan.demo.service;

import com.evan.demo.entity.AzureConfig;
import com.evan.demo.service.FileService;
import lombok.SneakyThrows;

import java.io.FileReader;
import java.util.Objects;
import java.util.Properties;

public abstract class AbstractFileService implements FileService {

    private static final String ACCOUNT_NAME = "account.name";

    private static final String CONTAINER_NAME = "container.name";

    private static final String CONTAINER_URI = "container.uri";

    private static final String FILE = "file";

    private static final String FILE_FOLDER = "file.folder";

    private static final String SAS_TOKEN = "sas.token";


    private final AzureConfig azureConfig;

    @SneakyThrows
    public AbstractFileService(){
        FileReader fileReader = new FileReader(Objects.requireNonNull(getClass().getResource("/application.properties")).getFile());

        Properties properties = new Properties();
        properties.load(fileReader);

        azureConfig = new AzureConfig();
        azureConfig.setAccountName(properties.getProperty(ACCOUNT_NAME));
        azureConfig.setContainerUri(properties.getProperty(CONTAINER_URI));
        azureConfig.setContainerName(properties.getProperty(CONTAINER_NAME));
        azureConfig.setFile(properties.getProperty(FILE));
        azureConfig.setFileFolder(properties.getProperty(FILE_FOLDER));
        azureConfig.setSasToken(properties.getProperty(SAS_TOKEN));
    }

    protected AzureConfig getAzureConfig() {
        return azureConfig;
    }
}
