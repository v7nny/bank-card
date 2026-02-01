package v7nny.bank.card.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v7nny.bank.card.entity.BlockBankCardRequest;
import v7nny.bank.card.entity.enums.BankCardStatus;
import v7nny.bank.card.entity.enums.BlockBankCardRequestStatus;
import v7nny.bank.card.exception.*;
import v7nny.bank.card.repository.BlockBankCardRequestRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BlockBankCardRequestService {

    private final BlockBankCardRequestRepository blockBankCardRequestRepository;

    private final BankCardService bankCardService;

    private final BankCardUserService userService;

    @Autowired
    public BlockBankCardRequestService(BlockBankCardRequestRepository blockBankCardRequestRepository, BankCardService bankCardService, BankCardUserService userService) {
        this.blockBankCardRequestRepository = blockBankCardRequestRepository;
        this.bankCardService = bankCardService;
        this.userService = userService;
    }

    public BlockBankCardRequest findOneById(int id) throws BlockBankCardRequestNotFoundException {
        return blockBankCardRequestRepository.findById(id).orElseThrow(() ->
                new BlockBankCardRequestNotFoundException("Block bank card request with id %d not found".formatted(id)));
    }

    public List<BlockBankCardRequest> findAll(int page, int size) {
        return blockBankCardRequestRepository.findAllAsList(PageRequest.of(page, size));
    }

    public List<BlockBankCardRequest> findAllByUsername(String username) throws UserNotFoundException {
        var user = userService.findOneByUsername(username);

        return user.getBlockCardRequests();
    }

    @Transactional
    public BlockBankCardRequest create(int cardId, String username) throws BankCardNotFoundException, CardAccessDeniedException {
        var card = bankCardService.findOneById(cardId);

        if(!card.getUser().getUsername().equals(username))
            throw new CardAccessDeniedException("User %s doesn't have access to this bank card".formatted(username));

        var blockRequest = new BlockBankCardRequest(card);

        return blockBankCardRequestRepository.save(blockRequest);
    }

    @Transactional
    public void completeBlockRequestById(int requestId) throws CardStatusAlreadySetException, BankCardNotFoundException, CardExpiredException, BlockBankCardRequestNotFoundException {
        var blockRequest = findOneById(requestId);

        bankCardService.changeStatus(blockRequest.getBankCard().getId(), BankCardStatus.BLOCKED);
        blockRequest.setStatus(BlockBankCardRequestStatus.COMPLETED);

        blockBankCardRequestRepository.save(blockRequest);
    }
}
