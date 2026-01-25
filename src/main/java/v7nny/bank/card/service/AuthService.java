package v7nny.bank.card.service;

import jakarta.servlet.http.Cookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import v7nny.bank.card.dto.SignInDTO;
import v7nny.bank.card.security.JwtProvider;
import java.util.Date;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;
    private final BankCardUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(JwtProvider jwtProvider, BankCardUserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }


    public Cookie signIn(SignInDTO signInDTO) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInDTO.login(), signInDTO.password()));
        var tokenString = jwtProvider.generateToken(authentication);
        var token = jwtProvider.getAllClaims(tokenString);
        var cookie = new Cookie("__Host-auth-token", tokenString);
        var now = new Date().getTime();
        var cookieAge = token.getExpiration().getTime() - now;

        cookie.setPath("/");
        cookie.setDomain(null);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) (cookieAge / 1000));

        System.out.println(tokenString);

        return cookie;
    }
}