package com.example.managementsystem.controller;

import com.example.managementsystem.model.dto.EmployeeDto;
import com.example.managementsystem.model.entity.Employee;
import com.example.managementsystem.model.request.SaveEmployeeRequest;
import com.example.managementsystem.model.response.EmployeeResponse;
import com.example.managementsystem.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.managementsystem.exception.ResourceNotFoundException.objectNotFound;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeResponse createAndSaveEmployee(@RequestBody SaveEmployeeRequest request) {
        log.info("Employee with first name {}, last name {} and email {} was successfully created!",
                request.firstName(),request.lastName(),request.email());
        return employeeService.saveEmployee(request);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        List<EmployeeDto> employeesDto = employees.stream().map(EmployeeDto::printEmployee).collect(Collectors.toList());
        return new ResponseEntity<>(employeesDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeResponse getEmployeeById(@PathVariable final Long id) {
        return employeeService.getEmployeeById(id).orElseThrow(() -> objectNotFound(id));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateEmployee(@PathVariable("id") Long id,
                               @Valid @RequestBody SaveEmployeeRequest request) {
        log.info("Employee with id {} was successfully updated! Now his/her new first name is {}, last name is {}, email is {}!",
                id,request.firstName(),request.lastName(),request.email());
        employeeService.updateEmployee(id, request);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeResponse deleteEmployee(@PathVariable("id") Long id) {
        log.warn("Employee with id {} was deleted.",id);
        return employeeService.deleteEmployee(id).orElseThrow(() -> objectNotFound(id));
    }


}
