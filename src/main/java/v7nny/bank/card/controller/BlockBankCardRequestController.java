package v7nny.bank.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import v7nny.bank.card.entity.BlockBankCardRequest;
import v7nny.bank.card.exception.BankCardNotFoundException;
import v7nny.bank.card.exception.CardAccessDeniedException;
import v7nny.bank.card.exception.UserNotFoundException;
import v7nny.bank.card.service.BlockBankCardRequestService;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/block-card-requests")
public class BlockBankCardRequestController {

    private final BlockBankCardRequestService blockBankCardRequestService;


    @Autowired
    public BlockBankCardRequestController(BlockBankCardRequestService blockBankCardRequestService) {
        this.blockBankCardRequestService = blockBankCardRequestService;
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMy(Principal principal) {
        try {
            return ResponseEntity.status(200).body(blockBankCardRequestService.findAllByUsername(principal.getName()));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(int cardId, Principal principal) {
        try {
            BlockBankCardRequest blockRequest = blockBankCardRequestService.create(cardId, principal.getName());

            return ResponseEntity.status(201).body(blockRequest);
        } catch (CardAccessDeniedException e) {
            return ResponseEntity.status(403).body(Map.of("message", e.getMessage()));
        } catch (BankCardNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }
}