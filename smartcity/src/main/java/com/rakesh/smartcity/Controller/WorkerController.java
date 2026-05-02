package com.rakesh.smartcity.Controller;


import com.rakesh.smartcity.Dto.UserDto;
import com.rakesh.smartcity.Dto.WorkerCreateRequestDto;
import com.rakesh.smartcity.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workers")
public class WorkerController {

    @Autowired
    WorkerService workerService;


    @PostMapping("/create")
    public ResponseEntity<UserDto> createWorker(@RequestBody WorkerCreateRequestDto workerRequestDto){
            return new ResponseEntity<>(workerService.createWorker(workerRequestDto), HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<UserDto>> getAllWorkers() {
        return ResponseEntity.ok(workerService.getAllWorker());
    }

    // GET  /api/workers/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getWorkerById(@PathVariable Long id) {
        return ResponseEntity.ok(workerService.getWorkerById(id));
    }

    // GET  /api/workers/pincode?pinCode=700001
    @GetMapping("/pincode")
    public ResponseEntity<List<UserDto>> getWorkersByPincode(@RequestParam String pinCode) {
        return ResponseEntity.ok(workerService.getWorkerByPinCode(pinCode));
    }

    // GET  /api/workers/admin?adminId=1
    @GetMapping("/admin")
    public ResponseEntity<List<UserDto>> getWorkersByAdminId(@RequestParam Long adminId) {
        return ResponseEntity.ok(workerService.getWorkerByadminId(adminId));
    }
}
