package com.example.managementsystem.model.request;

import javax.validation.constraints.NotBlank;

public record SaveManagerRequest(@NotBlank(message = "name must not be blank") String name) {
}
