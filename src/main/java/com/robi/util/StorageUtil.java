package com.robi.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tomcat.util.http.fileupload.IOUtils;
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

    public static byte[] loadFileAsByteAry(String fileDir, String fileName)
        throws IllegalArgumentException, IOException
    {
        if (fileDir == null || fileDir.length() == 0) {
            fileDir = "./";
        }

        if (fileName == null || fileName.length() == 0) {
            throw new IllegalArgumentException("'fileName' is null or zero length!");
        }

        String filePath = fileDir + "/" + fileName;
        File loadFile = new File(filePath);

        if (!loadFile.exists()) {
            throw new IOException("File '" + filePath + "' does not exist!");
        }

        if (!loadFile.isFile()) {
            throw new IOException("File '" + filePath + "' is not a file!");
        }

        if (!loadFile.canRead()) {
            throw new IOException("Can't read the file '" + filePath + "'. Check permission!");
        }

        long fileLenLong = loadFile.length();

        if (fileLenLong <= 0L) {
            throw new IOException("File '" + filePath + "' size is under zero!");
        }
        else if (fileLenLong > (long) Integer.MAX_VALUE) {
            throw new IOException("File '" + filePath + "' size is too large! (MaxByteAllowed: " +
                                    Integer.MAX_VALUE + "FileSize: " + fileLenLong + ")");
        }

        int fileLen = (int) fileLenLong;
        byte[] rtByteAry = new byte[fileLen];
        InputStream is = new FileInputStream(loadFile);
        
        IOUtils.read(is, rtByteAry, 0, fileLen);
        is.close();
        
        return rtByteAry;
    }
}