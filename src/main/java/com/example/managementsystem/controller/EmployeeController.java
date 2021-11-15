package com.example.managementsystem.controller;

import com.example.managementsystem.entity.Employee;
import com.example.managementsystem.entity.SaveEmployeeRequest;
import com.example.managementsystem.entity.dto.EmployeeDto;
import com.example.managementsystem.entity.EmployeeResponse;
import com.example.managementsystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.managementsystem.exception.ResourceNotFoundException.objectNotFound;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public EmployeeResponse createAndSaveEmployee(@RequestBody SaveEmployeeRequest request) {
        return employeeService.saveEmployee(request);
    }

    @GetMapping()
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        List<EmployeeDto> employeesDto = employees.stream().map(EmployeeDto::printEmployee).collect(Collectors.toList());
        return new ResponseEntity<>(employeesDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public EmployeeResponse getEmployeeById(@PathVariable final Long id) {
        return employeeService.getEmployeeById(id).orElseThrow(() -> objectNotFound(id));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateEmployee(@PathVariable("id") Long id,
                               @Valid @RequestBody SaveEmployeeRequest request) {
        employeeService.updateEmployee(id, request);
    }

    @DeleteMapping("/{id}")
    public EmployeeResponse deleteEmployee(@PathVariable("id") Long id) {
        return employeeService.deleteEmployee(id).orElseThrow(() -> objectNotFound(id));
    }

}
