package com.robi.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

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
                logger.warn("File '{}' already exist! Origin file will be overwritten.", mpFilePath);
            }
        }

        mpFile.transferTo(storedFile);

        // 이미지파일의 경우 크기 조정
        try {
            int extIdx = fileName.lastIndexOf(".");
            
            if (extIdx != -1) {
                String fileExt = fileName.substring(extIdx + 1);
                BufferedImage img = ImageIO.read(storedFile);

                if (img != null) {
                    int imgWidth = img.getWidth();
                    int imgHeight = img.getHeight();
                    int imgNewWidth = Math.min(imgWidth, 800);
                    float imgNewRatio = (imgWidth / (float) imgNewWidth);
                    int imgNewHeight = Math.min((int) (imgHeight * imgNewRatio), 600);

                    // Image.SCALE_DEFAULT : 기본 이미지 스케일링 알고리즘 사용
                    // Image.SCALE_FAST    : 이미지 부드러움보다 속도 우선
                    // Image.SCALE_REPLICATE : ReplicateScaleFilter 클래스로 구체화 된 이미지 크기 조절 알고리즘
                    // Image.SCALE_SMOOTH  : 속도보다 이미지 부드러움을 우선
                    // Image.SCALE_AREA_AVERAGING  : 평균 알고리즘 사용
                    Image resizedImg = img.getScaledInstance(imgNewWidth, imgNewHeight, Image.SCALE_SMOOTH);
                    img = new BufferedImage(imgNewWidth, imgNewHeight, BufferedImage.TYPE_INT_RGB);
                    Graphics g = img.getGraphics();
                    g.drawImage(resizedImg, 0, 0, null);
                    g.dispose();
                    ImageIO.write(img, fileExt, storedFile);
                }
            }
        }
        catch (Exception e) {
            logger.error("Exception while ImageIO!", e);
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