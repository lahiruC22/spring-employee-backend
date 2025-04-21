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


@RestController
@RequestMapping("api/employees")
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<EmployeeDTO> getAllEmployees(){
        List<Employee> employees = employeeService.getAllEmployees();
        return EmployeeMapper.toDTOList(employees);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id){
        Optional<Employee> employeeOptional = employeeService.getEmployeeById(id);
        return employeeOptional.map(employee -> ResponseEntity.ok(EmployeeMapper.toDTO(employee)))
                        .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDTO createEmployee(@RequestBody CreateEmployeeDTO createEmployeeDTO){
        Employee employee = EmployeeMapper.toEntity(createEmployeeDTO);
        employee = employeeService.createEmployee(employee);
        return EmployeeMapper.toDTO(employee);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @RequestBody UpdateEmployeeDTO updateEmployeeDTO){
        Optional<Employee> employeeOptional = employeeService.getEmployeeById(id);
        if(employeeOptional.isPresent()){
            Employee employee = employeeOptional.get();
            EmployeeMapper.updateEntityFromDTO(updateEmployeeDTO, employee);
            employee = employeeService.updateEmployee(id, employee);
            return ResponseEntity.ok(EmployeeMapper.toDTO(employee));
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteEmployee(@PathVariable Long id){
        employeeService.deleteEmployee(id);
    }
}
