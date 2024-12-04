package com.example.city.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityListDto {
    private int page = 0;
    private int perPage = 10;
}
