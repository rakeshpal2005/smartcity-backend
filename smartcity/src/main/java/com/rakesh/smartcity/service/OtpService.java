package com.rakesh.smartcity.service;

import com.rakesh.smartcity.model.Otp;
import com.rakesh.smartcity.repo.OtpRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepo otpRepo;


    @Autowired
    private JavaMailSender mailSender;

    @Transactional
    public void sendOtp(String email) {
        // Delete old OTP if any
        otpRepo.deleteByEmail(email);

        // Generate 6 digit OTP
        String otpCode = String.format("%06d", new Random().nextInt(999999));

        // Save to database
        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setOtp(otpCode);
        otpRepo.save(otp);



        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("SmartCity - Registration OTP");
        message.setText("Your OTP for registration is: " + otpCode +
                       "\n\nThis OTP is valid for 5 minutes only.");
        mailSender.send(message);



        System.out.println("✅ OTP Generated for " + email + " : " + otpCode);

    }

    @Transactional
    public boolean verifyOtp(String email, String userOtp) {
        Optional<Otp> otpOptional = otpRepo.findByEmail(email);

        if (otpOptional.isEmpty()) {
            return false;
        }

        Otp otp = otpOptional.get();

        if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
            otpRepo.delete(otp);
            return false;
        }

        boolean isValid = otp.getOtp().equals(userOtp);

        if (isValid) {
            otpRepo.delete(otp);
        }

        return isValid;
    }
}
