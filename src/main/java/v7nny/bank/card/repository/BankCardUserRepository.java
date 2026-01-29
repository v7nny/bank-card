package v7nny.bank.card.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import v7nny.bank.card.entity.BankCardUser;

import java.util.Optional;

@Repository
public interface BankCardUserRepository extends CrudRepository<BankCardUser, Integer> {
    Optional<BankCardUser> findOneByEmail(String email);

    Optional<BankCardUser> findOneByUsername(String username);

    @Modifying
    @Query("DELETE BankCardUser WHERE id = :id")
    int deleteById(int id);
}