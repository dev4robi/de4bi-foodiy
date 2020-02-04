package com.robi.foodiy.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PostMenusDto {
    
    @JsonProperty("name")
    String name;

    @JsonProperty("price")
    String price;

    @JsonProperty("tags")
    String tags;

    @JsonProperty("score")
    String socre;
}