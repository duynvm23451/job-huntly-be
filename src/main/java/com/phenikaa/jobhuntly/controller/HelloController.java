package com.phenikaa.jobhuntly.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(@RequestParam List<String> categories, Authentication authentication)
    {
        System.out.println(categories);
        System.out.println(authentication.getName());
        return "Hello World";
    }
}
