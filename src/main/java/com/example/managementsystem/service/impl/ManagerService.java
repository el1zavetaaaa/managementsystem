package com.example.managementsystem.service.impl;

import com.example.managementsystem.entity.Manager;

import java.util.List;

public interface ManagerService {
    Manager saveManager(Manager manager);
    List<Manager> getAllManagers();
    Manager getManagerById(Long id);
    Manager updateManager(Manager manager, Long id);
    void deleteManager(Long id);
    Manager addEmployeeToManager(Long managerId, Long employeeId);
    Manager removeEmployee(Long managerId, Long employeeId);
}
