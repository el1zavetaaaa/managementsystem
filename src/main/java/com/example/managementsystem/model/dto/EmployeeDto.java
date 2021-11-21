package com.example.managementsystem.model.dto;

import com.example.managementsystem.model.entity.Employee;
import lombok.Data;

import java.util.Objects;

@Data
public class EmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private PlainManagerDto plainManagerDto;

    public static EmployeeDto printEmployee(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(employee.getId());
        employeeDto.setEmail(employee.getEmail());
        employeeDto.setFirstName(employee.getFirstName());
        employeeDto.setLastName(employee.getLastName());
        if (Objects.nonNull(employee.getManager())) {
            employeeDto.setPlainManagerDto(PlainManagerDto.printSimpleManager(employee.getManager()));
        }
        return employeeDto;
    }
}
