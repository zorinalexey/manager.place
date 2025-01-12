package lidofon.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class EmployeeDto {
    private String id;
    private Long intId;
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
    private LocalDate dateOfBirth;
    @NotBlank
    @NotNull
    private String virtualNumber;
    @NotBlank
    @NotNull
    private String phone;
    @NotBlank
    @NotNull
    private String operatorGroupId;
    @NotBlank
    @NotNull
    private String externalId;
    @NotBlank
    @NotNull
    private String roleId;

    private String cityId;

    private String regionId;

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
