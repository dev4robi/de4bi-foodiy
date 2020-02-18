package com.robi.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MdUtil {

    private static final Logger logger = LoggerFactory.getLogger(MdUtil.class);

    public static byte[] md(byte[] originData, String algorithms) {
        if (originData == null) {
            logger.error("'originData' is null!");
            return null;
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance(algorithms);
            md.update(originData);
            return md.digest();
        }
        catch (NoSuchAlgorithmException e) {
            logger.error("Exception!", e);
            return null;
        }
    }
    
    public static byte[] sha128(byte[] originData) {
        if (originData == null) {
            logger.error("'originData' is null!");
            return null;
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(originData);
            return md.digest();
        }
        catch (NoSuchAlgorithmException e) {
            logger.error("Exception!", e);
            return null;
        }
    }

    public static byte[] sha256(byte[] originData) {
        if (originData == null) {
            logger.error("'originData' is null!");
            return null;
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(originData);
            return md.digest();
        }
        catch (NoSuchAlgorithmException e) {
            logger.error("Exception!", e);
            return null;
        }
    }

    public static byte[] sha512(byte[] originData) {
        if (originData == null) {
            logger.error("'originData' is null!");
            return null;
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(originData);
            return md.digest();
        }
        catch (NoSuchAlgorithmException e) {
            logger.error("Exception!", e);
            return null;
        }
    }

    public static byte[] md5(byte[] originData) {
        if (originData == null) {
            logger.error("'originData' is null!");
            return null;
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(originData);
            return md.digest();
        }
        catch (NoSuchAlgorithmException e) {
            logger.error("Exception!", e);
            return null;
        }
    }
}