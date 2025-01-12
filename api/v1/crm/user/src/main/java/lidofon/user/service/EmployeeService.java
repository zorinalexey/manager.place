package lidofon.user.service;


import lidofon.user.dto.EmployeeDto;
import org.springframework.http.ResponseEntity;

public interface EmployeeService {
    ResponseEntity<EmployeeDto> createEmployee(EmployeeDto userDto);
    ResponseEntity<EmployeeDto> updateEmployee(String id,EmployeeDto updateEmployeeDto);
    ResponseEntity<EmployeeDto> getEmployee(String id);
}
