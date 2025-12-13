package com.boot.backend.Sweet.Shop.Management.System.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/home")
public class HomeController {

    @GetMapping("/sayHello")
    public String sayHello(){
        return "Hello World";
    }
}
