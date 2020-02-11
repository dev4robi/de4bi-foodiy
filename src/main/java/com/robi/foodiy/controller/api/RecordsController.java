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
        @ModelAttribute PostRecordsDto postRecordsDto
    ) {
        // @@ postRecordsDto에서 title값을 제외하고 자동 대입이 되지 않는다...
        // 모델 어트리뷰트 말고 다른 방식을 사용하면 멀티파트 전송이 안되는것 같다...
        // 괜찮은 방법은 없을까? 이미지를 base64로 변환해서 전송해버려...?
        // 여기부터 시작...!
        return recordsWithMenusSvc.insertRecordsWithMenus(userJwt, postRecordsDto);
    }
}