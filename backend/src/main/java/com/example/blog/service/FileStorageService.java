package com.example.blog.service;

import com.example.blog.exception.BadRequestException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    
    private final Path uploadDir = Paths.get("uploads/media");
    
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!");
        }
    }
    
    public String storeFile(MultipartFile file) {
        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || 
            (!contentType.startsWith("image/") && !contentType.startsWith("video/"))) {
            throw new BadRequestException("Only image and video files are allowed");
        }
        
        // Validate file size (10MB limit)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new BadRequestException("File size must not exceed 10MB");
        }
        
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + extension;
        
        try {
            Path targetLocation = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/media/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
    
    public void deleteFile(String fileUrl) {
        try {
            String filename = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            Path filePath = uploadDir.resolve(filename);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // Log but don't fail
        }
    }
}