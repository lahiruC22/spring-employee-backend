package com.lahirucw.emp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lahirucw.emp.dto.CreateEmployeeDTO;
import com.lahirucw.emp.dto.EmployeeDTO;
import com.lahirucw.emp.dto.UpdateEmployeeDTO;
import com.lahirucw.emp.mapper.EmployeeMapper;
import com.lahirucw.emp.model.Employee;
import com.lahirucw.emp.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;

@RestController
@RequestMapping("api/employees")
@Tag(name = "Employee Management", description = "API for managing employee information")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * Retrieves a list of all employees.
     * Accessible by users with 'USER' or 'ADMIN' roles.
     *
     * @return A list of EmployeeDTOs.
     */
    @Operation(summary = "Get all employees", description = "Retrieve a list of all employees", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return EmployeeMapper.toDTOList(employees);
    }

    /**
     * Retrieves an employee by their ID.
     * Accessible by users with 'USER' or 'ADMIN' roles.
     *
     * @param id The ID of the employee to retrieve.
     * @return ResponseEntity containing the EmployeeDTO or a 404 Not Found status.
     */
    @Operation(summary = "Get employee by ID", description = "Retrieve a specific employee by their ID", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved employee", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        Optional<Employee> employeeOptional = employeeService.getEmployeeById(id);
        return employeeOptional.map(employee -> ResponseEntity.ok(EmployeeMapper.toDTO(employee)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new employee.
     * Accessible only by users with the 'ADMIN' role.
     *
     * @param createEmployeeDTO The DTO containing the details for the new employee.
     * @return The created EmployeeDTO with a 201 Created status.
     */
    @Operation(summary = "Create a new employee", description = "Create a new employee record", responses = {
            @ApiResponse(responseCode = "201", description = "Employee created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDTO createEmployee(@RequestBody CreateEmployeeDTO createEmployeeDTO) {
        Employee employee = EmployeeMapper.toEntity(createEmployeeDTO);
        employee = employeeService.createEmployee(employee);
        return EmployeeMapper.toDTO(employee);
    }

    /**
     * Updates an existing employee.
     * Accessible only by users with the 'ADMIN' role.
     *
     * @param id                The ID of the employee to update.
     * @param updateEmployeeDTO The DTO containing the updated employee details.
     * @return ResponseEntity containing the updated EmployeeDTO or a 404 Not Found
     *         status.
     */
    @Operation(summary = "Update an existing employee", description = "Update details of an existing employee by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id,
            @RequestBody UpdateEmployeeDTO updateEmployeeDTO) {
        Optional<Employee> employeeOptional = employeeService.getEmployeeById(id);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            EmployeeMapper.updateEntityFromDTO(updateEmployeeDTO, employee);
            employee = employeeService.updateEmployee(id, employee);
            return ResponseEntity.ok(EmployeeMapper.toDTO(employee));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes an employee by their ID.
     * Accessible only by users with the 'ADMIN' role.
     *
     * @param id The ID of the employee to delete.
     */
    @Operation(summary = "Delete an employee", description = "Delete an employee by their ID", responses = {
            @ApiResponse(responseCode = "200", description = "Employee deleted successfully"), // Or 204 No Content
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }
}
