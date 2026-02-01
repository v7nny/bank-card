package v7nny.bank.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
public class BankCardAdminController {

    private final BankCardService bankCardService;


    @Autowired
    public BankCardAdminController(BankCardService bankCardService) {
        this.bankCardService = bankCardService;
    }

    @GetMapping
    @GetAllBankCardsDoc
    public ResponseEntity<List<BankCard>> getAll(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.status(200).body(bankCardService.findAll(page, size));
    }

    @PostMapping
    @CreateBankCardDoc
    public ResponseEntity<?> create(@RequestParam int userId) {
        try {
            BankCard savedCard = bankCardService.create(userId);

            return ResponseEntity.status(201).body(savedCard);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        } catch (CardNumberEncryptException e) {
            return ResponseEntity.status(500).body(Map.of("message", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/activate")
    @ActivateBankCardDoc
    public ResponseEntity<?> activate(@PathVariable int id) {
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
    public ResponseEntity<?> block(@PathVariable int id) {
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
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            bankCardService.deleteById(id);
            return ResponseEntity.status(200).build();
        } catch (BankCardNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }
}