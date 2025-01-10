package lidofon.auth.service;

import lidofon.auth.dto.UserDto;
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
    public String generateToken(UserDto user) {
        String tokenValue = generateRandomString();
        UUID uuid = UUID.randomUUID();
        Token token = Token.builder()
                .id(uuid.toString())
                .value(tokenValue)
                .ip("127.0.0.1")
                .userAgent("Chrome")
                .userId(user.getId())
                .createdAt(LocalDateTime.now())
                .expireDate(LocalDateTime.now().plusHours(1))
                .build();;
        tokenRepository.save(token);
        return tokenValue;
    }

    public boolean validateToken(String tokenValue) {
        return tokenRepository.findFirstByValue(tokenValue)
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
