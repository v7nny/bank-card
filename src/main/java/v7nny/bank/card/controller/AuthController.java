package v7nny.bank.card.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import v7nny.bank.card.dto.SignInDTO;
import v7nny.bank.card.dto.SignUpDTO;
import v7nny.bank.card.exception.EmailAlreadyTakenException;
import v7nny.bank.card.exception.UsernameAlreadyTakenException;
import v7nny.bank.card.service.AuthService;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpDTO signUpDTO, HttpServletResponse response) {
        try {
            var cookie = authService.signUp(signUpDTO);

            response.addCookie(cookie);
            return ResponseEntity.status(201).body(Map.of("username", signUpDTO.username()));
        } catch (EmailAlreadyTakenException | UsernameAlreadyTakenException e) {
            return ResponseEntity.status(409).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInDTO signInDTO, HttpServletResponse response) {
        try {
            var cookie = authService.signIn(signInDTO);

            response.addCookie(cookie);
            return ResponseEntity.status(200).body(Map.of("token", cookie.getValue()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Map.of("message", e.getMessage()));
        }
    }
}
