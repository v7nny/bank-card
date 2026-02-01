package v7nny.bank.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import v7nny.bank.card.documentation.blockcardrequest.admin.CompleteBlockCardRequestDoc;
import v7nny.bank.card.documentation.blockcardrequest.admin.FindAllBlockCardRequestsDoc;
import v7nny.bank.card.entity.BlockBankCardRequest;
import v7nny.bank.card.exception.bankcard.BankCardNotFoundException;
import v7nny.bank.card.exception.blockcardrequest.BlockBankCardRequestNotFoundException;
import v7nny.bank.card.exception.bankcard.CardExpiredException;
import v7nny.bank.card.exception.bankcard.CardStatusAlreadySetException;
import v7nny.bank.card.service.BlockBankCardRequestService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/block-card-requests")
public class BlockBankCardRequestAdminController {

    private final BlockBankCardRequestService blockBankCardRequestService;


    @Autowired
    public BlockBankCardRequestAdminController(BlockBankCardRequestService blockBankCardRequestService) {
        this.blockBankCardRequestService = blockBankCardRequestService;
    }

    @GetMapping
    @FindAllBlockCardRequestsDoc
    public ResponseEntity<?> getAll(@RequestParam int page, int size) {
        List<BlockBankCardRequest> blockBankCardRequests = blockBankCardRequestService.findAll(page, size);

        return ResponseEntity.status(200).body(blockBankCardRequests);
    }

    @PatchMapping("/{id}/complete")
    @CompleteBlockCardRequestDoc
    public ResponseEntity<?> completeBlockRequest(@PathVariable int id) {
        try {
            blockBankCardRequestService.completeBlockRequestById(id);
            return ResponseEntity.status(200).build();
        } catch (CardExpiredException e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        } catch (BankCardNotFoundException | BlockBankCardRequestNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        } catch (CardStatusAlreadySetException e) {
            return ResponseEntity.status(409).body(Map.of("message", e.getMessage()));
        }
    }
}