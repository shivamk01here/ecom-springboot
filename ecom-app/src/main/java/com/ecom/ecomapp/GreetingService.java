package com.ecom.ecomapp;

import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    public String greet(String name) {
        return "Hello, " + name + "!";
    }

    public String reverseName(String name) {
        return "Hello, " + new StringBuilder(name).reverse().toString() + "!";
    }
}
