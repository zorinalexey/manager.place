package com.example.city.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CityDto {
    private UUID id;

    @NotNull(message = "in_id cannot be null")
    private Long intId;
    @NotNull(message = "name cannot be null")
    @NotEmpty(message = "name cannot be empty")
    private String name;
    @NotNull(message = "region id cannot be null")
    private String regionId;
    @NotNull(message = "country id cannot be null")
    private String countryId;
    @NotNull(message = "latitude id cannot be null")
    private Double latitude;
    @NotNull(message = "longitude id cannot be null")
    private Double longitude;
    @NotNull(message = "zoom id cannot be null")
    private Integer zoom;
}
