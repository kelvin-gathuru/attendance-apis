package com.attendance.attendance.qrcode.service;

import com.attendance.attendance.qrcode.model.QrCode;
import com.attendance.attendance.qrcode.repository.QrCodeRepository;
import com.attendance.attendance.qrcode.util.EncryptionUtil;
import com.attendance.attendance.settings.service.JwtService;
import com.attendance.attendance.utility.service.ResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class QrCodeService {
    @Autowired
    private QrCodeRepository qrCodeRepository;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private JwtService jwtService;

    public ResponseEntity generateCode(){
        String qrCodeData = UUID.randomUUID().toString();
        QrCode newQrCode = new QrCode();
        newQrCode.setQrCodeData(qrCodeData);
        newQrCode.setCreatedAt(LocalDateTime.now());
        newQrCode.setExpired(false);
        qrCodeRepository.save(newQrCode);
        return responseService.formulateResponse(qrCodeData, "QR code generated successfully", HttpStatus.OK, true);
    }


    public ResponseEntity readQRCode(String token, String qrCodeData){
        Long userID = jwtService.extractUserID(token);

        Optional<QrCode> optionalQrCode = qrCodeRepository.findByQrCodeData(qrCodeData);
        if (optionalQrCode.isEmpty()) {
            return responseService.formulateResponse(null, "QR code does not exist", HttpStatus.BAD_REQUEST, false);
        }
        QrCode qrCode = qrCodeRepository.findByQrCodeData(qrCodeData).get();
        if (isExpired(qrCode)) {
            qrCode.setExpired(true);
            qrCodeRepository.save(qrCode);
            return responseService.formulateResponse(null, "QR code expired", HttpStatus.BAD_REQUEST, false);
        }

        qrCodeRepository.save(qrCode);
        return responseService.formulateResponse(null, "QR code read successfully", HttpStatus.OK, true);
    }

    private boolean isExpired(QrCode qrCode) {
        LocalDateTime expiryTime = qrCode.getCreatedAt().plusDays(7);
        return qrCode.isExpired() || expiryTime.isBefore(LocalDateTime.now());
    }
}
