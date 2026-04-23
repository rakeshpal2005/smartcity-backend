package com.rakesh.smartcity.Dto;

import com.rakesh.smartcity.model.ImageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplainImageResponseDto {
    private Long id;
    private String imageUrl;
    private ImageType imageType;
}
