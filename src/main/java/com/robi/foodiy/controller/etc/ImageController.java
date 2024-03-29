package com.robi.foodiy.controller.etc;

import java.io.IOException;

import com.robi.util.StorageUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/etc")
@RequiredArgsConstructor
public class ImageController {

    private static Logger logger = LoggerFactory.getLogger(ImageController.class);

    private final Environment env;
    
    @GetMapping(value="/img/{name}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable("name") String name) {
        byte[] rtByte = null;
        
        try {
            String fileDir = env.getProperty("foodiy.records.img.basedir");
            rtByte = StorageUtil.loadFileAsByteAry(fileDir, name);
        }
        catch (IllegalArgumentException | IOException e) {
            logger.error("Exception!", e);
        }

        return rtByte;
    }
}