package v7nny.bank.card.security;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import v7nny.bank.card.entity.BankCardUser;

import java.util.Collection;
import java.util.List;

public class BankCardUserDetails implements UserDetails {

    private final String username;
    private final String email;
    private final String password;

    public BankCardUserDetails(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public static BankCardUserDetails build(BankCardUser user) {
        return new BankCardUserDetails(user.getUsername(), user.getEmail(), user.getPassword());
    }
}
