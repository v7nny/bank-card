package v7nny.bank.card.controller;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import v7nny.bank.card.documentation.user.ChangeEmailDoc;
import v7nny.bank.card.documentation.user.ChangePasswordDoc;
import v7nny.bank.card.documentation.user.ChangeUsernameDoc;
import v7nny.bank.card.documentation.user.DeleteUserDoc;
import v7nny.bank.card.exception.user.UserNotFoundException;
import v7nny.bank.card.service.BankCardUserService;

import java.util.Map;

@RestController
@RequestMapping("/admin/users")
public class BankUserAdminController {

    private final BankCardUserService userService;


    @Autowired
    public BankUserAdminController(BankCardUserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/{id}/username")
    @ChangeUsernameDoc
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

    @PatchMapping("/{id}/email")
    @ChangeEmailDoc
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

    @PatchMapping("/{id}/password")
    @ChangePasswordDoc
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
    @DeleteUserDoc
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            userService.deleteById(id);
            return ResponseEntity.status(200).build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }
}
