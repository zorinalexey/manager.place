package lidofon.auth.controller;

import lidofon.auth.dto.RegisterDto;
import lidofon.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @GetMapping("/")
    public String index() {
        return "Hello World";
    }
    @PostMapping("/register")
    public String register(@RequestBody RegisterDto registerDto) {
        authService.register(registerDto);
        return "register success";
    }
}
