package com.example.city.controller;


import com.example.city.dto.CityDto;
import com.example.city.dto.CityListDto;
import com.example.city.model.City;
import com.example.city.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public CityDto createCity(@RequestBody CityDto cityDto) {
        return cityService.createCity(cityDto);
    }
    @GetMapping("/{id}")
    public CityDto getCity(@PathVariable UUID id) {
        return cityService.getCity(id);
    }

    @PutMapping("/{id}")
    public CityDto updateCity(@RequestBody CityDto cityDto) {
        return cityService.editCity(cityDto);
    }
    @DeleteMapping("/soft/{id}")
    public void softDeleteCity(@PathVariable UUID id) {
        cityService.softDeleteCity(id);
    }
    @DeleteMapping("/hard/{id}")
    public void hardDeleteCity(@PathVariable UUID id) {
        cityService.hardDeleteCity(id);
    }
}
