package com.rakesh.smartcity.service;


import com.rakesh.smartcity.Dto.ComplainImageResponseDto;
import com.rakesh.smartcity.Exception.BadRequestException;
import com.rakesh.smartcity.Exception.FileStorageException;
import com.rakesh.smartcity.Exception.ResourceNotFoundException;
import com.rakesh.smartcity.model.Complain;
import com.rakesh.smartcity.model.ComplainImage;
import com.rakesh.smartcity.model.ImageType;
import com.rakesh.smartcity.repo.ComplainImageRepo;
import com.rakesh.smartcity.repo.ComplainRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplainImageService {
    @Autowired
    private ComplainRepo complainRepo;

    @Autowired
    private ComplainImageRepo complainImageRepo;

    public ComplainImageResponseDto uploadImage(Long complainId, ImageType imageType, MultipartFile file) {

        //check file
        if (file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                (!contentType.equals("image/jpeg") &&
                        !contentType.equals("image/png") &&
                        !contentType.equals("image/jpg"))) {

            throw new BadRequestException("Only JPG and PNG images are allowed");
        }

        // fetch complaint
        Complain complain = complainRepo.findById(complainId)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found"));

        try {
            //create unique file name
            String originalName = file.getOriginalFilename();
            if (originalName == null) {
                throw new BadRequestException("Invalid file name");
            }
            originalName = originalName.replaceAll("\\s+", "_");
            String fileName = System.currentTimeMillis() + "_" + originalName;
            // set upload directory
            String uploadDir = "uploads/";

            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            //create path
            Path filePath = Paths.get(uploadDir, fileName);

            //save file to folder
            Files.write(filePath, file.getBytes());

            // create entity
            ComplainImage complainImage = new ComplainImage();
            complainImage.setComplain(complain);

            if (complain.getComplainImages() == null) {
                complain.setComplainImages(new ArrayList<>());
            }

            complain.getComplainImages().add(complainImage);

            String imageUrl = "/uploads/" + fileName;
            complainImage.setImageUrl(imageUrl);
            complainImage.setImageType(imageType);

            // save to DB
            ComplainImage savedImage = complainImageRepo.save(complainImage);

            //  return DTO
            return mapToDto(savedImage);

        } catch (IOException e) {
            throw new FileStorageException("Failed to upload image");        }
    }

    private ComplainImageResponseDto mapToDto(ComplainImage image) {
        ComplainImageResponseDto dto = new ComplainImageResponseDto();

        dto.setId(image.getId());
        dto.setImageUrl(image.getImageUrl());
        dto.setImageType(image.getImageType());

        return dto;
    }

    public void deleteImage(Long id){
        ComplainImage complainImage = complainImageRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));

        try {
            // delete file from folder
            String path = complainImage.getImageUrl().replace("/uploads/", "uploads/");
            Files.deleteIfExists(Paths.get(path));

        } catch (IOException e) {
            throw new FileStorageException("Failed to delete image file");        }

        // delete from database
        complainImageRepo.delete(complainImage);
    }

    public List<ComplainImageResponseDto> getImageByComplainId(Long complainId){
        List<ComplainImage> images = complainImageRepo.findByComplainId(complainId);

        return images.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

}
