package com.example.city.service.impl;


import com.example.city.dto.CityDto;
import com.example.city.dto.CityListDto;
import com.example.city.mapper.CityMapper;
import com.example.city.model.City;
import com.example.city.repository.CityRepository;
import com.example.city.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;
    @Override
    public Page<City> getCities(CityListDto cityListDto) {
        PageRequest pageRequest = PageRequest.of(cityListDto.getPage(), cityListDto.getPerPage());
        Page<City> cities = cityRepository.findAll(pageRequest);
        log.info("Paginated result: {}", cities.getContent());
        return cities;
    }
    //sort id int id name, region name,country name,timestamps
    //filter id int id name, region id,country id, timestamps
    // search id int id name, region id,country id,
    @Override
    public CityDto getCity(UUID id) {
        City city = getOneCity(id);
        return cityMapper.toDto(city);
    }



    @Override
    public CityDto createCity(CityDto cityDto) {
        City city = cityMapper.toEntity(cityDto);
        return cityMapper.toDto(cityRepository.save(city));
    }

    @Override
    public CityDto editCity(CityDto cityDto) {
        City city = getOneCity(cityDto.getId());
        City updatedCity = cityMapper.toEntity(cityDto);
        return cityMapper.toDto(cityRepository.save(updatedCity));
    }

    @Override
    public void softDeleteCity(UUID id) {
        City city = getOneCity(id);
        city.setDeletedAt(LocalDateTime.now());
        cityRepository.save(city);
    }

    @Override
    public void hardDeleteCity(UUID id) {
        City city = getOneCity(id);
        cityRepository.delete(city);
    }

    private City getOneCity(UUID id) {
        return cityRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}
