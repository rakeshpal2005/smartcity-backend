package com.rakesh.smartcity.service;

import com.rakesh.smartcity.Dto.*;
import com.rakesh.smartcity.Exception.BadRequestException;
import com.rakesh.smartcity.Exception.ResourceNotFoundException;
import com.rakesh.smartcity.model.*;
import com.rakesh.smartcity.repo.ComplainRepo;
import com.rakesh.smartcity.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    ComplainImageService complainImageService;
    @Autowired
    private ComplainHistoryService complainHistoryService;

    @Transactional
    public ComplainResponseDto createComplain(ComplainRequestDto complainRequestDto) {

        User user = userRepo.findById(complainRequestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        User admin = userRepo.findByRoleAndPinCode(
                        Role.ADMIN,
                        complainRequestDto.getPinCode())
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No admin available for this pinCode"));

        Complain complain = mapToEntity(complainRequestDto);
        complain.setUser(user);
        complain.setAssignedAdmin(admin);

        Complain saveComplain = complainRepo.save(complain);

        if (complainRequestDto.getImage() != null &&
                !complainRequestDto.getImage().isEmpty()) {

            complainImageService.uploadImage(
                    saveComplain.getId(),
                    ImageType.BEFORE,
                    complainRequestDto.getImage());
        }

        complainHistoryService.createHistory(
                saveComplain,
                "Complaint submitted",
                user,
                ComplainStatus.SUBMITTED
        );

        complainRepo.flush();

        Complain freshComplain = complainRepo.findById(saveComplain.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Complaint not found"));

        return mapToDto(freshComplain);
    }

    public ComplainResponseDto getComplainById(Long id) {
        Complain complain = complainRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Complaint not found"));
        return mapToDto(complain);
    }

    public List<ComplainResponseDto> getComplainByUserId(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
        List<Complain> complains = complainRepo.findByUserId(user.getId());
        return complains.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ComplainResponseDto> getComplainByAdminId(Long adminId) {
        List<Complain> complains = complainRepo.findByAssignedAdminId(adminId);
        return complains.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ComplainResponseDto> getComplainByWorkerId(Long workerId) {
        List<Complain> complains = complainRepo.findByAssignedWorkerId(workerId);
        return complains.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ComplainResponseDto> getComplainByPinCode(String pinCode) {
        List<Complain> complains = complainRepo.findByPinCode(pinCode);
        return complains.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ComplainResponseDto assignWorkerToComplain(
            Long complainId,
            Long workerId) {

        Complain complain = complainRepo.findById(complainId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Complaint not found"));

        User user = userRepo.findById(workerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (complain.getStatus() == ComplainStatus.ASSIGNED) {
            throw new BadRequestException("Worker already assigned");
        }

        if (complain.getStatus() == ComplainStatus.RESOLVED ||
                complain.getStatus() == ComplainStatus.REJECTED) {
            throw new BadRequestException(
                    "Cannot assign worker to completed complaint");
        }

        if (user.getRole() != Role.WORKER) {
            throw new BadRequestException("User is not a worker");
        }

        if (!user.getPinCode().equals(complain.getPinCode())) {
            throw new BadRequestException(
                    "Worker pincode does not match complaint pincode");
        }

        if (user.getAdmin() == null) {
            throw new BadRequestException("Worker has no admin assigned");
        }

        complain.setAssignedWorker(user);
        complain.setAssignedAdmin(user.getAdmin());
        Complain saveComplain = complainRepo.save(complain);


        complainHistoryService.createHistory(
                saveComplain,
                "Worker " + user.getName() + " assigned",
                user.getAdmin(),
                ComplainStatus.ASSIGNED
        );

        return mapToDto(saveComplain);
    }

    public ComplainResponseDto updateComplainStatus(
            Long id,
            ComplainStatusUpdateDto complainStatusUpdateDto) {

        Complain complain = complainRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Complaint not found with id " + id));

        complain.setStatus(complainStatusUpdateDto.getStatus());

        if (complainStatusUpdateDto.getStatus() == ComplainStatus.RESOLVED) {
            complain.setResolvedAt(LocalDateTime.now());
        }

        Complain updatedComplain = complainRepo.save(complain);


        User changedBy;
        if (complainStatusUpdateDto.getStatus() == ComplainStatus.IN_PROGRESS ||
                complainStatusUpdateDto.getStatus() == ComplainStatus.RESOLVED) {

            changedBy = updatedComplain.getAssignedWorker() != null
                    ? updatedComplain.getAssignedWorker()
                    : updatedComplain.getAssignedAdmin();
        } else {

            changedBy = updatedComplain.getAssignedAdmin();
        }

        if (changedBy != null) {
            complainHistoryService.createHistory(
                    updatedComplain,
                    complainStatusUpdateDto.getNote(),
                    changedBy,
                    complainStatusUpdateDto.getStatus()
            );
        }

        return mapToDto(updatedComplain);
    }

    public List<ComplainResponseDto> getAllComplains() {
        return complainRepo.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ComplainResponseDto mapToDto(Complain complain) {
        ComplainResponseDto complainResponseDto = new ComplainResponseDto();
        complainResponseDto.setId(complain.getId());
        complainResponseDto.setDescription(complain.getDescription());
        complainResponseDto.setTitle(complain.getTitle());
        complainResponseDto.setCategory(complain.getCategory());
        complainResponseDto.setStatus(complain.getStatus());
        complainResponseDto.setExactAddress(complain.getExactAddress());
        complainResponseDto.setLandMark(complain.getLandmark());
        complainResponseDto.setPinCode(complain.getPinCode());
        complainResponseDto.setAreaName(complain.getAreaName());
        complainResponseDto.setCreatedAt(complain.getCreatedAt());
        complainResponseDto.setUpdatedAt(complain.getUpdatedAt());
        complainResponseDto.setResolvedAt(complain.getResolvedAt());
        if (complain.getUser() != null)
            complainResponseDto.setUser(mapUserToDto(complain.getUser()));
        if (complain.getAssignedAdmin() != null)
            complainResponseDto.setAssignedAdmin(mapUserToDto(complain.getAssignedAdmin()));
        if (complain.getAssignedWorker() != null)
            complainResponseDto.setAssignedWorker(mapUserToDto(complain.getAssignedWorker()));
        if (complain.getComplainImages() != null)
            complainResponseDto.setComplaintImages(
                    complain.getComplainImages().stream()
                            .map(this::mapImageToDto)
                            .collect(Collectors.toList()));
        return complainResponseDto;
    }

    private Complain mapToEntity(ComplainRequestDto complainRequestDto) {
        Complain complain = new Complain();
        complain.setTitle(complainRequestDto.getTitle());
        complain.setDescription(complainRequestDto.getDescription());
        complain.setCategory(complainRequestDto.getCategory());
        complain.setExactAddress(complainRequestDto.getExactAddress());
        complain.setLandmark(complainRequestDto.getLandMark());
        complain.setPinCode(complainRequestDto.getPinCode());
        complain.setAreaName(complainRequestDto.getAreaName());
        return complain;
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
