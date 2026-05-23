package com.ecom.ecomapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/home")
    public String home() {
        return "welcome home, shivam!";
    }


    @GetMapping("/user")
    public String user() {
        return "this user is, shivam!";
    }


    @GetMapping("/jungle")
    public String jungle() {
        return "Welcome to the jungle, shivam!";
    }

}
