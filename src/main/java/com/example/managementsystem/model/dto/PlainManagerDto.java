package com.example.managementsystem.model.dto;

import com.example.managementsystem.model.entity.Manager;
import lombok.Data;

@Data
public class PlainManagerDto {
    private Long id;
    private String name;


    public static PlainManagerDto printSimpleManager(Manager manager) {
        PlainManagerDto plainManagerDto = new PlainManagerDto();
        plainManagerDto.setId(manager.getId());
        plainManagerDto.setName(manager.getName());
        return plainManagerDto;
    }
}
