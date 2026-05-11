package com.rakesh.smartcity.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequsetDto {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String pinCode;
    private String otp;
}
