package v7nny.bank.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import v7nny.bank.card.documentation.bankcard.user.GetMyBankCardBalanceDoc;
import v7nny.bank.card.documentation.bankcard.user.GetMyBankCardsDoc;
import v7nny.bank.card.documentation.bankcard.user.TransferBetweenMyBankCardsDoc;
import v7nny.bank.card.dto.ResultTransferDTO;
import v7nny.bank.card.dto.TransferDTO;
import v7nny.bank.card.entity.BankCard;
import v7nny.bank.card.exception.bankcard.BankCardNotFoundException;
import v7nny.bank.card.exception.bankcard.CardAccessDeniedException;
import v7nny.bank.card.exception.bankcard.InsufficientFundsException;
import v7nny.bank.card.exception.user.UserNotFoundException;
import v7nny.bank.card.service.BankCardService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cards")
public class BankCardController {

    private final BankCardService bankCardService;


    @Autowired
    public BankCardController(BankCardService bankCardService) {
        this.bankCardService = bankCardService;
    }

    @GetMapping("/my")
    @GetMyBankCardsDoc
    public ResponseEntity<?> getMy(@RequestParam int page, @RequestParam int size,
                                    @RequestParam(required = false) String cardNumber, Principal principal) {
        try {
            List<BankCard> cards = bankCardService.findByUsernameAndCardNumberLike(
                    page, size, cardNumber, principal.getName());

            return ResponseEntity.status(200).body(cards);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{id}/balance")
    @GetMyBankCardBalanceDoc
    public ResponseEntity<?> getBalance(@PathVariable int id, Principal principal) {
        try {
            BigDecimal balance = bankCardService.getBalanceByUsername(id, principal.getName());

            return ResponseEntity.status(200).body(balance);
        }catch (CardAccessDeniedException e) {
            return ResponseEntity.status(403).body(Map.of("message", e.getMessage()));
        } catch (BankCardNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    @PatchMapping("/my/transfer")
    @TransferBetweenMyBankCardsDoc
    public ResponseEntity<?> transferBetweenOwnCards(@RequestBody TransferDTO transferDTO, Principal principal) {
        try {
            ResultTransferDTO transferResult = bankCardService
                    .transferBetweenOwnCardsByUsername(transferDTO, principal.getName());

            return ResponseEntity.status(200).body(transferResult);
        } catch (InsufficientFundsException e) {
            return ResponseEntity.status(402).body(Map.of("message", e.getMessage()));
        } catch (CardAccessDeniedException e) {
            return ResponseEntity.status(403).body(Map.of("message", e.getMessage()));
        } catch (BankCardNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }
}