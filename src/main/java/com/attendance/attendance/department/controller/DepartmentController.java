package com.attendance.attendance.department.controller;

import com.attendance.attendance.department.model.Department;
import com.attendance.attendance.department.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/departments/")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @PostMapping("/create")
    public ResponseEntity registerUser(@RequestBody Department department, @RequestHeader("Authorization") String token) {
        return departmentService.createDepartment(department, token);
    }
    @PostMapping("/update")
    public ResponseEntity updateUser(@RequestBody Department department, @RequestHeader("Authorization") String token) {
        return departmentService.updateDepartment(department, token);
    }
    @GetMapping("/get")
    public ResponseEntity getDepartments(@RequestHeader("Authorization") String token) {
        return departmentService.getDepartments();
    }
}
