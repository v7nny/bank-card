package v7nny.bank.card.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import v7nny.bank.card.entity.BankCard;

import java.util.List;

@Repository
public interface BankCardRepository extends JpaRepository<BankCard, Integer> {

    @Query("SELECT b FROM BankCard b")
    List<BankCard> findAllAsList(Pageable pageable);

    List<BankCard> findAllByUserId(int id, Pageable pageable);

    @Query("SELECT b FROM BankCard b WHERE b.user.id = :userId AND b.maskedCardNumber LIKE %:maskedCardNumber%")
    List<BankCard> findByUserIdAndCardNumberLike(int userId, String maskedCardNumber, Pageable pageable);

    @Modifying
    @Query("DELETE BankCard WHERE id = :id")
    int deleteById(int id);
}
