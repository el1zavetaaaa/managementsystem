package com.example.managementsystem.service;

import com.example.managementsystem.entity.Employee;
import com.example.managementsystem.entity.SaveEmployeeRequest;
import com.example.managementsystem.entity.EmployeeResponse;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    EmployeeResponse saveEmployee(SaveEmployeeRequest saveEmployeeRequest);
    List<Employee> getAllEmployees();
    Optional <EmployeeResponse> getEmployeeById(Long id);
    void updateEmployee(Long id, SaveEmployeeRequest saveEmployeeRequest);
    Optional<EmployeeResponse> deleteEmployee(Long id);
}
