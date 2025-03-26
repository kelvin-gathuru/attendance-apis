package com.attendance.attendance.department.model;

import com.attendance.attendance.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentID;
    private String departmentName;
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "createdBy")
    private User createdBy;
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "updatedBy")
    private User updatedBy;
}
