package com.lahirucw.emp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lahirucw.emp.dto.CreateEmployeeDTO;
import com.lahirucw.emp.dto.EmployeeDTO;
import com.lahirucw.emp.dto.UpdateEmployeeDTO;
import com.lahirucw.emp.model.Employee;
import com.lahirucw.emp.service.EmployeeService;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee1;
    private Employee employee2;
    private EmployeeDTO employeeDTO1;
    private EmployeeDTO employeeDTO2;
    private CreateEmployeeDTO createEmployeeDTO;
    private UpdateEmployeeDTO updateEmployeeDTO;

    @BeforeEach
    void setUp() {
        employee1 = new Employee(
            1L, 
            "John", 
            "Doe", 
            "john.doe@example.com", 
            "Engineering", 
            "Senior Developer"
            );

        employee2 = new Employee(
            2L, 
            "Jane", 
            "Smith", 
            "jane.smith@example.com",
            "Sales", 
            "Sales Representative"
        );

        employeeDTO1 = new EmployeeDTO(
            1L,
            "John",
            "Doe",
            "Senior Developer"
        );
        
        employeeDTO2 = new EmployeeDTO(
            2L,
            "Jane", 
            "Smith", 
            "Sales Representative"
        );

        createEmployeeDTO = new CreateEmployeeDTO(
            "Peter",
            "Jones",
            "peter.jones@example.com",
            "Senior Manager",
            "Sales"
        );

        updateEmployeeDTO = new UpdateEmployeeDTO(
            "Sales Executive", 
            "jane.smith@example.com"
        );
    }

    @Test
    @WithMockUser(username = "ADMIN", roles = {"USER", "ADMIN"}) 
    void testGetAllEmployees() throws Exception {
        List<Employee> employees = Arrays.asList(employee1, employee2);
        when(employeeService.getAllEmployees()).thenReturn(employees);
        
        mockMvc.perform(get("/api/employees"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.length()").value(2))
               .andExpect(jsonPath("$[0].id").value(1L))
               .andExpect(jsonPath("$[0].firstName").value("John"))
               .andExpect(jsonPath("$[1].id").value(2L))
               .andExpect(jsonPath("$[1].firstName").value("Jane"));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    @WithMockUser(username = "ADMIN", roles = {"USER", "ADMIN"}) 
    void testGetEmployeeById_Found() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(employee1));
        mockMvc.perform(get("/api/employees/{id}", 1L))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id").value(1L))
               .andExpect(jsonPath("$.firstName").value("John"));

        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    @WithMockUser(username = "ADMIN", roles = {"USER", "ADMIN"}) 
    void testGetEmployeeById_NotFound() throws Exception {

        when(employeeService.getEmployeeById(3L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/employees/{id}", 3L))
               .andExpect(status().isNotFound());

        verify(employeeService, times(1)).getEmployeeById(3L);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"}) 
    void testCreateEmployee() throws Exception {
        //Employee employeeToSave = EmployeeMapper.toEntity(createEmployeeDTO);
        Employee savedEmployee = new Employee(
            3L, 
            "Peter", 
            "Jones",
            "peter.jones@example.com", 
            "HR", 
            "Manager");

       // EmployeeDTO expectedDTO = EmployeeMapper.toDTO(savedEmployee);
        when(employeeService.createEmployee(any(Employee.class))).thenReturn(savedEmployee);

        mockMvc.perform(post("/api/employees")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(createEmployeeDTO)))
               .andExpect(status().isCreated())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id").value(3L))
               .andExpect(jsonPath("$.firstName").value("Peter"))
               .andExpect(jsonPath("$.position").value("Manager"));

        verify(employeeService, times(1)).createEmployee(any(Employee.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"}) 
    void testUpdateEmployee_Found() throws Exception {

        Long employeeId = 1L;
        Employee existingEmployee = employee1;
        Employee updatedEmployeeEntity = new Employee(
            employeeId, 
            "John", 
            "Doe",
            "peter.jones.updated@example.com",
            "Engineering",
            "Senior Manager"
        );

        //EmployeeDTO expectedDTO = EmployeeMapper.toDTO(updatedEmployeeEntity);

        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(employeeService.updateEmployee(eq(employeeId), any(Employee.class))).thenReturn(updatedEmployeeEntity);

        mockMvc.perform(put("/api/employees/{id}", employeeId)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(updateEmployeeDTO)))
               .andExpect(status().isOk()) // Expect HTTP 200 OK
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id").value(employeeId))
               .andExpect(jsonPath("$.position").value("Senior Manager"));

        verify(employeeService, times(1)).getEmployeeById(employeeId);
        verify(employeeService, times(1)).updateEmployee(eq(employeeId), any(Employee.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"}) 
    void testUpdateEmployee_NotFound() throws Exception {
        Long employeeId = 3L;
        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/employees/{id}", employeeId)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(updateEmployeeDTO)))
               .andExpect(status().isNotFound());

        verify(employeeService, times(1)).getEmployeeById(employeeId);
        verify(employeeService, never()).updateEmployee(anyLong(), any(Employee.class));
    }


    @Test
    @WithMockUser(roles = {"ADMIN"}) 
    void testDeleteEmployee() throws Exception {
        mockMvc.perform(delete("/api/employees/{id}", 1L))
               .andExpect(status().isNoContent());
        verify(employeeService, times(1)).deleteEmployee(1L);
    }
}