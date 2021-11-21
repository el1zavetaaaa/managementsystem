package com.example.managementsystem.model.dto;

import com.example.managementsystem.model.entity.Manager;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ManagerDto {
    private Long id;
    private String name;
    private List<EmployeeDto> employeeDtoList = new ArrayList<>();

    public static ManagerDto printManagerFromDb(Manager manager) {
        ManagerDto managerDto = new ManagerDto();
        managerDto.setId(manager.getId());
        managerDto.setName(manager.getName());
        managerDto.setEmployeeDtoList(manager.getEmployees().stream()
                .map(EmployeeDto::printEmployee).collect(Collectors.toList()));
        return managerDto;
    }
}
