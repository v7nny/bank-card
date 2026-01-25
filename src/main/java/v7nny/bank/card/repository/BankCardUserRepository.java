package v7nny.bank.card.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import v7nny.bank.card.entity.BankCardUser;

import java.util.Optional;

@Repository
public interface BankCardUserRepository extends JpaRepository<BankCardUser, Integer> {
    Optional<BankCardUser> findOneByEmail(String email);

    Optional<BankCardUser> findOneByUsername(String username);
}
