package com.rakesh.smartcity.Dto;

import com.rakesh.smartcity.model.ImageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
  public class ComplainImageRequestDto {
    Long complaintId;
    private ImageType imageType;
}
