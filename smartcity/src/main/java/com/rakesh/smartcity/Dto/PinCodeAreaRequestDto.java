package com.rakesh.smartcity.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PinCodeAreaRequestDto {
    private String pinCode;
    private String city;
    private String state;
    private String areaName;
}
