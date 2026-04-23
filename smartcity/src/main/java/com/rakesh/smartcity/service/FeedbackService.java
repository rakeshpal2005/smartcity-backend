package com.rakesh.smartcity.service;

import com.rakesh.smartcity.Dto.FeedbackRequestDto;
import com.rakesh.smartcity.Dto.FeedbackResponseDto;
import com.rakesh.smartcity.model.Complain;
import com.rakesh.smartcity.model.ComplainStatus;
import com.rakesh.smartcity.model.Feedback;
import com.rakesh.smartcity.repo.ComplainRepo;
import com.rakesh.smartcity.repo.FeedbackRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class FeedbackService {

    @Autowired
    private FeedbackRepo feedbackRepo;

    @Autowired
    private ComplainRepo complainRepo;

    public FeedbackResponseDto createFeedBack(FeedbackRequestDto feedbackRequestDto) {

        Long complainId = feedbackRequestDto.getComplaintId();

        Complain complain = complainRepo.findById(complainId)
                .orElseThrow(() -> new RuntimeException("Complain not found with this id: " + complainId));

        if (complain.getStatus() != ComplainStatus.RESOLVED) {
            throw new RuntimeException("Feedback can be given only after complaint is resolved");
        }

        if (feedbackRepo.findByComplaintId(complainId).isPresent()) {
            throw new RuntimeException("Feedback already exists for this complaint");
        }

        if (complain.getAssignedWorker() == null) {
            throw new RuntimeException("No worker assigned to this complaint");
        }

        Feedback feedback = mapToEntity(feedbackRequestDto);
        feedback.setComplains(complain);
        feedback.setGivenBy(complain.getUser());
        feedback.setGivenTo(complain.getAssignedWorker());
        feedback.setCreatedAt(LocalDateTime.now());

        Feedback saveFeedBack = feedbackRepo.save(feedback);
        return mapToDto(saveFeedBack);
    }

    public FeedbackResponseDto getFeedBackByComplainId(Long complainId) {
        Feedback feedback = feedbackRepo.findByComplaintId(complainId)
                .orElseThrow(() -> new RuntimeException("Feedback not found with complaint id: " + complainId));

        return mapToDto(feedback);
    }

    public List<FeedbackResponseDto> getFeedbackByWorkerId(Long workerId) {

        List<Feedback> feedbackList = feedbackRepo.findByWorkerId(workerId);

        if (feedbackList.isEmpty()) {
            throw new RuntimeException("Feedback not found for this worker id: " + workerId);
        }

        return feedbackList.stream()
                .map(this::mapToDto)
                .collect( Collectors.toList());
    }

    public List<FeedbackResponseDto> getAllFeedBack() {
        List<Feedback> feedbackList = feedbackRepo.findAll();

        return feedbackList.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private Feedback mapToEntity(FeedbackRequestDto dto) {
        Feedback feedback = new Feedback();

        feedback.setComment(dto.getComment());
        feedback.setRating(dto.getRating());

        return feedback;
    }

    private FeedbackResponseDto mapToDto(Feedback feedback) {
        FeedbackResponseDto dto = new FeedbackResponseDto();

        dto.setId(feedback.getId());
        dto.setComment(feedback.getComment());
        dto.setRating(feedback.getRating());
        dto.setCreatedAt(feedback.getCreatedAt());

        if (feedback.getGivenBy() != null) {
            dto.setGivenByName(feedback.getGivenBy().getName());
        }

        if (feedback.getGivenTo() != null) {
            dto.setGivenToName(feedback.getGivenTo().getName());
        }

        return dto;
    }
}
