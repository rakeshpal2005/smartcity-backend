package com.rakesh.smartcity.Dto;

import com.rakesh.smartcity.model.Role;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private String token;
    private Long id;
    private String name;
    private String email;
    private Role role;
    private String phoneNumber;
    private String pinCode;

}
