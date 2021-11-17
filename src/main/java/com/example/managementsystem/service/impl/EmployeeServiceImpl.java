package com.example.managementsystem.service.impl;

import com.example.managementsystem.entity.*;
import com.example.managementsystem.entity.EmployeeResponse;
import com.example.managementsystem.exception.ResourceNotFoundException;
import com.example.managementsystem.repository.EmployeeRepository;
import com.example.managementsystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.managementsystem.exception.ResourceNotFoundException.objectNotFound;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public EmployeeResponse saveEmployee(SaveEmployeeRequest request) {
        var employee = new Employee(request.firstName(),request.lastName(),request.email());
        return EmployeeResponse.fromEmployee(employeeRepository.save(employee));
    }
    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<EmployeeResponse> getEmployeeById(Long id) {
        return employeeRepository.findById(id).map(EmployeeResponse::fromEmployee);
    }

    @Override
    public void updateEmployee(Long id, SaveEmployeeRequest request) {
        var existingEmployee = employeeRepository.findById(id).orElseThrow(() ->
                objectNotFound(id));
        existingEmployee.setFirstName(request.firstName());
        existingEmployee.setLastName(request.lastName());
        existingEmployee.setEmail(request.email());
        employeeRepository.save(existingEmployee);
    }

    @Override
    public Optional<EmployeeResponse> deleteEmployee(Long id) {
        var employee = employeeRepository.findById(id);
        employee.ifPresent(employeeRepository::delete);
        return employee.map(EmployeeResponse::fromEmployee);
    }
}
