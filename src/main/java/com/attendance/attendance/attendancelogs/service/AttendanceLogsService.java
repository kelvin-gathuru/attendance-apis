package com.attendance.attendance.attendancelogs.service;

import com.attendance.attendance.attendancelogs.model.AttendanceLogs;
import com.attendance.attendance.attendancelogs.repository.AttendanceLogsRepository;
import com.attendance.attendance.settings.service.JwtService;
import com.attendance.attendance.utility.service.ResponseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceLogsService {
    @Autowired
    private AttendanceLogsRepository attendanceLogsRepository;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private JwtService jwtService;
    @Value("${location.office.latitude}")
    private double officeLatitude;
    @Value("${location.office.longitude}")
    private double officeLongitude;
    @Value("${location.max.distance}")
    private double maxDistance;
    @Value("${location.earth.radius}")
    private int earthRadius;
    public ResponseEntity clockAttendance(AttendanceLogs attendanceLogs, String token) {
        if(attendanceLogs.getFingerprint()==null){
            return responseService.formulateResponse(null, "Failed. Reload to try again", HttpStatus.INTERNAL_SERVER_ERROR, false);
        }
        if(attendanceLogs.getLatitude()== 0.0 || attendanceLogs.getLongitude()== 0.0){
            return responseService.formulateResponse(null, "Failed. Enable Location Permission", HttpStatus.INTERNAL_SERVER_ERROR, false);
        }
        Long userID = jwtService.extractUserID(token);
        Optional<AttendanceLogs> activeSession = attendanceLogsRepository.findByUserIDAndClockOutIsNull(userID);
        if(activeSession.isPresent()){
            return clockOut(attendanceLogs, userID);
        }else{
            return clockIn(attendanceLogs,userID);
        }
    }
    private ResponseEntity clockIn(AttendanceLogs attendanceLogs, Long userID) {
        Optional<AttendanceLogs> existingFingerprint = attendanceLogsRepository.findByFingerprintAndUserIDNot(attendanceLogs.getFingerprint(), userID);
        if (existingFingerprint.isPresent()) {
            return responseService.formulateResponse(null, "Failed. Use your phone to Clock In...", HttpStatus.INTERNAL_SERVER_ERROR, false);
        }
        if (!isWithinRadius(attendanceLogs.getLatitude(), attendanceLogs.getLongitude())) {
            return responseService.formulateResponse(null, "Failed. You are not with in the office...", HttpStatus.INTERNAL_SERVER_ERROR, false);
        }
        attendanceLogs.setClockIn(LocalDateTime.now());
        attendanceLogs.setUserID(userID);
        attendanceLogsRepository.save(attendanceLogs);
        return responseService.formulateResponse(attendanceLogs, "Success. Clocked In...", HttpStatus.OK, true);
    }
    private boolean isWithinRadius(double userLat, double userLon) {
        double latDistance = Math.toRadians(officeLatitude - userLat);
        double lonDistance = Math.toRadians(officeLongitude - userLon);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(officeLatitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;
        return distance <= maxDistance;
    }
    private ResponseEntity clockOut(AttendanceLogs attendanceLogs, Long userID) {
        Optional<AttendanceLogs> existingFingerprint = attendanceLogsRepository.findByFingerprintAndUserIDNot(attendanceLogs.getFingerprint(), userID);
        if (existingFingerprint.isPresent()) {
            return responseService.formulateResponse(null, "Failed. Use your phone to Clock Out...", HttpStatus.INTERNAL_SERVER_ERROR, false);
        }
        if (!isWithinRadius(attendanceLogs.getLatitude(), attendanceLogs.getLongitude())) {
            return responseService.formulateResponse(null, "Failed. You are not with in the office...", HttpStatus.INTERNAL_SERVER_ERROR, false);
        }
        Optional<AttendanceLogs> existingAttendanceLog = attendanceLogsRepository.findByUserIDAndClockOutIsNull(userID);
        existingAttendanceLog.get().setClockOut(LocalDateTime.now());
        attendanceLogsRepository.save(existingAttendanceLog.get());
        return responseService.formulateResponse(attendanceLogs, "Success. Clocked Out...", HttpStatus.OK, true);
    }
    @Scheduled(cron = "1 0 0 * * ?")
    @Transactional
    public void autoClockOut() {
        System.out.println("Auto clock out");
        LocalDateTime clockOutTime = LocalDateTime.now().minusDays(1).with(LocalTime.MAX);
        attendanceLogsRepository.autoClockOut(clockOutTime);
    }

}
