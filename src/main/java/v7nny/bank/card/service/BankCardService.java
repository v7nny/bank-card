package v7nny.bank.card.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v7nny.bank.card.dto.ResultTransferDTO;
import v7nny.bank.card.dto.TransferDTO;
import v7nny.bank.card.entity.BankCard;
import v7nny.bank.card.entity.enums.BankCardStatus;
import v7nny.bank.card.exception.*;
import v7nny.bank.card.repository.BankCardRepository;
import v7nny.bank.card.util.CardNumberEncryptor;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BankCardService {

    private final BankCardRepository bankCardRepository;

    private final BankCardUserService userService;

    private final CardNumberEncryptor cardNumberEncryptor;

    private final LocalDate bankCardExpiryDate;


    @Autowired
    public BankCardService(BankCardRepository bankCardRepository, BankCardUserService userService,
                           CardNumberEncryptor cardNumberEncryptor, @Value("${bank-card.lifetime-in-years}") int cardLifetimeInYears) {
        this.bankCardRepository = bankCardRepository;
        this.userService = userService;
        this.cardNumberEncryptor = cardNumberEncryptor;
        this.bankCardExpiryDate = LocalDate.now().plusYears(cardLifetimeInYears);
    }

    public BankCard findOneById(int id) throws BankCardNotFoundException {
        return bankCardRepository.findById(id).orElseThrow(() ->
                new BankCardNotFoundException("Bank card with id %d not found".formatted(id)));
    }

    public List<BankCard> findAll(int page, int size) {
        return bankCardRepository.findAllAsList(PageRequest.of(page, size));
    }

    public List<BankCard> findByUsernameAndCardNumberLike(int page, int size, String cardNumber, String username) throws UserNotFoundException {
        var user = userService.findOneByUsername(username);

        if(cardNumber == null) return bankCardRepository.findAllByUserId(user.getId(), PageRequest.of(page, size));

        return bankCardRepository.findByUserIdAndCardNumberLike(
                user.getId(), cardNumber, PageRequest.of(page, size));
    }

    public BigDecimal getBalanceByUsername(int id, String username) throws BankCardNotFoundException, CardAccessDeniedException {
        var card = findOneById(id);

        if(!card.getUser().getUsername().equals(username))
            throw new CardAccessDeniedException("User %s doesn't have access to this bank card".formatted(username));

        return card.getBalance();
    }

    public String getDecryptedCardNumberByCardId(int cardId) throws BankCardNotFoundException, CardNumberDecryptException {
        var bankCard = findOneById(cardId);

        return cardNumberEncryptor.decryptCardNumber(bankCard.getEncryptedCardNumber());
    }

    @Transactional
    public void changeStatus(int id, BankCardStatus newStatus) throws BankCardNotFoundException, CardStatusAlreadySetException, CardExpiredException {
        var card = findOneById(id);

        validateCardStatus(card, newStatus);
        card.setStatus(newStatus);
        bankCardRepository.save(card);
    }

    @Transactional
    public BankCard create(int userId) throws UserNotFoundException, CardNumberEncryptException {
        String cardNumber = generateCardNumber();
        String maskedCardNumber = cardNumberEncryptor.maskCardNumber(cardNumber);
        String encryptedCardNumber = cardNumberEncryptor.encryptCardNumber(cardNumber);
        var user = userService.findOneById(userId);
        var bankCard = new BankCard(encryptedCardNumber, maskedCardNumber, bankCardExpiryDate, user);

        return bankCardRepository.save(bankCard);
    }

    @Transactional
    public ResultTransferDTO transferBetweenOwnCardsByUsername (TransferDTO transferDTO, String username) throws BankCardNotFoundException, CardAccessDeniedException, InsufficientFundsException {
        var fromCard = findOneById(transferDTO.fromCardId());
        var toCard = findOneById(transferDTO.toCardId());
        var balanceBeforeTransferOnFromCard = fromCard.getBalance();
        var balanceBeforeTransferOnToCard = toCard.getBalance();

        validateBalance(balanceBeforeTransferOnFromCard, transferDTO.amount());
        validateCardsOwnership(fromCard, toCard, username);
        fromCard.decreaseBalance(transferDTO.amount());
        toCard.increaseBalance(transferDTO.amount());
        bankCardRepository.saveAll(List.of(fromCard, toCard));

        return new ResultTransferDTO(balanceBeforeTransferOnFromCard, fromCard.getBalance(),
                balanceBeforeTransferOnToCard, toCard.getBalance());
    }

    @Transactional
    public void deleteById(int id) throws BankCardNotFoundException {
        int result = bankCardRepository.deleteById(id);

        if(result == 0) throw new BankCardNotFoundException("Bank card with id %d not found".formatted(id));
    }

    private String generateCardNumber() {
        var random = new SecureRandom();
        var stringBuilder = new StringBuilder();

        for(int i = 0; i < 16; ++i) {
            stringBuilder.append(random.nextInt(10));
        }

        return stringBuilder.toString();
    }

    private void validateCardStatus(BankCard card, BankCardStatus newStatus) throws CardStatusAlreadySetException, CardExpiredException {
        if(card.isCardExpired()) throw new CardExpiredException("Bank card is expired");
        if(card.getStatus() == newStatus) throw new CardStatusAlreadySetException("Bank card already has this status set");
    }

    private void validateCardsOwnership(BankCard fromCard, BankCard toCard, String username) throws CardAccessDeniedException {
        if(!fromCard.getUser().getUsername().equals(username) || !toCard.getUser().getUsername().equals(username))
            throw new CardAccessDeniedException("User %s doesn't have access to one of the bank cards".formatted(username));
    }

    private void validateBalance(BigDecimal fromCardBalance, BigDecimal amount) throws InsufficientFundsException {
        if(fromCardBalance.compareTo(amount) < 0)
            throw new InsufficientFundsException("There are not enough funds on the card");
    }
}