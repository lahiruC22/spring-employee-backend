package com.lahirucw.emp.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.lahirucw.emp.dto.CreateEmployeeDTO;
import com.lahirucw.emp.dto.EmployeeDTO;
import com.lahirucw.emp.dto.UpdateEmployeeDTO;
import com.lahirucw.emp.model.Employee;

public class EmployeeMapper {
    public static EmployeeDTO toDTO(Employee employee) {
        if (employee == null) {
            return null;
        }

        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setRole(employee.getRole());
        return dto;
    }

    public static List<EmployeeDTO> toDTOList(List<Employee> employees) {
        return employees.stream()
                .map(EmployeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static Employee toEntity(CreateEmployeeDTO dto) {
        if (dto == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setRole(dto.getRole());
        employee.setEmail(dto.getEmail());
        return employee;
    }

    public static void updateEntityFromDTO(UpdateEmployeeDTO dto, Employee employee) {
         if (dto == null || employee == null) {
            return;
        }
        if (dto.getRole() != null) {
            employee.setRole(dto.getRole());
        }
        if (dto.getEmail() != null) {
            employee.setEmail(dto.getEmail());
        }
    }
}
