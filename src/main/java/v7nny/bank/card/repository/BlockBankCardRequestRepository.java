package v7nny.bank.card.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import v7nny.bank.card.entity.BlockBankCardRequest;

import java.util.List;

@Repository
public interface BlockBankCardRequestRepository extends JpaRepository<BlockBankCardRequest, Integer> {

    @Query("SELECT b FROM BlockBankCardRequest b")
    List<BlockBankCardRequest> findAllAsList(Pageable pageable);
}
