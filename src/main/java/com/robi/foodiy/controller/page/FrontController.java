package com.robi.foodiy.controller.page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class FrontController {

    private static final Logger logger = LoggerFactory.getLogger(FrontController.class);

    @RequestMapping("/page/main")
    public ModelAndView mainPage() {
        return new ModelAndView("main");
    }

    @RequestMapping("/page/record")
    public ModelAndView recordPage() {
        return new ModelAndView("diary_record");
    }

    @RequestMapping("/page/search")
    public ModelAndView searchPage() {
        return new ModelAndView("diary_search");
    }

    @RequestMapping("/page/map")
    public ModelAndView mapPage() {
        return new ModelAndView("diary_map");
    }

    @RequestMapping("/page/stat")
    public ModelAndView statPage() {
        return new ModelAndView("diary_stat");
    }
}