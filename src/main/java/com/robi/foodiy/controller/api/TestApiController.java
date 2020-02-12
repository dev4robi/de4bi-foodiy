package com.robi.foodiy.controller.api;

import java.util.Arrays;

import com.robi.data.ApiResult;
import com.robi.foodiy.data.dto.TestDto;
import com.robi.foodiy.service.TestService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class TestApiController {

    private static final Logger logger = LoggerFactory.getLogger(TestApiController.class);

    private TestService testSvc;

    @GetMapping("/index")
    public ApiResult index() {
        return testSvc.index();
    }

    @PostMapping("/upload")
    public ApiResult upload(
        @RequestPart("name") String name,
        @RequestPart("sub_name") String subName,
        @RequestPart("pic") MultipartFile pic1,
        @RequestPart("sub_pics") MultipartFile pic2
    ) {
        //logger.info("sub_pic: " + testDto.getSubPics());
        return ApiResult.make(true);
    }
}