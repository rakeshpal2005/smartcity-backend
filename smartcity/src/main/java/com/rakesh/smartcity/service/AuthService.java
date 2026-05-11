package com.rakesh.smartcity.service;


import com.rakesh.smartcity.Dto.LoginRequestDto;
import com.rakesh.smartcity.Dto.LoginResponseDto;
import com.rakesh.smartcity.Dto.RegisterRequsetDto;
import com.rakesh.smartcity.Exception.BadRequestException;
import com.rakesh.smartcity.Exception.ResourceNotFoundException;
import com.rakesh.smartcity.model.Role;
import com.rakesh.smartcity.model.User;
import com.rakesh.smartcity.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    private OtpService otpService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    JwtService jwtService;

    // Send OTP
    public void sendRegistrationOtp(String email) {
        if (userRepo.findByEmail(email).isPresent()) {
            throw new BadRequestException("Email already registered");
        }
        otpService.sendOtp(email);
    }

    // Register with OTP
    public void register(RegisterRequsetDto requestDto) {
        // Verify OTP first
        if (!otpService.verifyOtp(requestDto.getEmail(), requestDto.getOtp())) {
            throw new BadRequestException("Invalid or expired OTP");
        }

        // Check duplicate phone
        if (userRepo.findByPhoneNumber(requestDto.getPhoneNumber()).isPresent()) {
            throw new BadRequestException("Phone number already exists");
        }

        User user = new User();
        user.setName(requestDto.getName());
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setPhoneNumber(requestDto.getPhoneNumber());
        user.setPinCode(requestDto.getPinCode());
        user.setRole(Role.USER);

        userRepo.save(user);
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getEmail(),
                            loginRequestDto.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new BadRequestException("Invalid email or password");
        }

        User user = userRepo.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponseDto(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getPhoneNumber(),
                user.getPinCode()
        );
    }
}
