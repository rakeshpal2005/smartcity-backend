package com.rakesh.smartcity.service;


import com.rakesh.smartcity.Dto.ComplainHistoryDto;
import com.rakesh.smartcity.Dto.ComplainHistoryRequestDto;
import com.rakesh.smartcity.model.Complain;
import com.rakesh.smartcity.model.ComplainStatus;
import com.rakesh.smartcity.model.Complainhistory;
import com.rakesh.smartcity.model.User;
import com.rakesh.smartcity.repo.ComplainHistoryRepo;
import com.rakesh.smartcity.repo.ComplainRepo;
import com.rakesh.smartcity.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplainHistoryService {

    @Autowired
    private ComplainHistoryRepo complainHistoryRepo;

    @Autowired
    private ComplainRepo complainRepo;

    @Autowired
    private UserRepo userRepo;

    public ComplainHistoryDto createHistory(Complain complain , String note , User user, ComplainStatus complainStatus){

        Complainhistory history = new Complainhistory();
        history.setComplaint(complain);
        history.setComplainStatus(complainStatus);
        history.setNote(note);
        history.setChangedBy(user);
        history.setChangedAt(LocalDateTime.now());
        Complainhistory saveComplainHistory = complainHistoryRepo.save(history);
        return mapToDto(saveComplainHistory);
    }



    public ComplainHistoryDto createComplainHistory(ComplainHistoryRequestDto dto) {

        // 1. Fetch Complain using ID
        Complain complain = complainRepo.findById(dto.getComplainId())
                .orElseThrow(() -> new RuntimeException("Complain not found"));

        // 2. Fetch User using ID
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Call my existing method
        return createHistory(
                complain,
                dto.getNote(),
                user,
                dto.getStatus()
        );
    }

 public List<ComplainHistoryDto> getComplainHistoryByComplainId(Long complainId) {
     List<Complainhistory> complainHistory = complainHistoryRepo.findByComplainId(complainId);
     if (complainHistory.isEmpty()) {
         throw new RuntimeException("No history found for this complaint id: " + complainId);
     }
     return complainHistory.stream()
             .map(this::mapToDto)
             .collect( Collectors.toList());
 }
private ComplainHistoryDto mapToDto(Complainhistory complainhistory){

        ComplainHistoryDto complainHistoryDto = new ComplainHistoryDto();

        complainHistoryDto.setId(complainhistory.getId());
    complainHistoryDto.setStatus(complainhistory.getComplainStatus());
    complainHistoryDto.setNote(complainhistory.getNote());
    complainHistoryDto.setChangedAt(complainhistory.getChangedAt());
    complainHistoryDto.setChangedByName(complainhistory.getChangedBy().getName());

    return   complainHistoryDto;
}
}
