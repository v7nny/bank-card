package v7nny.bank.card.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;
import v7nny.bank.card.entity.BlockBankCardRequest;

@Repository
public interface BlockBankCardRequestRepository extends CrudRepository<BlockBankCardRequest, Integer> {}
