package com.attendance.attendance.attendancelogs.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AttendanceLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceLogID;
    private Long userID;
    private String fingerprint;
    private String ipAddress;
    private String browser;
    private double latitude;
    private double longitude;
    private LocalDateTime clockIn;
    private LocalDateTime clockOut;
}
