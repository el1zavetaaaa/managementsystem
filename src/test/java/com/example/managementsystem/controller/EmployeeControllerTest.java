package com.example.managementsystem.controller;

import com.example.managementsystem.model.request.SaveEmployeeRequest;
import com.example.managementsystem.model.response.EmployeeResponse;
import com.example.managementsystem.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;
import java.util.Random;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeControllerTest {

    private MockMvc mockMvc;
    private EmployeeService employeeService;
    Random random = new Random();

    @BeforeEach
    void setUp() {
        employeeService = mock(EmployeeService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new EmployeeController(employeeService))
                .build();
    }

    @Test
    void testCreateEmployee() throws Exception {
        var request = new SaveEmployeeRequest("firstName", "lastName", "email");
        var id = random.nextLong();
        var response = new EmployeeResponse(id, "firstName", "lastName", "email");

        when(employeeService.saveEmployee(request)).thenReturn(response);

        var expectedJson = """
                {"id":%d,"firstName":"firstName","lastName":"lastName","email":"email"}
                """.formatted(id);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                {"firstName":"firstName","lastName":"lastName","email":"email"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(expectedJson));

        verify(employeeService, only()).saveEmployee(request);
    }

    @Test
    void testGetEmployeeById() throws Exception {
        var id = random.nextLong();
        var response = new EmployeeResponse(id, "firstName", "lastName", "email");

        when(employeeService.getEmployeeById(id)).thenReturn(Optional.of(response));

        var expectedJson = """
                {"id":%d,"firstName":"firstName","lastName":"lastName","email":"email"}
                """.formatted(id);

        mockMvc.perform(get("/employees/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(employeeService, only()).getEmployeeById(id);
    }

    @Test
    void testGetEmployeeByAbsentId() throws Exception {
        var id = random.nextLong();

        when(employeeService.getEmployeeById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/employees/{id}", id))
                .andExpect(status().isNotFound());

        verify(employeeService, only()).getEmployeeById(id);
    }

    @Test
    void testUpdateEmployee() throws Exception {
        var request = new SaveEmployeeRequest("firstName", "lastName", "email");
        var id = random.nextLong();

        mockMvc.perform(put("/employees/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName":"firstName","lastName":"lastName","email":"email"}
                                """))
                .andExpect(status().isNoContent());

        verify(employeeService, only()).updateEmployee(id, request);
    }

    @Test
    void testDeleteEmployee() throws Exception {
        var id = random.nextLong();
        var response = new EmployeeResponse(id, "firstName", "lastName", "email");

        var expectedJson = """
                {"id":%d,"firstName":"firstName","lastName":"lastName","email":"email"}
                """.formatted(id);

        when(employeeService.deleteEmployee(id))
                .thenReturn(Optional.of(response))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/employees/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        mockMvc.perform(delete("/employees/{id}", id))
                .andExpect(status().isNotFound());

        verify(employeeService, times(2)).deleteEmployee(id);
        verifyNoMoreInteractions(employeeService);
    }


}
