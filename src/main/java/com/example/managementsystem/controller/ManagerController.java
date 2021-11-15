package com.example.managementsystem.controller;

import com.example.managementsystem.entity.Manager;
import com.example.managementsystem.entity.ManagerResponse;
import com.example.managementsystem.entity.dto.ManagerDto;
import com.example.managementsystem.entity.SaveManagerRequest;
import com.example.managementsystem.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.managementsystem.exception.ResourceNotFoundException.objectNotFound;

@RestController
@RequestMapping("/managers")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public ManagerResponse createAndSaveManager(@Valid @RequestBody SaveManagerRequest request) {
        return managerService.saveManager(request);
    }

    @GetMapping()
    public ResponseEntity<List<ManagerDto>> getAllManagers() {
        List<Manager> managers = managerService.getAllManagers();
        List<ManagerDto> managersDto = managers.stream().map(ManagerDto::printManagerFromDb).collect(Collectors.toList());
        return new ResponseEntity<>(managersDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ManagerResponse getManagerById(@PathVariable final Long id) {
        return managerService.getManagerById(id).orElseThrow(() -> objectNotFound(id));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateManager(@PathVariable("id") Long id,
                              @Valid @RequestBody SaveManagerRequest request) {
        managerService.updateManager(id, request);
    }

    @DeleteMapping("/{id}")
    public ManagerResponse deleteManager(@PathVariable("id") Long id) {
        return managerService.deleteManager(id).orElseThrow(() -> objectNotFound(id));
    }

    @PostMapping(value = "/{managerId}/employees/{emploeeId}/add")
    public ResponseEntity<ManagerDto> addItemToCart(@PathVariable final Long managerId,
                                                    @PathVariable final Long emploeeId) {
        Manager manager = managerService.addEmployeeToManager(managerId, emploeeId);
        return new ResponseEntity<>(ManagerDto.printManagerFromDb(manager), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{managerId}/employees/{emploeeId}/remove")
    public ResponseEntity<ManagerDto> removeEmployeeFromManager(@PathVariable final Long managerId,
                                                                @PathVariable final Long emploeeId) {
        Manager manager = managerService.removeEmployee(managerId, emploeeId);
        return new ResponseEntity<>(ManagerDto.printManagerFromDb(manager), HttpStatus.OK);
    }
}
