package com.example.managementsystem.controller;

import com.example.managementsystem.entity.Employee;
import com.example.managementsystem.entity.dto.EmployeeDto;
import com.example.managementsystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/createandsaveemployees")
    public ResponseEntity<Employee> createAndSaveEmployee( @RequestBody  Employee employee){
        return new ResponseEntity<Employee>(employeeService.saveEmployee(employee), HttpStatus.CREATED);
    }

    @GetMapping("/getallemployees")
    public ResponseEntity<List<EmployeeDto>> getAllManagers(){
        List<Employee> employees = employeeService.getAllEmployees();
        List<EmployeeDto> employeesDto = employees .stream().map(EmployeeDto:: printEmployee).collect(Collectors.toList());
        return new ResponseEntity<>(employeesDto, HttpStatus.OK);
    }

    @GetMapping("/getemployeebyid/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable final Long id){
        Employee employee = employeeService.getEmployeeById(id);
        return new ResponseEntity<>(EmployeeDto.printEmployee(employee), HttpStatus.OK);
    }

    @PutMapping("/updateemployee/{id}")
    public ResponseEntity<Employee> updateEmployee( @PathVariable("id") Long id,
                                                    @RequestBody Employee employee){
        return new ResponseEntity<Employee>(employeeService.updateEmployee(employee,id), HttpStatus.OK);
    }

    @DeleteMapping("/deleteemployee/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long id){
        employeeService.deleteEmployee(id);
        return new ResponseEntity<String>("Employee was successfully delete!",HttpStatus.OK);
    }

}
