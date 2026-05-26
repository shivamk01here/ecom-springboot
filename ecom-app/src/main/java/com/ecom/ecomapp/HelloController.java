package com.ecom.ecomapp;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Validated
public class HelloController {

    private final GreetingService greetingService;

    public HelloController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping("/home")
    public ResponseEntity<MessageResponse> home(@RequestParam(defaultValue = "Shivam") String name) {
        return ResponseEntity.ok(new MessageResponse("Welcome home, " + name + "!"));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponse> user(
            @PathVariable("id") Long id,
            @RequestParam(defaultValue = "Shivam") String name) {
        return ResponseEntity.ok(new UserResponse(id, name, "This user is " + name + "."));
    }

    @GetMapping("/jungle")
    public ResponseEntity<MessageResponse> jungle() {
        return ResponseEntity.ok(new MessageResponse("Welcome to the jungle, Shivam!"));
    }

    @GetMapping("/goodbye")
    public ResponseEntity<MessageResponse> goodbye() {
        return ResponseEntity.ok()
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(new MessageResponse("Goodbye, Shivam! See you soon."));
    }

    @GetMapping("/status")
    public ResponseEntity<StatusResponse> status() {
        return ResponseEntity.ok(new StatusResponse("Server is up and running.", "OK"));
    }

    @PostMapping("/hello")
    public ResponseEntity<MessageResponse> createHello(@Valid @RequestBody HelloRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponse(greetingService.greet(request.name())));
    }

    @PostMapping("/hello/reverse")
    public ResponseEntity<MessageResponse> reverseHello(@Valid @RequestBody HelloRequest request) {
        return ResponseEntity.ok(new MessageResponse(greetingService.reverseName(request.name())));
    }

    public static record HelloRequest(@NotBlank String name) {
    }

    public static record MessageResponse(String message) {
    }

    public static record UserResponse(Long id, String name, String details) {
    }

    public static record StatusResponse(String status, String code) {
    }
}
