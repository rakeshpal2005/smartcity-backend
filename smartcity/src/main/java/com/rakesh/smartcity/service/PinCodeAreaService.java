package com.rakesh.smartcity.service;

import com.rakesh.smartcity.Dto.PincodeAreaDto;
import com.rakesh.smartcity.Dto.PinCodeAreaRequestDto;
import com.rakesh.smartcity.model.PinCodeArea;
import com.rakesh.smartcity.repo.PincodeAreaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class PinCodeAreaService {

@Autowired
private PincodeAreaRepo pincodeAreaRepo;


    public PincodeAreaDto createPinCodeArea(PinCodeAreaRequestDto pinCodeAreaRequestDto){
        PinCodeArea pinCodeArea=mapToEntity(pinCodeAreaRequestDto);
        PinCodeArea savePincodeArea= pincodeAreaRepo.save(pinCodeArea);
        return mapToDto(savePincodeArea);
    }

public List<PincodeAreaDto> getAllPinCodeArea(){
    List<PinCodeArea> pinCodeAreas=pincodeAreaRepo.findAll();
    return pinCodeAreas.stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
}



public PincodeAreaDto getPinCodeAreaByPinCode(String pincode) {
    PinCodeArea pinCodeArea = pincodeAreaRepo.findByPinCode(pincode)
            .orElseThrow(() -> new RuntimeException("not found"));
            return mapToDto(pinCodeArea);
}


public PincodeAreaDto getPinCodeAreaById(Long id){
        PinCodeArea pinCodeArea = pincodeAreaRepo.findById(id)
                .orElseThrow(() ->new RuntimeException("not found.."));
        return mapToDto(pinCodeArea);
}

    private PincodeAreaDto mapToDto(PinCodeArea pinCodeArea) {
        PincodeAreaDto dto = new PincodeAreaDto();

        dto.setId(pinCodeArea.getId());
        dto.setPinCode(pinCodeArea.getPincode());
        dto.setCity(pinCodeArea.getCity());
        dto.setState(pinCodeArea.getState());
        dto.setAreaName(pinCodeArea.getAreaname());

        return dto;
    }

    private PinCodeArea mapToEntity(PinCodeAreaRequestDto dto) {
        PinCodeArea pinCodeArea = new PinCodeArea();

        pinCodeArea.setPincode(dto.getPinCode());
        pinCodeArea.setCity(dto.getCity());
        pinCodeArea.setState(dto.getState());
        pinCodeArea.setAreaname(dto.getAreaName());

        return pinCodeArea;
    }
}
