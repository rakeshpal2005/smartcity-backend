package com.rakesh.smartcity.Controller;

import com.rakesh.smartcity.Dto.ComplainHistoryDto;
import com.rakesh.smartcity.Dto.ComplainHistoryRequestDto;
import com.rakesh.smartcity.service.ComplainHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complain-history")
public class ComplainHistoryController {


    @Autowired
    ComplainHistoryService complainHistoryService;

    // POST  /api/complain-history/create
    @PostMapping("/create")
    public ResponseEntity<ComplainHistoryDto> createComplainHistory(@RequestBody ComplainHistoryRequestDto complainHistoryRequestDto) {
        return new ResponseEntity<>(complainHistoryService.createComplainHistory(complainHistoryRequestDto), HttpStatus.CREATED);
    }

    // GET  /api/complain-history/{complainId}
    @GetMapping("/{complainId}")
    public ResponseEntity<List<ComplainHistoryDto>> getComplainHistoryByComplainId(
            @PathVariable Long complainId) {
        return ResponseEntity.ok(
                complainHistoryService.getComplainHistoryByComplainId(complainId));
    }

}
