package com.rakesh.smartcity.Controller;


import com.rakesh.smartcity.Dto.UserDto;
import com.rakesh.smartcity.Dto.WorkerCreateRequestDto;
import com.rakesh.smartcity.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/workers")
public class WorkerController {

    @Autowired
    WorkerService workerService;


    @PostMapping("/create")
    public ResponseEntity<?> createWorker(@RequestBody WorkerCreateRequestDto dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(workerService.createWorker(dto));
        } catch (Exception e) {
            e.printStackTrace(); // VERY IMPORTANT
            throw e;
        }
    }


    @GetMapping
    public ResponseEntity<List<UserDto>> getAllWorkers() {
        return ResponseEntity.ok(workerService.getAllWorker());
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getWorkerById(@PathVariable Long id) {
        return ResponseEntity.ok(workerService.getWorkerById(id));
    }


    @GetMapping("/pincode")
    public ResponseEntity<List<UserDto>> getWorkersByPincode(@RequestParam String pinCode) {
        return ResponseEntity.ok(workerService.getWorkerByPinCode(pinCode));
    }


    @GetMapping("/admin")
    public ResponseEntity<List<UserDto>> getWorkersByAdminId(@RequestParam Long adminId) {
        return ResponseEntity.ok(workerService.getWorkerByadminId(adminId));
    }
}
