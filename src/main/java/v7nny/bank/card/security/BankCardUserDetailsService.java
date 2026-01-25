package v7nny.bank.card.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import v7nny.bank.card.repository.BankCardUserRepository;

@Service
public class BankCardUserDetailsService implements UserDetailsService {
    private final BankCardUserRepository userRepository;

    @Autowired
    public BankCardUserDetailsService(BankCardUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return BankCardUserDetails.build(userRepository.findOneByEmail(login).or(() ->
                userRepository.findOneByUsername(login)).orElseThrow(() ->
                new UsernameNotFoundException("User with login \"%s\" not found".formatted(login))));
    }
}