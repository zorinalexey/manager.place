package lidofon.user.controller;

import lidofon.user.dto.UserDto;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    @GetMapping("/")
    public String index() {
        return "Hello World";
    }
    @PostMapping("/create")
    public UserDto create(@RequestBody UserDto userDto) {
        String id = UUID.randomUUID().toString();
        userDto.setId(id);
        userDto.setName(userDto.getName().toLowerCase());
        return userDto;
    }
}
