package com.example.city.service.impl;


import com.example.city.dto.CityListDto;
import com.example.city.model.City;
import com.example.city.repository.CityRepository;
import com.example.city.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    @Override
    public Page<City> getCities(CityListDto cityListDto) {
        PageRequest pageRequest = PageRequest.of(cityListDto.getPage(), cityListDto.getPerPage());
        Page<City> cities = cityRepository.findAll(pageRequest);
        log.info("Paginated result: {}", cities.getContent());
        return cities;
    }

}
