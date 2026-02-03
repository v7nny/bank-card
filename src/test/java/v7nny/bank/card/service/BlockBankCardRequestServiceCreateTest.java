package v7nny.bank.card.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import v7nny.bank.card.entity.BankCard;
import v7nny.bank.card.entity.BankCardUser;
import v7nny.bank.card.entity.BlockBankCardRequest;
import v7nny.bank.card.exception.bankcard.BankCardNotFoundException;
import v7nny.bank.card.exception.bankcard.CardAccessDeniedException;
import v7nny.bank.card.repository.BlockBankCardRequestRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlockBankCardRequestServiceCreateTest {

    @Mock
    private BlockBankCardRequestRepository blockBankCardRequestRepository;

    @Mock
    private BankCardService bankCardService;

    private BankCardUser user;

    private BankCard card;

    private BlockBankCardRequest blockRequest;

    @InjectMocks
    BlockBankCardRequestService blockBankCardRequestService;


    @BeforeEach
    void setUp() {
        user = new BankCardUser();
        user.setUsername("username");

        card = new BankCard();
        card.setId(1);
        card.setUser(user);

        blockRequest = new BlockBankCardRequest(card);
        blockRequest.setId(1);
    }

    @Test
    void create_ShouldReturnBlockRequest_WhenUserHasAccessToCard() throws BankCardNotFoundException, CardAccessDeniedException {
        when(bankCardService.findOneById(card.getId())).thenReturn(card);
        when(blockBankCardRequestRepository.save(any(BlockBankCardRequest.class)))
                .thenReturn(blockRequest);

        BlockBankCardRequest blockRequestResult = blockBankCardRequestService.create(card.getId(), user.getUsername());

        assertNotNull(blockRequestResult);
        assertEquals(blockRequest, blockRequestResult);
        verify(bankCardService, times(1)).findOneById(card.getId());
        verify(blockBankCardRequestRepository, times(1)).save(any(BlockBankCardRequest.class));
    }

    @Test
    void create_ShouldPropagateBankCardNotFoundException() throws BankCardNotFoundException {
        when(bankCardService.findOneById(card.getId()))
                .thenThrow(new BankCardNotFoundException("Bank card not found"));

        assertThrows(BankCardNotFoundException.class, () -> {
            blockBankCardRequestService.create(card.getId(), user.getUsername());
        });
        verify(bankCardService, times(1)).findOneById(card.getId());
        verify(blockBankCardRequestRepository, never()).save(any());
    }

    @Test
    void create_ShouldThrowCardAccessDeniedException_WhenUserHasNoAccess() throws BankCardNotFoundException {
        var requestUsername = "username1";
        when(bankCardService.findOneById(card.getId())).thenReturn(card);

        assertThrows(CardAccessDeniedException.class, () -> {
            blockBankCardRequestService.create(card.getId(), requestUsername);
        });
        verify(bankCardService, times(1)).findOneById(card.getId());
        verify(blockBankCardRequestRepository, never()).save(any());
    }
}
