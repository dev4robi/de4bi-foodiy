package com.robi.foodiy.controller.page;

import com.robi.foodiy.service.TestService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class TestPageController {

    private static final Logger logger = LoggerFactory.getLogger(TestPageController.class);

    @GetMapping("/test")
    public ModelAndView upload() {
        return new ModelAndView("test");
    }
}