package com.example.managementsystem.controller;

import com.example.managementsystem.entity.Manager;
import com.example.managementsystem.entity.dto.ManagerDto;
import com.example.managementsystem.service.impl.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @PostMapping("/createandsavemanagers")
    public ResponseEntity<Manager> createAndSaveManager(@RequestBody Manager manager){
        return new ResponseEntity<Manager>(managerService.saveManager(manager), HttpStatus.CREATED);
    }

    @GetMapping("/getallmanagers")
    public ResponseEntity<List<ManagerDto>> getAllManagers(){
        List<Manager> managers = managerService.getAllManagers();
        List<ManagerDto> managersDto = managers.stream().map(ManagerDto:: printManagerFromDb).collect(Collectors.toList());
        return new ResponseEntity<>(managersDto, HttpStatus.OK);
    }

    @GetMapping("/getmanagerbyid/{id}")
    public ResponseEntity<ManagerDto> getManagerById(@PathVariable final Long id){
        Manager manager = managerService.getManagerById(id);
        return new ResponseEntity<>(ManagerDto.printManagerFromDb(manager), HttpStatus.OK);
    }

    @PutMapping("/updatmanager/{id}")
    public ResponseEntity<Manager> updateManager( @PathVariable("id") Long id,
                                                    @RequestBody Manager manager){
        return new ResponseEntity<Manager>(managerService.updateManager(manager,id), HttpStatus.OK);
    }

    @DeleteMapping("/deletmanager/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long id){
        managerService.deleteManager(id);
        return new ResponseEntity<String>("Manager was successfully delete!",HttpStatus.OK);
    }

    @PostMapping(value = "{managerId}/employees/{emploeeId}/add")
    public ResponseEntity<ManagerDto> addItemToCart(@PathVariable final Long managerId,
                                                    @PathVariable final Long emploeeId){
        Manager manager = managerService.addEmployeeToManager(managerId,emploeeId);
        return new ResponseEntity<>(ManagerDto.printManagerFromDb(manager), HttpStatus.OK);
    }

    @DeleteMapping(value = "{managerId}/employees/{emploeeId}/remove")
    public ResponseEntity<ManagerDto> removeItemFromCart(@PathVariable final Long managerId,
                                                      @PathVariable final Long emploeeId){
        Manager manager = managerService.removeEmployee(managerId, emploeeId);
        return new ResponseEntity<>(ManagerDto.printManagerFromDb(manager), HttpStatus.OK);
    }
}
