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

    public ResponseEntity<?> generateCode(String token) throws Exception {
        Long userID = jwtService.extractUserID(token);
        Optional<QrCode> existingQrCode = qrCodeRepository.findByUserIDAndIsExpiredIsFalseAndIsUsedIsFalse(userID);

        if (existingQrCode.isPresent()) {
            QrCode qrCode = existingQrCode.get();
            if (isExpired(qrCode)) {
                qrCode.setExpired(true);
                qrCodeRepository.save(qrCode);
            } else {
                long secondsToWait = getSecondsToWait(qrCode);
                return responseService.formulateResponse(null, "User already has a valid QR code. Wait for " + secondsToWait + " more seconds" , HttpStatus.BAD_REQUEST, false);
            }
        }
        String qrCodeData = UUID.randomUUID().toString();
        QrCode newQrCode = new QrCode();
        newQrCode.setQrCodeData(qrCodeData);
        newQrCode.setUserID(userID);
        newQrCode.setCreatedAt(LocalDateTime.now());
        newQrCode.setExpired(false);
        newQrCode.setUsed(false);
        qrCodeRepository.save(newQrCode);
        String encryptedQrCodeData = EncryptionUtil.encrypt(qrCodeData);
        return responseService.formulateResponse(encryptedQrCodeData, "QR code generated successfully", HttpStatus.OK, true);
    }


    public ResponseEntity<?> readQRCode(String token, String code) throws Exception {
        Long userID = jwtService.extractUserID(token);
        String qrCodeData = EncryptionUtil.decrypt(code);

        Optional<QrCode> optionalQrCode = qrCodeRepository.findByQrCodeData(qrCodeData);
        if (optionalQrCode.isEmpty()) {
            return responseService.formulateResponse(null, "QR code does not exist", HttpStatus.BAD_REQUEST, false);
        }

        QrCode qrCode = optionalQrCode.get();

        if (!isAuthorized(qrCode, userID)) {
            return responseService.formulateResponse(null, "Unauthorized access", HttpStatus.UNAUTHORIZED, false);
        }

        if (qrCode.isUsed()) {
            return responseService.formulateResponse(null, "QR code already used", HttpStatus.BAD_REQUEST, false);
        }

        if (isExpired(qrCode)) {
            qrCode.setExpired(true);
            qrCodeRepository.save(qrCode);
            return responseService.formulateResponse(null, "QR code expired", HttpStatus.BAD_REQUEST, false);
        }

        qrCode.setUsed(true);
        qrCodeRepository.save(qrCode);
        return responseService.formulateResponse(null, "QR code read successfully", HttpStatus.OK, true);
    }

    private boolean isAuthorized(QrCode qrCode, Long userID) {
        return Objects.equals(qrCode.getUserID(), userID);
    }

    private boolean isExpired(QrCode qrCode) {
        LocalDateTime expiryTime = qrCode.getCreatedAt().plusMinutes(3);
        return qrCode.isExpired() || expiryTime.isBefore(LocalDateTime.now());
    }
    private long getSecondsToWait(QrCode qrCode) {
        LocalDateTime expiryTime = qrCode.getCreatedAt().plusMinutes(3);
        Duration duration = Duration.between(LocalDateTime.now(), expiryTime);
        return duration.getSeconds() > 0 ? duration.getSeconds() : 0;  // Return 0 if QR code is already expired
    }

}
