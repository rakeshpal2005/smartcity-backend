package com.rakesh.smartcity.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponseDto {

    private Long id;
    private String comment;
    private int rating;
    private LocalDateTime createdAt;
    private String givenByName;
    private String givenToName;
}
