package com.attendance.attendance.attendancelogs.controller;

import com.attendance.attendance.attendancelogs.model.AttendanceLogs;
import com.attendance.attendance.attendancelogs.service.AttendanceLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/attendance")
public class AttendanceLogsController {
    @Autowired
    private AttendanceLogsService attendanceLogsService;
    @PostMapping("/clock-attendance")
    public ResponseEntity clockAttendance(@RequestHeader("Authorization") String token,
                                     @RequestBody AttendanceLogs attendanceLogs){
        System.out.println(attendanceLogs);
        return attendanceLogsService.clockAttendance(attendanceLogs, token);
    }
}
