package com.evan.demo;

import com.evan.demo.service.FileService;
import com.evan.demo.service.impl.AzureV12Impl;
import com.evan.demo.service.impl.AzureV8Impl;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Scanner;

@Slf4j
public class Test {

    private static final FileService v12 = new AzureV12Impl();

    private static final FileService v8 = new AzureV8Impl();

    public static void main(String[] args) {

//        new DirectMemoryPrinter().start();

        Scanner scanner = new Scanner(System.in);

        int input = 0;
        printTips();
        while (input != 9 && scanner.hasNext()) {
            input = scanner.nextInt();
            switch (input) {
                case 1:
                    System.out.println("v8");
                    v8Upload();
                    printTips();
                    break;
                case 2:
                    System.out.println("v12");
                    v12Upload_inputStream();
                    printTips();
                    break;
                case 3:
                    System.out.println("v12 binaryData from File");
                    v12Upload_binaryData_fromFile();
                    printTips();
                    break;
                case 4:
                    System.out.println("v12 binaryData from InputStream");
                    v12Upload_binaryData_fromInputStream();
                    printTips();
                    break;
                case 5:
                    System.out.println("v12 binaryData with blobClient");
                    v12Upload_binaryData_withBlobClient();
                    printTips();
                    break;
                case 9:
                    System.out.println("stop.");
                    break;
                default:
                    System.out.println("invalid input. pls try again");
                    printTips();
                    break;
            }
        }
    }

    private static void printTips() {
        System.out.println("Please choose which version Azure sdk you wanna use for test upload:");
        System.out.println("1 - V8");
        System.out.println("2 - V12");
        System.out.println("3 - V12 upload binaryData from file");
        System.out.println("4 - V12 upload binaryData from inputStream");
        System.out.println("5 - V12 upload binaryData with blobClient");
        System.out.println("9 - exit.");
    }

    private static void v8Upload() {
        log.info("V8 upload start at : {}", new Date());
        try {
            v8.upload();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        log.info("V8 upload finished at : {}", new Date());
    }

    private static void v12Upload_inputStream() {
        log.info("v12Upload_inputStream start at : {}", new Date());
        try {
            v12.upload();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        log.info("v12Upload_inputStream finished at : {}", new Date());
    }

    private static void v12Upload_binaryData_fromFile() {
        log.info("v12Upload_binaryData_fromFile start at : {}", new Date());
        try {
            v12.upload_binaryData_fromFile();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        log.info("v12Upload_binaryData_fromFile finished at : {}", new Date());
    }

    private static void v12Upload_binaryData_fromInputStream() {
        log.info("v12Upload_binaryData_fromInputStream start at : {}", new Date());
        try {
            v12.upload_binaryData_fromInputStream();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        log.info("v12Upload_binaryData_fromInputStream finished at : {}", new Date());
    }

    private static void v12Upload_binaryData_withBlobClient(){
        log.info("v12Upload_binaryData_withBlobClient start at : {}", new Date());
        try {
            v12.upload_binaryData_blobClient();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        log.info("v12Upload_binaryData_withBlobClient finished at : {}", new Date());
    }

    /**
     * try print the information about DirectMemory.
     */
    @Slf4j
    static class DirectMemoryPrinter extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                Class<?> clazz = Class.forName("java.nio.Bits");
                Field maxMemory = getAccessibleField(clazz, "maxMemory");
                Field reservedMemory = getAccessibleField(clazz, "reservedMemory");
                Field totalCapacity = getAccessibleField(clazz, "totalCapacity");
                Field count = getAccessibleField(clazz, "count");

                while (true) {
                    sleep(1000);
                    log.info(">>>direct memory usage<<< max:{} reserved:{} totalCapacity:{} count:{}", maxMemory.get(null), reservedMemory.get(null), totalCapacity.get(null), count.get(null));
                }
            } catch (ClassNotFoundException | NoSuchFieldException | InterruptedException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        private Field getAccessibleField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        }
    }

}
