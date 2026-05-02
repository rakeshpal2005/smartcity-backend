package com.rakesh.smartcity.Dto;


import com.rakesh.smartcity.model.ComplainStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor
@NoArgsConstructor
public class ComplainHistoryRequestDto {

    private Long complainId;
    private Long userId;
    private String note;
    private ComplainStatus status;
}
