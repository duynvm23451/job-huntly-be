package com.phenikaa.jobhuntly.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(Authentication authentication)
    {
        System.out.println(authentication.getName());
        return "Hello World";
    }
}
