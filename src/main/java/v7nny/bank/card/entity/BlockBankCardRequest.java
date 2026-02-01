package v7nny.bank.card.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import v7nny.bank.card.entity.enums.BlockBankCardRequestStatus;

@Entity
@Table(name = "Block_card_requests")
public class BlockBankCardRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BlockBankCardRequestStatus status;

    @OneToOne
    @JoinColumn(name = "bank_card_id", referencedColumnName = "id")
    private BankCard bankCard;

    @JsonIgnoreProperties("hibernateLazyInitializer")
    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private BankCardUser user;


    public BlockBankCardRequest() {}

    public BlockBankCardRequest(BankCard bankCard) {
        this.status = BlockBankCardRequestStatus.PENDING;
        this.bankCard = bankCard;
        this.user = bankCard.getUser();
    }

    public int getId() {
        return id;
    }

    public BlockBankCardRequestStatus getStatus() {
        return status;
    }

    public BankCard getBankCard() {
        return bankCard;
    }

    public BankCardUser getUser() {
        return user;
    }

    public void setStatus(BlockBankCardRequestStatus status) {
        this.status = status;
    }

    public void setUser(BankCardUser user) {
        this.user = user;
    }
}
