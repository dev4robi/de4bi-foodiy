package com.robi.foodiy.controller.api;

import com.robi.data.ApiResult;
import com.robi.foodiy.data.dto.PostRecordsDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class RecordsController {

    private static final Logger logger = LoggerFactory.getLogger(RecordsController.class);

    @GetMapping("/records/{id}")
    public ApiResult getRecords(
        @PathVariable("id") String id,
        @RequestHeader("user_jwt") String userJwt
    ) {
        logger.info("id:" + id);
        logger.info("userJwt:" + userJwt);
        return ApiResult.make(true);
    }

    @PostMapping("/records")
    public ApiResult postRecords(
        @RequestHeader("user_jwt") String userJwt,
        @ModelAttribute PostRecordsDto postRecordDto
    ) {
        logger.info("user_jwt:" + userJwt);
        logger.info("post_record_dto:" + postRecordDto.toString()); // @여기부터 시작...
        return ApiResult.make(true);
    }
}