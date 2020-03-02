package com.robi.foodiy.data.dao;

import lombok.Data;

@Data
public class MenusDao {

    private long id;            // 고유 ID
    private long writeUserId;   // 메뉴 작성자 회원ID
    private long recordId;      // 게시글 ID
    private String name;        // 메뉴명
    private String picUrl;      // 사진URL
    private int price;          // 가격
    private String tags;        // 태그들
    private int score;          // 평점
}