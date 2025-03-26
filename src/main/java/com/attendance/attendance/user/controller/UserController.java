package com.attendance.attendance.user.controller;

import com.attendance.attendance.user.model.User;
import com.attendance.attendance.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/update")
    public ResponseEntity updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }
    @GetMapping("/get")
    public ResponseEntity getUsers() {
        return userService.getUsers();
    }
}
