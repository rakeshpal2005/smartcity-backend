package com.rakesh.smartcity.Controller;


import com.rakesh.smartcity.Dto.FeedbackRequestDto;
import com.rakesh.smartcity.Dto.FeedbackResponseDto;
import com.rakesh.smartcity.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    FeedbackService feedbackService;


    @PostMapping("/create")
    public ResponseEntity<FeedbackResponseDto> createFeedback(@RequestBody FeedbackRequestDto feedbackRequestDto) {
        return new ResponseEntity<>(feedbackService.createFeedBack(feedbackRequestDto), HttpStatus.CREATED);
    }


    @GetMapping("/complain/{complainId}")
    public ResponseEntity<FeedbackResponseDto> getFeedbackByComplainId(@PathVariable Long complainId) {
        return ResponseEntity.ok(feedbackService.getFeedBackByComplainId(complainId));
    }


    @GetMapping("/worker/{workerId}")
    public ResponseEntity<List<FeedbackResponseDto>> getFeedbackByWorkerId(@PathVariable Long workerId) {
        return ResponseEntity.ok(feedbackService.getFeedbackByWorkerId(workerId));
    }


    @GetMapping
    public ResponseEntity<List<FeedbackResponseDto>> getAllFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAllFeedBack());
    }

}
