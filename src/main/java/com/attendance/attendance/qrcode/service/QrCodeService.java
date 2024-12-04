package com.attendance.attendance.qrcode.service;

import com.attendance.attendance.qrcode.repository.QrCodeRepository;
import com.attendance.attendance.settings.service.JwtService;
import com.attendance.attendance.utility.service.ResponseService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Slf4j
public class QrCodeService {
    @Autowired
    private QrCodeRepository qrCodeRepository;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private JwtService jwtService;
    public byte[] generateQrCode(String token) throws WriterException, IOException {
        Long userId = jwtService.extractUserID(token);
        String qrCodeData = "USER_" + userId + "_" + LocalDateTime.now();
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, 200, 200);
        BufferedImage bufferedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        bufferedImage.createGraphics().fillRect(0, 0, 200, 200);
        Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
        graphics.setColor(Color.BLACK);

        for (int x = 0; x < 200; x++) {
            for (int y = 0; y < 200; y++) {
                if (bitMatrix.get(x, y)) {
                    graphics.fillRect(x, y, 1, 1);
                }
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write((RenderedImage) bufferedImage, "png", baos);
        return baos.toByteArray();
    }
}
