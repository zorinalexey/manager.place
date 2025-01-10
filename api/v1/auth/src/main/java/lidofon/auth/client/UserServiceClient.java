package lidofon.auth.client;

import lidofon.auth.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "user-client",url = "${user.service.url}")
public interface UserServiceClient {
    @PostMapping("/create")
    UserDto createUser(@RequestBody UserDto userDto);
}
