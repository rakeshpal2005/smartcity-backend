package com.rakesh.smartcity.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class PincodeAreaDto {

    private Long id;
    private String pinCode;
    private String city;
    private String state;
    private String areaName;
}
