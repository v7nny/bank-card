package v7nny.bank.card.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import v7nny.bank.card.entity.BankCardUser;

import java.util.Optional;

@Repository
public interface BankCardUserRepository extends CrudRepository<BankCardUser, Integer> {
    Optional<BankCardUser> findOneByEmail(String email);

    Optional<BankCardUser> findOneByUsername(String username);
}