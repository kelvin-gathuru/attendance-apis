package com.attendance.attendance.user.controller;

import com.attendance.attendance.user.model.User;
import com.attendance.attendance.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users/")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody User user) {
        return userService.createUser(user);
    }
    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody User user) {
        return userService.authenticateUser(user);
    }
}
