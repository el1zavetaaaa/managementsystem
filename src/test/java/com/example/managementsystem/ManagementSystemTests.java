package com.example.managementsystem;

import com.example.managementsystem.model.request.SaveEmployeeRequest;
import com.example.managementsystem.model.request.SaveManagerRequest;
import com.example.managementsystem.model.response.EmployeeResponse;
import com.example.managementsystem.model.response.ManagerResponse;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriTemplate;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"h2db", "debug"})
class ManagementSystemTests {

    private static final Logger log = LoggerFactory.getLogger(ManagementSystemTests.class);

    @Autowired
    private TestRestTemplate rest;

    @Test
    void contextLoads() {
        assertNotNull(rest);
    }

    @Test
    void testCreateManager() {
        var name = "test  creation of manager";
        ResponseEntity<ManagerResponse> managerResponseEntity = createManager(name);

        assertEquals(HttpStatus.CREATED, managerResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, managerResponseEntity.getHeaders().getContentType());

        ManagerResponse responseBody = managerResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(name, responseBody.name());
        assertNotNull(responseBody.id());
        log.info("Test manager with name {} was successfully created!", name);
    }


    @Test
    void testGetManagerById() {
        var name = "test getting manager by id";

        var manager = createManager(name).getBody();
        assertNotNull(manager);

        Long id = manager.id();

        var managerUrl = baseUrlForGettingManagerById(id);

        ResponseEntity<ManagerResponse> messageResponseEntity = rest
                .getForEntity(managerUrl, ManagerResponse.class);
        assertEquals(HttpStatus.OK, messageResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, messageResponseEntity.getHeaders().getContentType());

        ManagerResponse responseBody = messageResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(name, responseBody.name());
        assertEquals(id, responseBody.id());

        assertEquals(responseBody, rest.getForEntity(managerUrl, ManagerResponse.class).getBody());
    }

    @Test
    void testUpdateManager() {
        var name = "new message";

        var manager = createManager(name).getBody();
        assertNotNull(manager);

        Long id = manager.id();

        var managerUrl = baseUrlForGettingManagerById(id);

        var updatedName = "updated manager's name";

        rest.put(managerUrl, new SaveManagerRequest(updatedName));

        ResponseEntity<ManagerResponse> managerResponseEntity = rest.getForEntity(managerUrl, ManagerResponse.class);
        assertEquals(HttpStatus.OK, managerResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, managerResponseEntity.getHeaders().getContentType());

        ManagerResponse responseBody = managerResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(updatedName, responseBody.name());
        assertEquals(id, responseBody.id());

        log.info("Test Manager was successfully updated! His/Her new name is {}!", updatedName);
    }

    @Test
    void testDeleteManager() {
        var name = "name for deleting";

        var manager = createManager(name).getBody();
        assertNotNull(manager);

        Long id = manager.id();

        var managerUrl = baseUrlForGettingManagerById(id);

        ResponseEntity<ManagerResponse> managerResponseEntity = rest
                .exchange(RequestEntity.delete(managerUrl).build(), ManagerResponse.class);

        assertEquals(HttpStatus.OK, managerResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, managerResponseEntity.getHeaders().getContentType());

        ManagerResponse responseBody = managerResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(name, responseBody.name());
        assertEquals(id, responseBody.id());

        assertEquals(HttpStatus.NOT_FOUND, rest.getForEntity(managerUrl, ManagerResponse.class).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, rest
                .exchange(RequestEntity.delete(managerUrl).build(), ManagerResponse.class)
                .getStatusCode());
        log.warn("Test Manager with id {} was deleted.", id);
    }

    @Test
    void testCreateEmployee() {
        var firstName = "test employee's first name";
        var lastName = "test employee's last name";
        var email = "test employee's email";
        ResponseEntity<EmployeeResponse> employeeResponseEntity = createEmployee(firstName, lastName, email);

        assertEquals(HttpStatus.CREATED, employeeResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, employeeResponseEntity.getHeaders().getContentType());

        EmployeeResponse responseBody = employeeResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(firstName, responseBody.firstName());
        assertEquals(lastName, responseBody.lastName());
        assertEquals(email, responseBody.email());
        assertNotNull(responseBody.id());
        log.info("Test Employee with email {} was successfully created!", email);
    }

