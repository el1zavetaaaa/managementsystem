package com.example.managementsystem.entity;

import javax.validation.constraints.NotBlank;

public record SaveManagerRequest(@NotBlank (message = "name must not be blank") String name) {
}
