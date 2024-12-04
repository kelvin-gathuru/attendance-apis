package com.attendance.attendance.attendancelogs.repository;

import com.attendance.attendance.attendancelogs.model.AttendanceLogs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AttendanceLogsRepository extends JpaRepository<AttendanceLogs, Long> {
}
