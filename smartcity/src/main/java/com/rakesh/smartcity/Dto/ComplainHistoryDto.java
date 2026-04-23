package com.rakesh.smartcity.Dto;

import com.rakesh.smartcity.model.ComplainStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@Data
@NoArgsConstructor
public class ComplainHistoryDto {

    private Long id;
    private ComplainStatus status;
    private String note;
    private LocalDateTime changedAt;
    private String changedByName;
}
