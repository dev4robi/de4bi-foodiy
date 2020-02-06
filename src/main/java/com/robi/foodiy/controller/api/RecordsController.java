package com.robi.foodiy.controller.api;

import com.robi.data.ApiResult;
import com.robi.foodiy.data.dto.PostRecordsDto;
import com.robi.foodiy.service.RecordsWithMenusService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class RecordsController {

    private static final Logger logger = LoggerFactory.getLogger(RecordsController.class);

    RecordsWithMenusService recordsWithMenusSvc;

    @GetMapping("/records/{id}")
    public ApiResult getRecords(
        @RequestHeader("user_jwt") String userJwt,
        @PathVariable("id") long id
    ) {
        return recordsWithMenusSvc.selectRecordById(userJwt, id);
    }

    @PostMapping("/records")
    public ApiResult postRecordsWithMenus(
        @RequestHeader("user_jwt") String userJwt,
        @ModelAttribute PostRecordsDto postRecordDto
    ) {
        //return recordsWithMenusSvc.insert
        return null;
    }
}