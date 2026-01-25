package v7nny.bank.card.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import v7nny.bank.card.entity.enums.BankCardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Bank_cards")
public class BankCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "status")
    private BankCardStatus status;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private BankCardUser user;


    public BankCard() {}

    public BankCard(String cardNumber, BigDecimal balance, BankCardStatus status, LocalDate expiryDate) {
        this.cardNumber = cardNumber;
        this.balance = balance;
        this.status = status;
        this.expiryDate = expiryDate;
    }

    public int getId() {
        return id;
    }

    public String getCardNumber() {
        return cardNumber;
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
}