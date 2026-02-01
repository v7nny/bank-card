package v7nny.bank.card.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import v7nny.bank.card.entity.enums.BankCardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Bank_cards")
public class BankCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnore
    @Column(name = "encrypted_card_number")
    private String encryptedCardNumber;

    @Column(name = "masked_card_number")
    private String maskedCardNumber;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BankCardStatus status;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @JsonIgnore
    @OneToMany(mappedBy = "bankCard", fetch = FetchType.LAZY)
    private List<BlockBankCardRequest> blockRequests;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private BankCardUser user;


    public BankCard() {}

    public BankCard(String encryptedCardNumber, String maskedCardNumber,
                    LocalDate expiryDate, BankCardUser user) {
        this.encryptedCardNumber = encryptedCardNumber;
        this.maskedCardNumber = maskedCardNumber;
        this.balance = BigDecimal.valueOf(0.00);
        this.status = BankCardStatus.ACTIVE;
        this.expiryDate = expiryDate;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getEncryptedCardNumber() {
        return encryptedCardNumber;
    }

    public String getMaskedCardNumber() {
        return maskedCardNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BankCardStatus getStatus() {
        return status;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public BankCardUser getUser() {
        return user;
    }

    public void setStatus(BankCardStatus status) {
        this.status = status;
    }

    public void setUser(BankCardUser user) {
        this.user = user;
    }

    @JsonIgnore
    public boolean isCardExpired() {
        return this.status == BankCardStatus.EXPIRED;
    }

    public void increaseBalance(BigDecimal increaseSum) {
        this.balance = this.balance.add(increaseSum);
    }

    public void decreaseBalance(BigDecimal decreaseBalance) {
        this.balance = this.balance.subtract(decreaseBalance);
    }

    @Override
    public String toString() {
        return "BankCard{" +
                "expiryDate=" + expiryDate +
                ", status=" + status +
                ", balance=" + balance +
                ", maskedCardNumber='" + maskedCardNumber + '\'' +
                ", encryptedCardNumber='" + encryptedCardNumber + '\'' +
                ", id=" + id +
                '}';
    }
}