package com.example.managementsystem.model.response;

import com.example.managementsystem.model.entity.Manager;

public record ManagerResponse(Long id, String name) {


    public static ManagerResponse fromManager(Manager manager) {


        return new ManagerResponse(
                manager.getId(),
                manager.getName()
        );


    }
}
