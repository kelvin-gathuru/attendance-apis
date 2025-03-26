package com.attendance.attendance.qrcode.controller;

import com.attendance.attendance.qrcode.service.QrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/attendance")
public class QrCodeController {
    @Autowired
    private QrCodeService qrCodeService;
    @GetMapping("/generate-qrcode")
    public ResponseEntity generateQRCode(@RequestHeader("Authorization") String token){
        return qrCodeService.generateCode();
    }
    @PostMapping("/read-qrcode")
    public ResponseEntity readQRCode(@RequestHeader("Authorization") String token,
                                     @RequestParam("code") String code) throws Exception {
        return qrCodeService.readQRCode(token, code);
    }
}
