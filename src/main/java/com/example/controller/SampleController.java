package com.example.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sample")
@EnableAutoConfiguration
public class SampleController {

    @RequestMapping("/home")
    @ResponseBody
    String home() {
        return "Hello World! Man";
    }

    public static void main(String[] args) throws Exception {
        //SpringApplication.run(SampleController.class, args);
        SpringApplication.run(AlarmController.class, args);
    }
}
