package lidofon.auth.service;

import lidofon.auth.entity.Token;
import lidofon.auth.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;
    public String generateToken(String username) {
        String tokenValue = generateRandomString();
        UUID uuid = UUID.randomUUID();
        Token token = Token.builder()
                .id(uuid.toString())
                .tokenValue(tokenValue)
                .createdAt(LocalDateTime.now())
                .expireDate(LocalDateTime.now().plusHours(1))
                .build();;
        tokenRepository.save(token);
        return tokenValue;
    }

    public boolean validateToken(String tokenValue) {
        return tokenRepository.findByValue(tokenValue)
                .filter(token -> token.getExpireDate().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    private String generateRandomString() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
