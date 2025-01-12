package lidofon.user.mapper;

import lidofon.user.dto.EmployeeDto;
import lidofon.user.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {
//    @Mappings({
//            @Mapping(target = "id", source = "id"),
//            @Mapping(target = "name", source = "name"),
//            @Mapping(target = "middleName", source = "middleName"),
//            @Mapping(target = "lastName", source = "lastName"),
//            @Mapping(target = "email", source = "email"),
//            @Mapping(target = "login", source = "login"),
//            @Mapping(target = "dateOfBirth", source = "dateOfBirth"),
//            @Mapping(target = "virtualNumber", source = "virtualNumber"),
//            @Mapping(target = "phone", source = "phone"),
//            @Mapping(target = "operatorGroup", source = "operatorGroup"),
//            @Mapping(target = "externalId", source = "externalId"),
//            @Mapping(target = "roleId", source = "roleId"),
//            @Mapping(target = "cityId", source = "cityId"),
//            @Mapping(target = "regionId", source = "regionId"),
//            @Mapping(target = "countryId", source = "countryId"),
//            @Mapping(target = "isBlocked", source = "isBlocked"),
//            @Mapping(target = "status", source = "status"),
//            @Mapping(target = "createdAt", source = "createdAt"),
//            @Mapping(target = "updatedAt", source = "updatedAt"),
//    })
    EmployeeDto toDto(Employee employee);

//    @Mappings({
//            @Mapping(target = "id", source = "id"),
//            @Mapping(target = "name", source = "name"),
//            @Mapping(target = "middleName", source = "middleName"),
//            @Mapping(target = "lastName", source = "lastName"),
//            @Mapping(target = "email", source = "email"),
//            @Mapping(target = "login", source = "login"),
//            @Mapping(target = "dateOfBirth", source = "dateOfBirth"),
//            @Mapping(target = "virtualNumber", source = "virtualNumber"),
//            @Mapping(target = "phone", source = "phone"),
//            @Mapping(target = "operatorGroup", source = "operatorGroup"),
//            @Mapping(target = "externalId", source = "externalId"),
//            @Mapping(target = "roleId", source = "roleId"),
//            @Mapping(target = "cityId", source = "cityId"),
//            @Mapping(target = "regionId", source = "regionId"),
//            @Mapping(target = "countryId", source = "countryId"),
//            @Mapping(target = "isBlocked", source = "isBlocked"),
//            @Mapping(target = "status", source = "status"),
//            @Mapping(target = "password", source = "password"),
//    })
    Employee toEntity(EmployeeDto userDto);
}
