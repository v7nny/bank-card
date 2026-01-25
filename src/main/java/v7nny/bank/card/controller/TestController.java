package v7nny.bank.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import v7nny.bank.card.service.AuthService;
import java.util.Map;

@RestController
public class TestController {

    private final AuthService authService;

    @Autowired
    public TestController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok().body(Map.of("message", "lololo"));
    }
}