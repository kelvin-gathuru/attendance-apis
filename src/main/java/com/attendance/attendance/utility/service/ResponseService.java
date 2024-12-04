package com.attendance.attendance.utility.service;

import com.attendance.attendance.utility.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ResponseService {
    public ResponseEntity formulateResponse(Object data, String message, HttpStatus httpStatus, boolean status) {
        return new ResponseEntity<>(new ResponseDto(data, message, httpStatus, status), httpStatus);
    }
}
