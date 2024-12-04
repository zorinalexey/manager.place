package com.example.city.service;

import com.example.city.dto.CityListDto;
import com.example.city.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CityService {
    Page<City> getCities(CityListDto cityListDto);
}
