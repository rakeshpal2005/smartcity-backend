package com.rakesh.smartcity.Controller;

import com.rakesh.smartcity.Dto.ComplainImageResponseDto;
import com.rakesh.smartcity.model.ImageType;
import com.rakesh.smartcity.service.ComplainImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/images")
public class ComplainImageController {

    @Autowired
    ComplainImageService complainImageService;


    @PostMapping("/upload")
    public ResponseEntity<ComplainImageResponseDto> uploadImage(@RequestParam Long complainId,@RequestParam ImageType imageType,@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(complainImageService.uploadImage(complainId,imageType,file), HttpStatus.CREATED);
    }


    @GetMapping("/complain/{complainId}")
    public ResponseEntity<List<ComplainImageResponseDto>> getImagesByComplainId(
            @PathVariable Long complainId) {
        return ResponseEntity.ok(complainImageService.getImageByComplainId(complainId));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteImage(
            @PathVariable Long id) {
        complainImageService.deleteImage(id);
        return ResponseEntity.ok("Image deleted successfully.");
    }
}