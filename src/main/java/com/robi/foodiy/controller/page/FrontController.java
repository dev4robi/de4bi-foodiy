package com.robi.foodiy.controller.page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/page")
public class FrontController {

    private static final Logger logger = LoggerFactory.getLogger(FrontController.class);

    @RequestMapping("/main")
    public ModelAndView mainPage() {
        return new ModelAndView("main");
    }

    @RequestMapping("/record")
    public ModelAndView recordPage() {
        return new ModelAndView("diary_record");
    }

    @RequestMapping("/search")
    public ModelAndView searchPage() {
        return new ModelAndView("diary_search");
    }

    @RequestMapping("/map")
    public ModelAndView mapPage() {
        return new ModelAndView("diary_map");
    }

    @RequestMapping("/stat")
    public ModelAndView statPage() {
        return new ModelAndView("diary_stat");
    }
}