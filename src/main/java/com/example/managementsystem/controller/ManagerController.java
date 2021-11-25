package com.example.managementsystem.controller;

import com.example.managementsystem.model.dto.ManagerDto;
import com.example.managementsystem.model.entity.Manager;
import com.example.managementsystem.model.request.SaveManagerRequest;
import com.example.managementsystem.model.response.ManagerResponse;
import com.example.managementsystem.service.ManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.managementsystem.exception.ResourceNotFoundException.objectNotFound;

@RestController
@RequestMapping("/managers")
public class ManagerController {

    private static final Logger log = LoggerFactory.getLogger(ManagerController.class);

    @Autowired
    private ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ManagerResponse createAndSaveManager(@Valid @RequestBody SaveManagerRequest request) {
        log.info("Manager wit name {} was successfully created!", request.name());
        return managerService.saveManager(request);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ManagerDto>> getAllManagers() {
        List<Manager> managers = managerService.getAllManagers();
        List<ManagerDto> managersDto = managers.stream().map(ManagerDto::printManagerFromDb).collect(Collectors.toList());
        return new ResponseEntity<>(managersDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ManagerResponse getManagerById(@PathVariable final Long id) {
        return managerService.getManagerById(id).orElseThrow(() -> objectNotFound(id));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateManager(@PathVariable("id") Long id,
                              @Valid @RequestBody SaveManagerRequest request) {
        log.info("Manager with id {} was successfully updated! Now his/her new name is {}", id, request.name());
        managerService.updateManager(id, request);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ManagerResponse deleteManager(@PathVariable("id") Long id) {
        log.warn("Manager with id {} was deleted.", id);
        return managerService.deleteManager(id).orElseThrow(() -> objectNotFound(id));
    }

    @PostMapping(value = "/{managerId}/employees/{employeeId}/add")
    public ResponseEntity<ManagerDto> addEmployeeToManager(@PathVariable final Long managerId,
                                                           @PathVariable final Long employeeId) {
        Manager manager = managerService.addEmployeeToManager(managerId, employeeId);
        log.info("New employee with id {} was added to manager with id {} and name {}",
                employeeId, managerId, manager.getName());
        return new ResponseEntity<>(ManagerDto.printManagerFromDb(manager), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{managerId}/employees/{employeeId}/remove")
    public void removeEmployeeFromManager(@PathVariable final Long managerId,
                                          @PathVariable final Long employeeId) {
        Manager manager = managerService.removeEmployee(managerId, employeeId);
        log.warn("Employee with id {} was removed from manager with id {} and name {}.",
                employeeId, managerId, manager.getName());
    }
}
