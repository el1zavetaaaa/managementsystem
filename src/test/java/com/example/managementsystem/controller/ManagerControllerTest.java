package com.example.managementsystem.controller;

import com.example.managementsystem.model.request.SaveManagerRequest;
import com.example.managementsystem.model.response.ManagerResponse;
import com.example.managementsystem.service.ManagerService;
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


public class ManagerControllerTest {

    private MockMvc mockMvc;
    private ManagerService managerService;
    Random random = new Random();

    @BeforeEach
    void setUp() {
        managerService = mock(ManagerService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ManagerController(managerService))
                .build();
    }

    @Test
    void testCreateManager() throws Exception {
        var request = new SaveManagerRequest("name");
        var id = random.nextLong();
        var response = new ManagerResponse(id, "name");

        when(managerService.saveManager(request)).thenReturn(response);

        var expectedJson = """
                {"id":%d,"name":"name"}
                """.formatted(id);

        mockMvc.perform(post("/managers")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                {"name":"name"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(expectedJson));

        verify(managerService, only()).saveManager(request);
    }

    @Test
    void testGetManagerById() throws Exception {
        var id = random.nextLong();
        var response = new ManagerResponse(id, "name");

        when(managerService.getManagerById(id)).thenReturn(Optional.of(response));

        var expectedJson = """
                {"id":%d,"name":"name"}
                """.formatted(id);

        mockMvc.perform(get("/managers/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(managerService, only()).getManagerById(id);
    }

    @Test
    void testGetManagerByAbsentId() throws Exception {
        var id = random.nextLong();

        when(managerService.getManagerById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/managers/{id}", id))
                .andExpect(status().isNotFound());

        verify(managerService, only()).getManagerById(id);
    }


    @Test
    void testUpdateManager() throws Exception {
        var request = new SaveManagerRequest("name");
        var id = random.nextLong();

        mockMvc.perform(put("/managers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"name"}
                                """))
                .andExpect(status().isNoContent());

        verify(managerService, only()).updateManager(id, request);
    }

    @Test
    void testDeleteManager() throws Exception {
        var id = random.nextLong();
        var response = new ManagerResponse(id, "name");

        var expectedJson = """
                {"id":%d,"name":"name"}
                """.formatted(id);

        when(managerService.deleteManager(id))
                .thenReturn(Optional.of(response))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/managers/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        mockMvc.perform(delete("/managers/{id}", id))
                .andExpect(status().isNotFound());

        verify(managerService, times(2)).deleteManager(id);
        verifyNoMoreInteractions(managerService);
    }


}
