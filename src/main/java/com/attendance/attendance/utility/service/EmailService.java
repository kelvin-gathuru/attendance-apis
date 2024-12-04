package com.attendance.attendance.utility.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class EmailService {
    @Value("${notification.api.url}")
    private String notificationApiUrl;
    @Autowired
    private ResponseService responseService;
    public void sendCredentials(String email, String password){
        String payload = getString(email, password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                notificationApiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Email sent successfully");
            responseService.formulateResponse(null,"Email Sent", HttpStatus.OK, true);
        } else {
            log.info("Email not Sent");
            responseService.formulateResponse(null,"Email Not Sent", HttpStatus.BAD_REQUEST, false);
        }

    }

    private static String getString(String email, String password) {
        String credentials = "::Email: " + email + "  ::Password: " + password;
        String payload = "{\"notificationCode\":\"PMANAGER-EMAIL\"," +
                "\"clientID\":1," +
                "\"message\":\"Hello User! These are your OuK Attendance App Login Credentials! " + credentials + "\"," +
                "\"subject\":\"ATTENDANCE APP LOGIN CREDENTIALS\"," +
                "\"recepient\":\"" + email + "\"," +
                "\"cCrecepients\":\"\"," +
                "\"bCCrecepients\":\"\"," +
                "\"type\":\"text/html\"}";
        return payload;
    }
}
