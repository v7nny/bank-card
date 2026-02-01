package v7nny.bank.card.service;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v7nny.bank.card.entity.BankCardUser;
import v7nny.bank.card.exception.user.UserNotFoundException;
import v7nny.bank.card.repository.BankCardUserRepository;

@Service
@Transactional(readOnly = true)
public class BankCardUserService {

    private final BankCardUserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BankCardUserService(BankCardUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public BankCardUser findOneById(int id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("User not found"));
    }

    public BankCardUser findOneByUsername(String username) throws UserNotFoundException {
        return userRepository.findOneByUsername(username).orElseThrow(() ->
                new UserNotFoundException("User not found"));
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.findOneByUsername(username).isPresent();
    }

    public boolean isEmailTaken(String email) {
        return userRepository.findOneByEmail(email).isPresent();
    }

    @Transactional
    public void changeUsernameById(int id, String newUsername) throws UserNotFoundException, BadRequestException {
        var user = findOneById(id);

        if (user.getUsername().equals(newUsername)) throw new BadRequestException("Usernames match");

        user.setUsername(newUsername);
        userRepository.save(user);
    }

    @Transactional
    public void changeEmailById(int id, String newEmail) throws UserNotFoundException, BadRequestException {
        var user = findOneById(id);

        if(user.getEmail().equals(newEmail)) throw new BadRequestException("Emails match");

        user.setEmail(newEmail);
        userRepository.save(user);
    }

    @Transactional
    public void changePasswordById(int id, String newPassword) throws UserNotFoundException, BadRequestException {
        var user = findOneById(id);

        if (passwordEncoder.matches(newPassword, user.getPassword())) throw new BadRequestException("Passwords match");

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void save(BankCardUser user) {
        userRepository.save(user);
    }

    @Transactional
    public void deleteById(int id) throws UserNotFoundException {
        var result = userRepository.deleteById(id);

        if(result == 0) throw new UserNotFoundException("User with id %d not found".formatted(id));
    }
}