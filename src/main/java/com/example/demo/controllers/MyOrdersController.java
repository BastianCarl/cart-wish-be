package com.example.demo.controllers;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyOrdersController {

    @GetMapping("/api/myorders")
    public String myOrders() {
        return "Hello World";
    }
}
