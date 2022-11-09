package com.evan.demo.service;

public interface FileService {

    void upload();

    default void upload_binaryData_fromFile() {
        throw new UnsupportedOperationException();
    }

    default void upload_binaryData_fromInputStream() {
        throw new UnsupportedOperationException();
    }

    default void upload_binaryData_blobClient() {
        throw new UnsupportedOperationException();
    }
    void delete();


}
