package com.attendance.attendance.department.service;

import com.attendance.attendance.department.model.Department;
import com.attendance.attendance.department.repository.DepartmentRepository;
import com.attendance.attendance.settings.service.JwtService;
import com.attendance.attendance.user.model.User;
import com.attendance.attendance.user.repository.UserRepository;
import com.attendance.attendance.utility.service.RandomCodeGenerator;
import com.attendance.attendance.utility.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    public ResponseEntity createDepartment(Department department, String token) {
        department.setCreatedAt(LocalDateTime.now());
        department.setCreatedBy(userRepository.findByUserID(jwtService.extractUserID(token)));
        departmentRepository.save(department);
        return responseService.formulateResponse(null, "Department created successfully", HttpStatus.OK, true);
    }
    public ResponseEntity updateDepartment(Department department, String token) {
        department.setUpdatedAt(LocalDateTime.now());
        department.setUpdatedBy(userRepository.findByUserID(jwtService.extractUserID(token)));
        departmentRepository.save(department);
        return responseService.formulateResponse(null, "Department updated successfully", HttpStatus.OK, true);
    }
    public ResponseEntity getDepartments(){
        return responseService.formulateResponse(departmentRepository.findAll(), "Departments fetched successfully", HttpStatus.OK, true);
    }
}
