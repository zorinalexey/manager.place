package lidofon.auth.service;

import lidofon.auth.client.UserServiceClient;
import lidofon.auth.dto.RegisterDto;
import lidofon.auth.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final TokenService tokenService;
    private final UserServiceClient userServiceClient;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public String register(RegisterDto registerDto) {
        // поиск пользователя
        // запрос в userService на создание нового
        String passwordHash = passwordEncoder.encode(registerDto.getPassword());
        UserDto createUserDto = new UserDto();
        createUserDto.setName(registerDto.getUsername());
        UserDto userDto = userServiceClient.createUser(createUserDto);
        log.info("Created user: {}", userDto);
        return tokenService.generateToken(userDto);
    }
}
