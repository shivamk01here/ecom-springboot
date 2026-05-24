package com.ecom.ecomapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/goodbye")
    public String goodbye() {
        return "Goodbye, shivam! See you soon.";
    }

    @PostMapping("/hello")
    public String createHello(@RequestBody HelloRequest request) {
        return "Hello, " + request.name() + "!";
    }

    public static record HelloRequest(String name) {
    }

}
