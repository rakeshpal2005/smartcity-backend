package com.rakesh.smartcity.service;

import com.rakesh.smartcity.Dto.*;
import com.rakesh.smartcity.model.*;
import com.rakesh.smartcity.repo.ComplainRepo;
import com.rakesh.smartcity.repo.PincodeAreaRepo;
import com.rakesh.smartcity.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import java.util.stream.Collectors;

@Service
public class ComplainService {

    @Autowired
   private ComplainRepo complainRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    PincodeAreaRepo pincodeAreaRepo;

    public ComplainResponseDto createComplain(ComplainRequestDto complainRequestDto){

       User user = userRepo.findById(complainRequestDto.getUserId())        // not use//
               .orElseThrow(() -> new RuntimeException("User not found"));

          Complain complain=mapToEntity(complainRequestDto);
          complain.setUser(user);
        complain.setStatus(ComplainStatus.SUBMITTED);
        complain.setCreatedAt(LocalDateTime.now());
          Complain saveComplain=complainRepo.save(complain);
          return mapToDto(saveComplain);
    }


    public ComplainResponseDto getComplainById(Long id){
    Complain complain = complainRepo.findById(id)
          .orElseThrow(() -> new RuntimeException("Complain not Found"));
     return mapToDto(complain);
}


public List<ComplainResponseDto> getComplainByUserId(Long UserId){

        User user = userRepo.findById(UserId)
                .orElseThrow(() -> new RuntimeException("user not found"));

         List<Complain> complains= complainRepo.findByUserId(user.getId());
    return complains.stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
}

public List<ComplainResponseDto>getComplainByAdminId(Long AdminId){
        List<Complain> complains = complainRepo.findByAssignedAdminId(AdminId);
    return complains.stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());

}

    public List<ComplainResponseDto>getComplainByWorkerId(Long WorkerId) {
        List<Complain> complains = complainRepo.findByAssignedWorkerId(WorkerId);
        return complains.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

    }

    public List<ComplainResponseDto> getComplainByPinCodeAreaId(Long PinCodeAreaId){
        List<Complain> complains = complainRepo.findByPinCodeAreaId(PinCodeAreaId);
        return complains.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }



    public ComplainResponseDto assignWorkerToComplain(Long ComplainId , Long WorkerId){

          Complain complain = complainRepo.findById(ComplainId)
                  .orElseThrow(() -> new RuntimeException("Complain Not Found..."));

          User user = userRepo.findById(WorkerId)
                  .orElseThrow(() -> new RuntimeException("User Not found "));

        if (complain.getStatus() == ComplainStatus.ASSIGNED) {
            throw new RuntimeException("Worker Already Assigned");
        }

          if(user.getRole()==Role.WORKER){

              complain.setAssignedWorker(user);
          }
          else{
              throw new RuntimeException("this is not a worker");
          }
        if (user.getAdmin() == null) {
            throw new RuntimeException("Worker has no admin assigned");
        }
        complain.setAssignedAdmin(user.getAdmin());

          complain.setStatus(ComplainStatus.ASSIGNED);
          complain.setUpdatedAt(LocalDateTime.now());
          Complain saveComplain = complainRepo.save(complain);
          return mapToDto(saveComplain);
    }


    public ComplainResponseDto updateComplainStatus(Long id , ComplainStatusUpdateDto complainStatusUpdateDto){
   Complain complain = complainRepo.findById(id)
           .orElseThrow(() -> new RuntimeException("Complain not found with this id "+id));

      complain.setStatus(complainStatusUpdateDto.getStatus());
        complain.setUpdatedAt(LocalDateTime.now());

        Complain updatedcomplain = complainRepo.save(complain);
    return mapToDto(updatedcomplain);
    }



    private ComplainResponseDto mapToDto(Complain complain) {

        ComplainResponseDto complainResponseDto = new ComplainResponseDto();

        complainResponseDto.setId(complain.getId());
        complainResponseDto.setDescription(complain.getDescription());
        complainResponseDto.setTitle(complain.getTitle());
        complainResponseDto.setCategory(complain.getCategory());
        complainResponseDto.setStatus(complain.getStatus());
        complainResponseDto.setExactAddress(complain.getExactAddress());
        complainResponseDto.setCreatedAt(complain.getCreatedAt());
        complainResponseDto.setUpdatedAt(complain.getUpdatedAt());
        complainResponseDto.setResolvedAt(complain.getResolvedAt());
        complainResponseDto.setPinCodeArea(mapPinCodeAreaToDto(complain.getPinCodeArea()));

        if (complain.getUser() != null) {
            complainResponseDto.setUser(mapUserToDto(complain.getUser()));
        }

        if (complain.getAssignedAdmin() != null) {
            complainResponseDto.setAssignedAdmin(mapUserToDto(complain.getAssignedAdmin()));
        }

        if (complain.getAssignedWorker() != null) {
            complainResponseDto.setAssignedWorker(mapUserToDto(complain.getAssignedWorker()));
        }

        if (complain.getComplainImages() != null) {
            complainResponseDto.setComplaintImages(
                    complain.getComplainImages().stream()
                            .map(this::mapImageToDto)
                            .collect(Collectors.toList())
            );
        }

        return complainResponseDto;
    }

    private  Complain mapToEntity(ComplainRequestDto complainRequestDto) {

        Complain complain = new Complain();
         complain.setTitle(complainRequestDto.getTitle());
         complain.setDescription(complainRequestDto.getDescription());
        complain.setCategory(complainRequestDto.getCategory());
        complain.setExactAddress(complainRequestDto.getExactAddress());

        PinCodeArea pinCodeArea = pincodeAreaRepo.findById(complainRequestDto.getPinCodeAreaId())
                .orElseThrow(() -> new RuntimeException("PinCodeArea not found"));

        complain.setPinCodeArea(pinCodeArea);

        return complain;
    }



    private PincodeAreaDto mapPinCodeAreaToDto(PinCodeArea pinCodeArea) {     // use in mapToDto method

        PincodeAreaDto dto = new PincodeAreaDto();

        dto.setId(pinCodeArea.getId());
        dto.setPinCode(pinCodeArea.getPincode());
        dto.setCity(pinCodeArea.getCity());
        dto.setState(pinCodeArea.getState());
        dto.setAreaName(pinCodeArea.getAreaname());
        return dto;
    }

    private UserDto mapUserToDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setPinCode(user.getPinCode());
        userDto.setRole(user.getRole());

        return userDto;
    }

    private ComplainImageResponseDto mapImageToDto(ComplainImage complainImage) {
        ComplainImageResponseDto dto = new ComplainImageResponseDto();

        dto.setId(complainImage.getId());
        dto.setImageUrl(complainImage.getImageUrl());
        dto.setImageType(complainImage.getImageType());

        return dto;
    }
}
