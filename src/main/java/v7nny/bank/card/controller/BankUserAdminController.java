package v7nny.bank.card.controller;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import v7nny.bank.card.exception.UserNotFoundException;
import v7nny.bank.card.service.BankCardUserService;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class BankUserAdminController {

    private final BankCardUserService userService;


    @Autowired
    public BankUserAdminController(BankCardUserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/{id}/change-username")
    public ResponseEntity<?> changeUsername(@PathVariable int id, String newUsername) {
        try {
            userService.changeUsernameById(id, newUsername);
            return ResponseEntity.status(200).build();
        } catch (BadRequestException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/change-email")
    public ResponseEntity<?> changeEmail(@PathVariable int id, String newEmail) {
        try {
            userService.changeEmailById(id, newEmail);
            return ResponseEntity.status(200).build();
        } catch (BadRequestException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable int id, String newPassword) {
        try {
            userService.changePasswordById(id, newPassword);
            return ResponseEntity.status(200).build();
        } catch (BadRequestException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            userService.deleteById(id);
            return ResponseEntity.status(200).build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }
}
