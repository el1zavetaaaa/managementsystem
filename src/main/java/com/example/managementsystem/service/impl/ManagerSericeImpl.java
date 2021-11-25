package com.example.managementsystem.service.impl;

import com.example.managementsystem.model.entity.Employee;
import com.example.managementsystem.model.entity.Manager;
import com.example.managementsystem.model.request.SaveManagerRequest;
import com.example.managementsystem.model.response.ManagerResponse;
import com.example.managementsystem.repository.EmployeeRepository;
import com.example.managementsystem.repository.ManagerRepository;
import com.example.managementsystem.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.managementsystem.exception.ResourceNotFoundException.objectNotFound;

@Service
public class ManagerSericeImpl implements ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    public ManagerSericeImpl(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ManagerResponse saveManager(SaveManagerRequest saveManagerRequest) {
        var manager = new Manager(saveManagerRequest.name());
        return ManagerResponse.fromManager(managerRepository.save(manager));
    }

    @Override
    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    @Override
    public Optional<ManagerResponse> getManagerById(Long id) {
        return managerRepository.findById(id).map(ManagerResponse::fromManager);
    }

    @Override
    public void updateManager(Long id, SaveManagerRequest request) {
        var existingManager = managerRepository.findById(id).orElseThrow(() ->
                objectNotFound(id));
        existingManager.setName(request.name());
        managerRepository.save(existingManager);
    }

    @Override
    public Optional<ManagerResponse> deleteManager(Long id) {
        var manager = managerRepository.findById(id);
        manager.ifPresent(managerRepository::delete);
        return manager.map(ManagerResponse::fromManager);
    }

    @Transactional
    public Manager addEmployeeToManager(Long managerId, Long employeeId) {
        Manager existingManager = managerRepository.findById(managerId).orElseThrow(() ->
                objectNotFound(managerId));
        Employee existingEmployee = employeeRepository.findById(employeeId).orElseThrow(() ->
                objectNotFound(employeeId));
        existingManager.addEmployee(existingEmployee);
        return existingManager;
    }


    @Transactional
    public Manager removeEmployee(Long managerId, Long employeeId) {
        Manager existingManager = managerRepository.findById(managerId).orElseThrow(() ->
                objectNotFound(managerId));
        Employee existingEmployee = employeeRepository.findById(employeeId).orElseThrow(() ->
                objectNotFound(employeeId));
        existingManager.removeEmployee(existingEmployee);
        return existingManager;
    }

}
