package com.rakesh.smartcity.Dto;

import com.rakesh.smartcity.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplainRequestDto {
    private String title;
    private String description;
    private Category category;
    private String exactAddress;
    private Long pinCodeAreaId;
    private MultipartFile image;
    private Long UserId;
}
