package com.rakesh.smartcity.Dto;


import com.rakesh.smartcity.model.Category;
import com.rakesh.smartcity.model.ComplainStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplainResponseDto {
    private Long id;
    private String title;
    private String description;
    private Category category;
    private ComplainStatus status;
    private String exactAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;
    private UserDto user;
    private UserDto assignedAdmin;
    private UserDto assignedWorker;
    private PincodeAreaDto pinCodeArea;
    private List<ComplainImageResponseDto> complaintImages;
}
