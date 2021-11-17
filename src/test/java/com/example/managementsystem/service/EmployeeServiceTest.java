package com.example.managementsystem.service;

import com.example.managementsystem.entity.Employee;
import com.example.managementsystem.entity.EmployeeResponse;
import com.example.managementsystem.entity.SaveEmployeeRequest;
import com.example.managementsystem.repository.EmployeeRepository;
import com.example.managementsystem.service.impl.EmployeeServiceImpl;
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

public class EmployeeServiceTest {

    private EmployeeServiceImpl employeeServiceImpl;
    private EmployeeRepository employeeRepository;
    Random random = new Random();

    @BeforeEach
    void setUp() {
        employeeRepository = mock(EmployeeRepository.class);
        employeeServiceImpl = new EmployeeServiceImpl(employeeRepository);
    }

    @Test
    void testCreateEmployee() {
        var request = new SaveEmployeeRequest("firstName", "lastName", "email");
        var employeeId = random.nextLong();

        when(employeeRepository.save(notNull())).thenAnswer(invocation -> {
            Employee entity = invocation.getArgument(0);
            assertThat(entity.getId()).isNull();
            assertThat(entity.getFirstName()).isEqualTo(request.firstName());
            assertThat(entity.getLastName()).isEqualTo(request.lastName());
            assertThat(entity.getEmail()).isEqualTo(request.email());
            entity.setId(employeeId);
            return entity;
        });

        EmployeeResponse response = employeeServiceImpl.saveEmployee(request);

        assertThat(response.id()).isEqualTo(employeeId);
        assertThat(response.firstName()).isEqualTo(request.firstName());
        assertThat(response.lastName()).isEqualTo(request.lastName());
        assertThat(response.email()).isEqualTo(request.email());
        verify(employeeRepository, only()).save(notNull());
    }

    @Test
    void testGetEmployeeById() {
        var absentId = random.nextLong();
        var presentId = random.nextLong();
        var employee = new Employee(presentId, "firstName", "lastName", "email");

        when(employeeRepository.findById(absentId)).thenReturn(Optional.empty());
        when(employeeRepository.findById(presentId)).thenReturn(Optional.of(employee));

        Optional<EmployeeResponse> absentResponse = employeeServiceImpl.getEmployeeById(absentId);

        assertThat(absentResponse).isEmpty();
        verify(employeeRepository).findById(absentId);

        Optional<EmployeeResponse> presentResponse = employeeServiceImpl.getEmployeeById(presentId);

        assertThat(presentResponse).hasValueSatisfying(employeeResponse ->
                assertEmployeeMatchesResponse(employee, employeeResponse));
        verify(employeeRepository).findById(presentId);

        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void testUpdateManager() {
        var presentId = random.nextLong();
        var absentId = random.nextLong();
        var update = new SaveEmployeeRequest("new firstName", "new lastName", "new email");

        var employee = new Employee(presentId, "firstName", "lastName", "email");

        when(employeeRepository.findById(absentId)).thenReturn(Optional.empty());
        when(employeeRepository.findById(presentId)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(same(employee))).thenReturn(employee);

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> employeeServiceImpl.updateEmployee(absentId, update))
                .satisfies(e -> assertThat(e.getStatus()).isSameAs(HttpStatus.NOT_FOUND));

        verify(employeeRepository).findById(absentId);

        employeeServiceImpl.updateEmployee(presentId, update);

        assertThat(employee.getFirstName()).isEqualTo(update.firstName());
        assertThat(employee.getLastName()).isEqualTo(update.lastName());
        assertThat(employee.getEmail()).isEqualTo(update.email());

        verify(employeeRepository).findById(presentId);
        verify(employeeRepository).save(same(employee));

        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void testDeleteManager() {
        var absentId = random.nextLong();
        var presentId = random.nextLong();
        var employee = new Employee(presentId, "firstName", "lastName", "email");

        when(employeeRepository.findById(absentId)).thenReturn(Optional.empty());
        when(employeeRepository.findById(presentId)).thenReturn(Optional.of(employee));

        Optional<EmployeeResponse> absentResponse = employeeServiceImpl.deleteEmployee(absentId);

        assertThat(absentResponse).isEmpty();
        verify(employeeRepository).findById(absentId);

        Optional<EmployeeResponse> presentResponse = employeeServiceImpl.deleteEmployee(presentId);

        assertThat(presentResponse).hasValueSatisfying(employeeResponse ->
                assertEmployeeMatchesResponse(employee, employeeResponse));
        verify(employeeRepository).findById(presentId);
        verify(employeeRepository).delete(employee);

        verifyNoMoreInteractions(employeeRepository);
    }

    private static void assertEmployeeMatchesResponse(Employee employee, EmployeeResponse employeeResponse) {
        assertThat(employeeResponse.id()).isEqualTo(employee.getId());
        assertThat(employeeResponse.firstName()).isEqualTo(employee.getFirstName());
        assertThat(employeeResponse.lastName()).isEqualTo(employee.getLastName());
        assertThat(employeeResponse.email()).isEqualTo(employee.getEmail());
    }
}
