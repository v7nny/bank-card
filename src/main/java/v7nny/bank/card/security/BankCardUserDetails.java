package v7nny.bank.card.security;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import v7nny.bank.card.entity.BankCardUser;
import v7nny.bank.card.entity.Role;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BankCardUserDetails implements UserDetails {

    private final String username;

    private final String email;

    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;


    public BankCardUserDetails(String username, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public static BankCardUserDetails build(BankCardUser user) {
        return new BankCardUserDetails(user.getUsername(), user.getEmail(), user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
    }
}
