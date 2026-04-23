package com.rakesh.smartcity.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class WorkerCreateRequestDto {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String pinCode;
    private Long adminId;
}
