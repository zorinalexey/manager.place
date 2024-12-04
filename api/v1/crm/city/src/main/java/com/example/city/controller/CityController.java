package com.example.city.controller;


import com.example.city.dto.CityListDto;
import com.example.city.model.City;
import com.example.city.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/city/")
@RequiredArgsConstructor
@Slf4j
public class CityController {

    private final CityService cityService;
    @PutMapping("/")
    public Page<City> getCityList(@RequestBody CityListDto cityListDto) {
        return cityService.getCities(cityListDto);
    }
}
