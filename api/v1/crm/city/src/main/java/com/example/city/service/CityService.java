package com.example.city.service;

import com.example.city.dto.CityDto;
import com.example.city.dto.CityListDto;
import com.example.city.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CityService {
    Page<City> getCities(CityListDto cityListDto);
    CityDto getCity(UUID id);
    CityDto createCity(CityDto cityDto);
    CityDto editCity(CityDto cityDto);
    void softDeleteCity(UUID id);
    void hardDeleteCity(UUID id);
}
