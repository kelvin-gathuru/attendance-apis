package com.attendance.attendance.qrcode.repository;

import com.attendance.attendance.qrcode.model.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface QrCodeRepository extends JpaRepository<QrCode, Long> {
}
