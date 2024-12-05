package com.attendance.attendance.user.service;

import com.attendance.attendance.settings.dto.JwtUser;
import com.attendance.attendance.settings.service.JwtService;
import com.attendance.attendance.user.model.User;
import com.attendance.attendance.user.repository.UserRepository;
import com.attendance.attendance.utility.service.EmailService;
import com.attendance.attendance.utility.service.RandomCodeGenerator;
import com.attendance.attendance.utility.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ResponseService responseService;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(16, new SecureRandom());

    public ResponseEntity createUser(User user) {
        if(userRepository.findByEmail(user.getEmail()) != null) {
            return responseService.formulateResponse(null, "Email already exists", HttpStatus.BAD_REQUEST, false);
        }
        String generatedPassword = RandomCodeGenerator.generateRandomCode();
        String encodedPassword = bCryptPasswordEncoder.encode(generatedPassword);
        user.setPassword(encodedPassword);
        user.setCreatedAt(LocalDateTime.now());
        emailService.sendCredentials(user.getEmail(), generatedPassword);
        userRepository.save(user);
        return responseService.formulateResponse(null, "User created successfully", HttpStatus.OK, true);
    }
    public ResponseEntity updateUser(User user) {
        if(userRepository.findByEmail(user.getEmail()) == null) {
            return responseService.formulateResponse(null, "User does not exist", HttpStatus.BAD_REQUEST, false);
        }
        userRepository.save(user);
        return responseService.formulateResponse(null, "User updated successfully", HttpStatus.OK, true);
    }
    public ResponseEntity authenticateUser(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if(existingUser == null) {
            return responseService.formulateResponse(null, "User does not exist", HttpStatus.BAD_REQUEST, false);
        }
        if(bCryptPasswordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            JwtUser jwtUser = new JwtUser();
            jwtUser.setUserID(existingUser.getUserID());
            jwtUser.setName(existingUser.getName());
            jwtUser.setEmail(existingUser.getEmail());
            String token = jwtService.generateToken(jwtUser);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("name",existingUser.getName());
            return responseService.formulateResponse(response, "User authenticated successfully", HttpStatus.OK, true);
        }
        return responseService.formulateResponse(null, "Invalid credentials", HttpStatus.BAD_REQUEST, false);
    }
}
