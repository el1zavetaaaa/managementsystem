package com.example.managementsystem.service;

import com.example.managementsystem.model.entity.Manager;
import com.example.managementsystem.model.request.SaveManagerRequest;
import com.example.managementsystem.model.response.ManagerResponse;

import java.util.List;
import java.util.Optional;

public interface ManagerService {
    ManagerResponse saveManager(SaveManagerRequest saveManagerRequest);

    List<Manager> getAllManagers();

    Optional<ManagerResponse> getManagerById(Long id);

    void updateManager(Long id, SaveManagerRequest saveManagerRequest);

    Optional<ManagerResponse> deleteManager(Long id);

    Manager addEmployeeToManager(Long managerId, Long employeeId);

    Manager removeEmployee(Long managerId, Long employeeId);
}
