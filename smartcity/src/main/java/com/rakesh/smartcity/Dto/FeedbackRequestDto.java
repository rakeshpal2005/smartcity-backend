package com.rakesh.smartcity.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRequestDto {
    private Long complaintId;
    private String comment;
    private int rating;
}
