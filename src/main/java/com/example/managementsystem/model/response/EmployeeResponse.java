package com.example.managementsystem.model.response;

import com.example.managementsystem.model.entity.Employee;

public record EmployeeResponse(Long id, String firstName, String lastName, String email) {

    public static EmployeeResponse fromEmployee(Employee employee) {


        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail()
        );


    }
}
