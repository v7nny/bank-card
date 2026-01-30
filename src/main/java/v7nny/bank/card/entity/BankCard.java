package v7nny.bank.card.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import tools.jackson.databind.annotation.JsonSerialize;
import v7nny.bank.card.entity.enums.BankCardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Bank_cards")
public class BankCard {

    @Id
    @JsonProperty("iD")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setStatus(BankCardStatus status) {
        this.status = status;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setUser(BankCardUser user) {
        this.user = user;
    }

    public boolean isCardExpired() {
        return this.status == BankCardStatus.EXPIRED;
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