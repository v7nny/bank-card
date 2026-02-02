package v7nny.bank.card.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.apache.coyote.BadRequestException;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@Validated
public class BankUserAdminController {

    private final BankCardUserService userService;


    @Autowired
    public BankUserAdminController(BankCardUserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/{id}/username")
    @ChangeUsernameDoc
    public ResponseEntity<?> changeUsername(@PathVariable @Min(value = 1, message= "{validation.id-min}") int id,
                                            @NotBlank(message = "{validation.username.not-blank}")
                                            @Length(min = 5, max = 10, message = "{validation.username.length}") String newUsername) {
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
    public ResponseEntity<?> changeEmail(@PathVariable @Min(value = 1, message= "{validation.id-min}") int id,
                                         @NotBlank(message = "{validation.email.not-blank}")
                                         @Email(message = "{validation.email}", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$") String newEmail) {
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
    public ResponseEntity<?> changePassword(@PathVariable @Min(value = 1, message= "{validation.id-min}") int id,
                                            @NotBlank(message = "{validation.password.not-blank}")
                                            @Length(min = 8, message = "{validation.password.length}") String newPassword) {
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
    public ResponseEntity<?> delete(@PathVariable @Min(value = 1, message= "{validation.id-min}") int id) {
        try {
            userService.deleteById(id);
            return ResponseEntity.status(200).build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }
}