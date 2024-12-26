package lidofon.country.dto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCountryDto {
    @NotNull
    private String name;
}
