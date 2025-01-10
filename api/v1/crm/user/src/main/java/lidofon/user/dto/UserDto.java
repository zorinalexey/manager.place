package lidofon.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class UserDto {
    private String id;
    @NotBlank
    @NotNull
    private String name;
}
