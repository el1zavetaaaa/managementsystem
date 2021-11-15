package com.example.managementsystem.entity;

public record ManagerResponse(Long id, String name) {


    public static ManagerResponse fromManager(Manager manager) {


        return new ManagerResponse(
                manager.getId(),
                manager.getName()
        );


    }
}
