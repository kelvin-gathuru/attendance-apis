package com.attendance.attendance.qrcode.repository;

import com.attendance.attendance.qrcode.model.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface QrCodeRepository extends JpaRepository<QrCode, Long> {
    Optional<QrCode> findByQrCodeData(String qrCodeData);
}
