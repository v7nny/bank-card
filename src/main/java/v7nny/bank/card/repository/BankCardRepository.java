package v7nny.bank.card.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import v7nny.bank.card.entity.BankCard;
import v7nny.bank.card.entity.enums.BankCardStatus;

@Repository
public interface BankCardRepository extends CrudRepository<BankCard, Integer> {

    @Modifying
    @Query("DELETE BankCard WHERE id = :id")
    int deleteByIdL(int id);
}
