package v7nny.bank.card.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import v7nny.bank.card.documentation.bankcard.admin.*;
import v7nny.bank.card.entity.BankCard;
import v7nny.bank.card.entity.enums.BankCardStatus;
import v7nny.bank.card.exception.bankcard.BankCardNotFoundException;
import v7nny.bank.card.exception.bankcard.CardExpiredException;
import v7nny.bank.card.exception.bankcard.CardNumberEncryptException;
import v7nny.bank.card.exception.bankcard.CardStatusAlreadySetException;
import v7nny.bank.card.exception.user.UserNotFoundException;
import v7nny.bank.card.service.BankCardService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/cards")
@Validated
public class BankCardAdminController {

    private final BankCardService bankCardService;


    @Autowired
    public BankCardAdminController(BankCardService bankCardService) {
        this.bankCardService = bankCardService;
    }

    @GetMapping
    @GetAllBankCardsDoc
    public ResponseEntity<?> getAll(@RequestParam @Min(value = 0, message = "{validation.page.index-min}") int page,
                                    @RequestParam @Min(value = 1, message = "{validation.page.size-min}") int size) {
        return ResponseEntity.status(200).body(bankCardService.findAll(page, size));
    }

    @PostMapping
    @CreateBankCardDoc
    public ResponseEntity<?> create(@RequestParam @Min(value = 1, message= "{validation.id-min}") int userId) {
        try {
            BankCard savedCard = bankCardService.create(userId);

            return ResponseEntity.status(201).body(savedCard);
        } catch(ConstraintViolationException e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        } catch (CardNumberEncryptException e) {
            return ResponseEntity.status(500).body(Map.of("message", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/activate")
    @ActivateBankCardDoc
    public ResponseEntity<?> activate(@PathVariable @Min(value = 1, message = "${validation.id-min}") int id) {
        try {
            bankCardService.changeStatus(id, BankCardStatus.ACTIVE);
            return ResponseEntity.status(200).build();
        } catch (CardExpiredException e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        }catch (BankCardNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        } catch (CardStatusAlreadySetException e) {
            return ResponseEntity.status(409).body(Map.of("message", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/block")
    @BlockBankCardDoc
    public ResponseEntity<?> block(@PathVariable @Min(value = 1, message = "${validation.id-min}") int id) {
        try {
            bankCardService.changeStatus(id, BankCardStatus.BLOCKED);
            return ResponseEntity.status(200).build();
        } catch (CardExpiredException e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        }catch (BankCardNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        } catch (CardStatusAlreadySetException e) {
            return ResponseEntity.status(409).body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @DeleteBankCardDoc
    public ResponseEntity<?> delete(@PathVariable @Min(value = 1, message = "${validation.id-min}") int id) {
        try {
            bankCardService.deleteById(id);
            return ResponseEntity.status(200).build();
        } catch (BankCardNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }
}