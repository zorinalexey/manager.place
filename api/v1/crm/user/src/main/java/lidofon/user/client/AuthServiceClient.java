package lidofon.user.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "user-client",url = "${auth.service.url}")
public interface AuthServiceClient {

}
