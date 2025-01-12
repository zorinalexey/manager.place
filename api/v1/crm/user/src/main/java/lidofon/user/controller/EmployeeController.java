package lidofon.user.controller;

import lidofon.user.dto.EmployeeDto;
import lidofon.user.service.EmployeeService;
import lidofon.user.validator.EmployeeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeValidator employeeValidator;
    @GetMapping("/")
    public String index() {
        return "Hello World";
    }
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable UUID id) {
        return employeeService.getEmployee(id.toString());
    }
    @PostMapping("/create")
    public ResponseEntity<EmployeeDto> create(@RequestBody EmployeeDto userDto) {
        return employeeService.createEmployee(userDto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> update(@PathVariable String id, @RequestBody EmployeeDto userDto) {
        employeeValidator.editEmployeeValidation(id);
        return employeeService.updateEmployee(id,userDto);
    }

}
