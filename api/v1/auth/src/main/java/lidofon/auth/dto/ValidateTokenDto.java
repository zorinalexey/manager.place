package lidofon.auth.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class ValidateTokenDto {
    @NotNull
    private String token;
}
