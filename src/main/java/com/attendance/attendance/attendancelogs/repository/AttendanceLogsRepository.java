package com.attendance.attendance.attendancelogs.repository;

import com.attendance.attendance.attendancelogs.model.AttendanceLogs;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AttendanceLogsRepository extends JpaRepository<AttendanceLogs, Long> {
    Optional<AttendanceLogs> findByFingerprintAndUserIDNot(String fingerprint,Long userID);
    Optional<AttendanceLogs> findByUserIDAndClockOutIsNull(Long userID);
    List<AttendanceLogs> findByClockOutIsNull();
    @Modifying
    @Transactional
    @Query("UPDATE AttendanceLogs a SET a.clockOut = :clockOut WHERE a.clockOut IS NULL")
    void autoClockOut(@Param("clockOut") LocalDateTime clockOut);
}
