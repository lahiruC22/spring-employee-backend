package com.lahirucw.emp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lahirucw.emp.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
