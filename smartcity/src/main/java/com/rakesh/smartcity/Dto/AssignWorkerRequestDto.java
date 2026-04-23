package com.rakesh.smartcity.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class AssignWorkerRequestDto {
    private Long complaintId;
    private Long workerId;
}
