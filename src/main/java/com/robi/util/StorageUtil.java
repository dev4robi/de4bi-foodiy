package com.robi.util;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class StorageUtil {

    private static Logger logger = LoggerFactory.getLogger(StorageUtil.class);

    public static String storeMultipartAsFile(MultipartFile mpFile, String fileDir, String fileName)
            throws IllegalStateException, IOException
    {
        if (mpFile == null) {
            throw new IllegalArgumentException("'mpFile' is null!");
        }

        if (fileDir == null || fileDir.length() == 0) {
            fileDir = "./";
        }

        if (fileName == null || fileName == null) {
            throw new IllegalArgumentException("'fileName' is null or zero-length! (fileName: " + fileName);
        }
     
        final String mpFilePath = (fileDir + "/" + fileName);
        File mpFileDir = new File(fileDir);
        File storedFile = new File(mpFilePath);

        synchronized (StorageUtil.class) {
            if (!mpFileDir.exists()) {
                if (!mpFileDir.mkdirs()) {
                    throw new IOException("Fail to make directory '" + fileDir + "'!");
                }
            }

            if (storedFile.exists()) {
                throw new IOException("File '" + mpFilePath + "'already exist!");
            }

            mpFile.transferTo(storedFile);
        }

        return mpFilePath;
    }
}