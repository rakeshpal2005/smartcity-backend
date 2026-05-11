package com.rakesh.smartcity.Controller;

import com.rakesh.smartcity.Dto.ComplainHistoryDto;
import com.rakesh.smartcity.Dto.ComplainHistoryRequestDto;
import com.rakesh.smartcity.service.ComplainHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/complain-history")
public class ComplainHistoryController {


    @Autowired
    ComplainHistoryService complainHistoryService;


    @PostMapping("/create")
    public ResponseEntity<ComplainHistoryDto> createComplainHistory(@RequestBody ComplainHistoryRequestDto complainHistoryRequestDto) {
        return new ResponseEntity<>(complainHistoryService.createComplainHistory(complainHistoryRequestDto), HttpStatus.CREATED);
    }


    @GetMapping("/{complainId}")
    public ResponseEntity<List<ComplainHistoryDto>> getComplainHistoryByComplainId(
            @PathVariable Long complainId) {
        return ResponseEntity.ok(
                complainHistoryService.getComplainHistoryByComplainId(complainId));
    }

}
