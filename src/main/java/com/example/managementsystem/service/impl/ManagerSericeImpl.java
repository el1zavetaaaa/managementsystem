package com.example.managementsystem.service.impl;

import com.example.managementsystem.entity.Employee;
import com.example.managementsystem.entity.Manager;
import com.example.managementsystem.exception.ResourceNotFoundException;
import com.example.managementsystem.repository.ManagerRepository;
import com.example.managementsystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ManagerSericeImpl implements ManagerService{

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public Manager saveManager(Manager manager) {
        return managerRepository.save(manager);
    }

    @Override
    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    @Override
    public Manager getManagerById(Long id) {
        return managerRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Manager", "Id", id));
    }

    @Override
    public Manager updateManager(Manager manager, Long id) {
        Manager existingManager = managerRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Manager","Id",id));
        existingManager.setName(manager.getName());
        managerRepository.save(existingManager);
        return existingManager;
    }

    @Override
    public void deleteManager(Long id) {
        managerRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Manager","Id",id));
        managerRepository.deleteById(id);
    }

    @Transactional
    public Manager addEmployeeToManager(Long managerId, Long employeeId){
        Manager existingManager = managerRepository.findById(managerId).orElseThrow(() ->
                new ResourceNotFoundException("Manager","Id",managerId));
        Employee existingEmployee = employeeService.getEmployeeById(employeeId);
        existingManager.addEmployee(existingEmployee);
        return existingManager;
    }

    @Transactional
    public Manager removeEmployee(Long managerId, Long employeeId) {
        Manager existingManager = managerRepository.findById(managerId).orElseThrow(() ->
                new ResourceNotFoundException("Manager","Id",managerId));
        Employee existingEmployee = employeeService.getEmployeeById(employeeId);
        existingManager.removeEmployee(existingEmployee);
        return existingManager;
    }


}
