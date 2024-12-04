package com.attendance.attendance.department.repository;

import com.attendance.attendance.department.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
