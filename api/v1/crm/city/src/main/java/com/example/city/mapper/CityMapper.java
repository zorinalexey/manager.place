package com.example.city.mapper;

import com.example.city.dto.CityDto;
import com.example.city.model.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface CityMapper {
    City toEntity(CityDto cityDto);
    CityDto toDto(City city);
}
