package v7nny.bank.card.service;

import jakarta.servlet.http.Cookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v7nny.bank.card.dto.SignInDTO;
import v7nny.bank.card.dto.SignUpDTO;
import v7nny.bank.card.entity.BankCardUser;
import v7nny.bank.card.exception.EmailAlreadyTakenException;
import v7nny.bank.card.exception.UsernameAlreadyTakenException;
import v7nny.bank.card.security.JwtProvider;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final JwtProvider jwtProvider;

    private final BankCardUserService userService;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;


    public AuthService(JwtProvider jwtProvider, BankCardUserService userService, RoleService roleService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public Cookie signUp(SignUpDTO signUpDTO) throws UsernameAlreadyTakenException, EmailAlreadyTakenException {
        if (userService.isUsernameTaken(signUpDTO.username())) throw new UsernameAlreadyTakenException("Username already taken");
        if (userService.isEmailTaken(signUpDTO.email())) throw new EmailAlreadyTakenException("Email already taken");

        var role = roleService.getUserRole();
        var hashedPassword = passwordEncoder.encode(signUpDTO.password());
        var user = new BankCardUser(signUpDTO.username(), signUpDTO.email(), hashedPassword, List.of(role));

        userService.save(user);
        return signIn(new SignInDTO(signUpDTO.username(), signUpDTO.password()));
    }

    public Cookie signIn(SignInDTO signInDTO) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInDTO.login(), signInDTO.password()));
        var tokenString = jwtProvider.generateToken(authentication);
        var tokenExpiration = jwtProvider.getTokenExpiration(tokenString).getTime();
        var cookie = new Cookie("__Host-auth-token", tokenString);
        var now = new Date().getTime();
        var cookieLifetime = tokenExpiration - now;

        cookie.setPath("/");
        cookie.setDomain(null);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) (cookieLifetime / 1000));

        return cookie;
    }
}