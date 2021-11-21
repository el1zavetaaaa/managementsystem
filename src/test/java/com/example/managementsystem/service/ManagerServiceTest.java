package com.example.managementsystem.service;

import com.example.managementsystem.model.entity.Manager;
import com.example.managementsystem.model.request.SaveManagerRequest;
import com.example.managementsystem.model.response.ManagerResponse;
import com.example.managementsystem.repository.ManagerRepository;
import com.example.managementsystem.service.impl.ManagerSericeImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.*;

public class ManagerServiceTest {

    private ManagerSericeImpl managerServiceImpl;
    private ManagerRepository managerRepository;
    Random random = new Random();

    @BeforeEach
    void setUp() {
        managerRepository = mock(ManagerRepository.class);
        managerServiceImpl = new ManagerSericeImpl(managerRepository);
    }

    @Test
    void testCreateManager() {
        var request = new SaveManagerRequest("name");
        var managerId = random.nextLong();

        when(managerRepository.save(notNull())).thenAnswer(invocation -> {
            Manager entity = invocation.getArgument(0);
            assertThat(entity.getId()).isNull();
            assertThat(entity.getName()).isEqualTo(request.name());
            entity.setId(managerId);
            return entity;
        });

        ManagerResponse response = managerServiceImpl.saveManager(request);

        assertThat(response.id()).isEqualTo(managerId);
        assertThat(response.name()).isEqualTo(request.name());
        verify(managerRepository, only()).save(notNull());
    }

    @Test
    void testGetManagerById() {
        var absentId = random.nextLong();
        var presentId = random.nextLong();
        var manager = new Manager(presentId, "name");

        when(managerRepository.findById(absentId)).thenReturn(Optional.empty());
        when(managerRepository.findById(presentId)).thenReturn(Optional.of(manager));

        Optional<ManagerResponse> absentResponse = managerServiceImpl.getManagerById(absentId);

        assertThat(absentResponse).isEmpty();
        verify(managerRepository).findById(absentId);

        Optional<ManagerResponse> presentResponse = managerServiceImpl.getManagerById(presentId);

        assertThat(presentResponse).hasValueSatisfying(managerResponse ->
                assertManagerMatchesResponse(manager, managerResponse));
        verify(managerRepository).findById(presentId);

        verifyNoMoreInteractions(managerRepository);
    }

    @Test
    void testUpdateManager() {
        var presentId = random.nextLong();
        var absentId = random.nextLong();
        var update = new SaveManagerRequest("new name");

        var manager = new Manager(presentId, "name");

        when(managerRepository.findById(absentId)).thenReturn(Optional.empty());
        when(managerRepository.findById(presentId)).thenReturn(Optional.of(manager));
        when(managerRepository.save(same(manager))).thenReturn(manager);

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> managerServiceImpl.updateManager(absentId, update))
                .satisfies(e -> assertThat(e.getStatus()).isSameAs(HttpStatus.NOT_FOUND));

        verify(managerRepository).findById(absentId);

        managerServiceImpl.updateManager(presentId, update);

        assertThat(manager.getName()).isEqualTo(update.name());

        verify(managerRepository).findById(presentId);
        verify(managerRepository).save(same(manager));

        verifyNoMoreInteractions(managerRepository);
    }

    @Test
    void testDeleteManager() {
        var absentId = random.nextLong();
        var presentId = random.nextLong();
        var manager = new Manager(presentId, "name");

        when(managerRepository.findById(absentId)).thenReturn(Optional.empty());
        when(managerRepository.findById(presentId)).thenReturn(Optional.of(manager));

        Optional<ManagerResponse> absentResponse = managerServiceImpl.deleteManager(absentId);

        assertThat(absentResponse).isEmpty();
        verify(managerRepository).findById(absentId);

        Optional<ManagerResponse> presentResponse = managerServiceImpl.deleteManager(presentId);

        assertThat(presentResponse).hasValueSatisfying(managerResponse ->
                assertManagerMatchesResponse(manager, managerResponse));
        verify(managerRepository).findById(presentId);
        verify(managerRepository).delete(manager);

        verifyNoMoreInteractions(managerRepository);
    }


    private static void assertManagerMatchesResponse(Manager manager, ManagerResponse managerResponse) {
        assertThat(managerResponse.id()).isEqualTo(manager.getId());
        assertThat(managerResponse.name()).isEqualTo(manager.getName());
    }
}
