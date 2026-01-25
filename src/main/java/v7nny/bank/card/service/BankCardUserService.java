package v7nny.bank.card.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import v7nny.bank.card.entity.BankCardUser;
import v7nny.bank.card.repository.BankCardUserRepository;

import java.util.List;

@Service
public class BankCardUserService {

    private final BankCardUserRepository userRepository;


    @Autowired
    public BankCardUserService(BankCardUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<BankCardUser> findAll() {
        return userRepository.findAll();
    }
}
