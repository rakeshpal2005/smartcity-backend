package com.rakesh.smartcity.Dto;

import com.rakesh.smartcity.model.ComplainStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplainStatusUpdateDto {

    private ComplainStatus status;
    private String note;
}
