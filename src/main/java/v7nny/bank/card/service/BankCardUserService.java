package v7nny.bank.card.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v7nny.bank.card.entity.BankCardUser;
import v7nny.bank.card.exception.UserNotFoundException;
import v7nny.bank.card.repository.BankCardUserRepository;

@Service
@Transactional(readOnly = true)
public class BankCardUserService {

    private final BankCardUserRepository userRepository;


    @Autowired
    public BankCardUserService(BankCardUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public BankCardUser findOneById(int id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("User not found"));
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.findOneByUsername(username).isPresent();
    }

    public boolean isEmailTaken(String email) {
        return userRepository.findOneByEmail(email).isPresent();
    }

    @Transactional
    public void save(BankCardUser user) {
        userRepository.save(user);
    }
}