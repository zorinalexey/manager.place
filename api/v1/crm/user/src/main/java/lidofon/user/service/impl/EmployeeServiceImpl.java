package lidofon.user.service.impl;

import lidofon.user.dto.EmployeeDto;
import lidofon.user.entity.Employee;
import lidofon.user.exception.EmployeeNotFoundException;
import lidofon.user.mapper.EmployeeMapper;
import lidofon.user.repository.EmployeeRepository;
import lidofon.user.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public ResponseEntity<EmployeeDto> updateEmployee(String id, EmployeeDto updateEmployeeDto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Not found Employee with id: " + id));
        log.info("Update Employee {} ", employee);
        log.info("Update Employee id {} ", employee.getIntId());

        Employee editedEmployee= employeeMapper.toEntity(updateEmployeeDto);
        editedEmployee.setId(id);
        editedEmployee.setIntId(employee.getIntId());
        return ResponseEntity.ok(employeeMapper.toDto(employeeRepository.save(editedEmployee)));
    }

    @Override
    public ResponseEntity<EmployeeDto> createEmployee(EmployeeDto userDto) {
        String id = UUID.randomUUID().toString();
        userDto.setId(id);
        log.info("Create employee : {}", userDto);
        Employee employee = employeeMapper.toEntity(userDto);
        Long intId = generateEmployeeIntId();
        log.info("Employee created with id {}", intId);
        log.info("Employee created ent {}", employee);
        employee.setIntId(intId);
        employeeRepository.save(employee);
        return ResponseEntity.ok(employeeMapper.toDto(employee));
    }

    @Override
    public ResponseEntity<EmployeeDto> getEmployee(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(()-> new EmployeeNotFoundException("Employee Not Found"));
        return ResponseEntity.ok(employeeMapper.toDto(employee));
    }
    private Long generateEmployeeIntId() {

        Long maxId = employeeRepository
                .getMaxId()
                .orElseThrow(()-> new EmployeeNotFoundException("Employee Not Found"));
        if(maxId == null){
            maxId = 1L;
        } else {
            maxId++;
        }
        return maxId;
    }
}
