package com.example.managementsystem.model.request;

import javax.validation.constraints.NotBlank;

public record SaveEmployeeRequest(@NotBlank(message = "first name must not be blank") String firstName,
                                  @NotBlank(message = "last name must not be blank") String lastName,
                                  @NotBlank(message = "email must not be blank") String email) {
}
