package lidofon.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data

@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String id;
    @NotBlank
    @NotNull
    private String name;
    @NotBlank
    @NotNull
    private String middleName;
    @NotBlank
    @NotNull
    private String lastName;
    @NotBlank
    @NotNull
    private String login;
    @NotBlank
    @NotNull
    @Email
    private String email;
    @NotBlank
    @NotNull
    private LocalDateTime dateOfBirth;
    @NotBlank
    @NotNull
    private String virtualNumber;
    @NotBlank
    @NotNull
    private String phone;
    @NotBlank
    @NotNull
    private String operatorGroup;
    @NotBlank
    @NotNull
    private String externalId;
    @NotBlank
    @NotNull
    private String roleId;
    @NotBlank
    @NotNull
    private String cityId;
    @NotBlank
    @NotNull
    private String regionId;
    @NotBlank
    @NotNull
    private String countryId;
    @NotBlank
    @NotNull
    private boolean isBlocked;
    @NotBlank
    @NotNull
    private String status;
    @NotBlank
    @NotNull
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
