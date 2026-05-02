package com.rakesh.smartcity.Controller;

import com.rakesh.smartcity.Dto.PinCodeAreaRequestDto;
import com.rakesh.smartcity.Dto.PincodeAreaDto;
import com.rakesh.smartcity.service.PinCodeAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pincode")

public class PinCodeAreaController {

    @Autowired
    PinCodeAreaService pincodeService;

    // POST  /api/pincode/create
    @PostMapping("/create")
    public ResponseEntity<PincodeAreaDto> createPincodeArea(@RequestBody PinCodeAreaRequestDto pincodeRequestDto) {
        return new ResponseEntity<>(pincodeService.createPinCodeArea(pincodeRequestDto), HttpStatus.CREATED);
    }

    // GET  /api/pincode
    @GetMapping
    public ResponseEntity<List<PincodeAreaDto>> getAllPincodeAreas() {
        return ResponseEntity.ok(pincodeService.getAllPinCodeArea());
    }

    // GET  /api/pincode/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PincodeAreaDto> getPincodeAreaById(
            @PathVariable Long id) {
        return ResponseEntity.ok(pincodeService.getPinCodeAreaById(id));
    }

    // GET  /api/pincode/code?pincode=700001
    @GetMapping("/code")
    public ResponseEntity<PincodeAreaDto> getPincodeAreaByCode(
            @RequestParam String pincode) {
        return ResponseEntity.ok(pincodeService.getPinCodeAreaByPinCode(pincode));
    }


}