    @Test
    void testGetEmployeeByID() {
        var firstName = "test employee first name";
        var lastName = "test employee last name";
        var email = "test employee email";

        var employee = createEmployee(firstName, lastName, email).getBody();
        assertNotNull(employee);

        Long id = employee.id();

        var employeeUrl = baseUrlForGettingEmployeeById(id);

        ResponseEntity<EmployeeResponse> employeeResponseEntity = rest
                .getForEntity(employeeUrl, EmployeeResponse.class);
        assertEquals(HttpStatus.OK, employeeResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, employeeResponseEntity.getHeaders().getContentType());

        EmployeeResponse responseBody = employeeResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(firstName, responseBody.firstName());
        assertEquals(lastName, responseBody.lastName());
        assertEquals(email, responseBody.email());
        assertEquals(id, responseBody.id());

        assertEquals(responseBody, rest.getForEntity(employeeUrl, EmployeeResponse.class).getBody());
    }

    @Test
    void testUpdateEmployee() {
        var firstName = "test employee's first name";
        var lastName = "test employee's last name";
        var email = "test employee's email";

        var employee = createEmployee(firstName, lastName, email).getBody();
        assertNotNull(employee);

        Long id = employee.id();

        var employeeUrl = baseUrlForGettingEmployeeById(id);

        var updatedFirstName = "test update employee first name";
        var updatedLastName = "test update employee last name";
        var updatedEmail = "test update employee email";

        rest.put(employeeUrl, new SaveEmployeeRequest(updatedFirstName, updatedLastName, updatedEmail));

        ResponseEntity<EmployeeResponse> employeeResponseEntity = rest.getForEntity(employeeUrl, EmployeeResponse.class);
        assertEquals(HttpStatus.OK, employeeResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, employeeResponseEntity.getHeaders().getContentType());

        EmployeeResponse responseBody = employeeResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(updatedFirstName, responseBody.firstName());
        assertEquals(updatedLastName, responseBody.lastName());
        assertEquals(updatedEmail, responseBody.email());
        assertEquals(id, responseBody.id());

        log.info("Test Employee was successfully updated! His/her new email is {}!", email);
    }

    @Test
    void testDeleteEmployee() {
        var firstName = "test delete employee first name";
        var lastName = "test delete employee last name";
        var email = "test delete employee email";

        var employee = createEmployee(firstName, lastName, email).getBody();
        assertNotNull(employee);

        Long id = employee.id();

        var employeeUrl = baseUrlForGettingEmployeeById(id);

        ResponseEntity<EmployeeResponse> employeeResponseResponseEntity = rest
                .exchange(RequestEntity.delete(employeeUrl).build(), EmployeeResponse.class);

        assertEquals(HttpStatus.OK, employeeResponseResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, employeeResponseResponseEntity.getHeaders().getContentType());

        EmployeeResponse responseBody = employeeResponseResponseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(firstName, responseBody.firstName());
        assertEquals(lastName, responseBody.lastName());
        assertEquals(email, responseBody.email());
        assertEquals(id, responseBody.id());

        assertEquals(HttpStatus.NOT_FOUND, rest.getForEntity(employeeUrl, EmployeeResponse.class).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, rest
                .exchange(RequestEntity.delete(employeeUrl).build(), EmployeeResponse.class)
                .getStatusCode());

        log.warn("Test Employee with id {} was deleted", id);
    }

    private ResponseEntity<ManagerResponse> createManager(String name) {
        var url = baseUrlForManager();
        var requestBody = new SaveManagerRequest(name);

        return rest.postForEntity(url, requestBody, ManagerResponse.class);
    }

    private URI baseUrlForManager() {
        return URI.create("/managers");
    }

    private URI baseUrlForGettingManagerById(Long id) {
        return new UriTemplate("/managers/{id}").expand(id);
    }

    private ResponseEntity<EmployeeResponse> createEmployee(String firstName, String lastName, String email) {
        var url = baseUrlForEmployee();
        var requestBody = new SaveEmployeeRequest(firstName, lastName, email);

        return rest.postForEntity(url, requestBody, EmployeeResponse.class);
    }


    private URI baseUrlForEmployee() {
        return URI.create("/employees");
    }

    private URI baseUrlForGettingEmployeeById(Long id) {
        return new UriTemplate("/employees/{id}").expand(id);
    }


}
