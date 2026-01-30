package v7nny.bank.card.entity;

import jakarta.persistence.*;
import v7nny.bank.card.entity.enums.BlockBankCardRequestStatus;

@Entity
@Table(name = "Block_card_requests")
public class BlockBankCardRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "status")
    private BlockBankCardRequestStatus status;

    @OneToOne
    @JoinColumn(name = "bank_card_id", referencedColumnName = "id")
    private BankCard bankCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private BankCardUser user;


    public BlockBankCardRequest() {}

    public BlockBankCardRequest(BankCard bankCard) {
        this.status = BlockBankCardRequestStatus.PENDING;
        this.bankCard = bankCard;
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

    public void setStatus(BlockBankCardRequestStatus status) {
        this.status = status;
    }

    public void setBankCard(BankCard bankCard) {
        this.bankCard = bankCard;
    }
}
