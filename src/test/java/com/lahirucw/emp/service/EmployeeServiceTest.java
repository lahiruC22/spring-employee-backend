package com.lahirucw.emp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lahirucw.emp.model.Employee;
import com.lahirucw.emp.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee1;
    private Employee employee2;

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
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> employees = Arrays.asList(employee1, employee2);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployees();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testGetEmployeeById_Found() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));

        Optional<Employee> result = employeeService.getEmployeeById(1L);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeRepository.findById(3L)).thenReturn(Optional.empty());

        Optional<Employee> result = employeeService.getEmployeeById(3L);

        assertFalse(result.isPresent());
        verify(employeeRepository, times(1)).findById(3L); // Verify findById was called with 3L
    }

    @Test
    void testCreateEmployee() {
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("Peter");
        newEmployee.setLastName("Jones");
        newEmployee.setEmail("peter.jones@example.com");
        newEmployee.setDepartment("Sales");
        newEmployee.setRole("Manager");

        Employee savedEmployee = new Employee(
            3L, 
            "Peter", 
            "Jones", 
            "peter.jones@example.com", 
            "Sales", 
            "Manager");
        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);
        Employee result = employeeService.createEmployee(newEmployee);

        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("Peter", result.getFirstName());
        assertEquals("Sales", result.getDepartment());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployee() {
        Employee updatedEmployeeData = new Employee(
            1L, 
            "John", 
            "Doe Updated", 
            "john.doe.updated@example.com", 
            "Engineering", 
            "Junior Developer");
        when(employeeRepository.save(updatedEmployeeData)).thenReturn(updatedEmployeeData);

        Employee result = employeeService.updateEmployee(1L, updatedEmployeeData);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe Updated", result.getLastName());
        assertEquals("Junior Developer", result.getRole());
        assertEquals("Engineering", result.getDepartment());
        verify(employeeRepository, times(1)).save(updatedEmployeeData);
    }

    @Test
    void testDeleteEmployee() {
        employeeService.deleteEmployee(1L);
        verify(employeeRepository, times(1)).deleteById(1L);
    }
}
