package com.utkarsh.MaterialHub.controllers;

import com.utkarsh.MaterialHub.Services.UserService;
import com.utkarsh.MaterialHub.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        String result = userService.register(user);

        if (result.equals("success")) {
            return ResponseEntity.ok(Map.of("message", "success"));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", result));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        try {
            Map<String , String> data = userService.login(email, password);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Server error"));
        }
    }


}
