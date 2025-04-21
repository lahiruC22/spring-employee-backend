package com.lahirucw.emp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeDTO {
    private String firstName;
    private String lastName;
    private String role;
    private String email;
    private String department;
}