package lidofon.user.service.impl;

import lidofon.user.dto.EmployeeDto;
import lidofon.user.entity.Employee;
import lidofon.user.exception.EmployeeNotFoundException;
import lidofon.user.mapper.EmployeeMapper;
import lidofon.user.repository.EmployeeRepository;
import lidofon.user.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    @Override
    public ResponseEntity<EmployeeDto> createEmployee(EmployeeDto userDto) {
        String id = UUID.randomUUID().toString();
        userDto.setId(id);
        Employee employee = employeeMapper.toEntity(userDto);
        employeeRepository.save(employee);
        return ResponseEntity.ok(employeeMapper.toDto(employee));
    }

    @Override
    public ResponseEntity<EmployeeDto> getEmployee(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(()-> new EmployeeNotFoundException("Employee Not Found"));
        return ResponseEntity.ok(employeeMapper.toDto(employee));
    }
}
