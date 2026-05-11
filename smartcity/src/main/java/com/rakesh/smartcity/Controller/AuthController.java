package com.rakesh.smartcity.Controller;

import com.rakesh.smartcity.Dto.LoginRequestDto;
import com.rakesh.smartcity.Dto.LoginResponseDto;
import com.rakesh.smartcity.Dto.RegisterRequsetDto;
import com.rakesh.smartcity.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")

public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam String email) {
        authService.sendRegistrationOtp(email);
        return "OTP sent successfully to " + email;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequsetDto request) {
        authService.register(request);
        return "User registered";
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto request) {
        return authService.login(request);
    }
}
