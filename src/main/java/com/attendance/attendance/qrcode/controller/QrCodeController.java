package com.attendance.attendance.qrcode.controller;

import com.attendance.attendance.qrcode.service.QrCodeService;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/attendance")
public class QrCodeController {
    @Autowired
    private QrCodeService qrCodeService;
    @GetMapping("/generate-qrcode")
    public ResponseEntity<byte[]> generateQRCode(@RequestHeader("Authorization") String token) throws IOException, WriterException {
        byte[] qrCode = qrCodeService.generateQrCode(token);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return ResponseEntity.ok().headers(headers).body(qrCode);
    }
}
