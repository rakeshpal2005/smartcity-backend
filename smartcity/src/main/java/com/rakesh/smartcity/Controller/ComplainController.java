package com.rakesh.smartcity.Controller;


import com.rakesh.smartcity.Dto.ComplainRequestDto;
import com.rakesh.smartcity.Dto.ComplainResponseDto;
import com.rakesh.smartcity.Dto.ComplainStatusUpdateDto;
import com.rakesh.smartcity.model.ComplainStatus;
import com.rakesh.smartcity.service.ComplainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complains")
public class ComplainController {

    @Autowired
    ComplainService complainService;


    @PostMapping("/create")
    public ResponseEntity<ComplainResponseDto> createComplain(@RequestBody ComplainRequestDto complainRequestDto){
        return new ResponseEntity<>(complainService.createComplain(complainRequestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplainResponseDto> getComplainById(@PathVariable Long id)
    {
        return ResponseEntity.ok(complainService.getComplainById(id));
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ComplainResponseDto>> getComplainByUserId(@PathVariable Long userId){

        return ResponseEntity.ok(complainService.getComplainByUserId(userId));
    }


    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<ComplainResponseDto>> getComplainByAdminId( @PathVariable Long adminId) {
        return ResponseEntity.ok(complainService.getComplainByAdminId(adminId));
    }


    @GetMapping("/worker/{workerId}")
    public ResponseEntity<List<ComplainResponseDto>> getComplainByWorkerId(@PathVariable Long workerId) {

        return ResponseEntity.ok(complainService.getComplainByWorkerId(workerId));
    }



    @GetMapping("/pincode/{pincodeAreaId}")
    public ResponseEntity<List<ComplainResponseDto>> getComplainByPincodeAreaId(   @PathVariable Long pincodeAreaId) {
        return ResponseEntity.ok(complainService.getComplainByPinCodeAreaId(pincodeAreaId));
    }


    @PostMapping("/{complainId}/assign-worker/{workerId}")
    public ResponseEntity<ComplainResponseDto> assignWorkerToComplain(@PathVariable Long complainId,  @PathVariable Long workerId){
        return ResponseEntity.ok(complainService.assignWorkerToComplain(complainId, workerId));
    }



    @PutMapping("/{complainId}/status")
    public ResponseEntity<ComplainResponseDto> updateComplainStatus( @PathVariable Long complainId,  @RequestBody ComplainStatusUpdateDto complainStatusUpdateDto){
        return ResponseEntity.ok(complainService.updateComplainStatus(complainId, complainStatusUpdateDto));
    }
}
