package com.example.telegrambot.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.telegrambot.util.FileInfo;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;
    private final String uploadDir;

//    @Autowired
    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.uploadDir = uploadDir;
        this.fileStorageLocation = Paths.get(uploadDir);
//                .toAbsolutePath().normalize(); // not storing the whole path C:\..\..

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }


    public FileInfo storeFileAndGetInfo(MultipartFile file) {
        // Generate a unique filename to avoid collisions
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }

        // Generate unique filename
        String uniqueFilename = UUID.randomUUID().toString() + (extension.isEmpty() ? "" : "." + extension);

        try {
            // Copy file to the target location
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Create and return file info map
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(uniqueFilename);
            fileInfo.setFilePath(targetLocation.toString());
            fileInfo.setFileSize(file.getSize());
            fileInfo.setFileType(file.getContentType());
            fileInfo.setFileExtension(extension);

            return fileInfo;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + originalFilename, ex);
        }
    }

    // testing purposes
    public FileInfo getFileInfo(MultipartFile file) {
        // Generate a unique filename to avoid collisions
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }

        // Generate unique filename
        String uniqueFilename = UUID.randomUUID().toString() + (extension.isEmpty() ? "" : "." + extension);

        try {
            // Copy file to the target location
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFilename);


            // Create and return file info map
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(uniqueFilename);
            fileInfo.setFilePath(targetLocation.toString());
            fileInfo.setFileSize(file.getSize());
            fileInfo.setFileType(file.getContentType());
            fileInfo.setFileExtension(extension);

            return fileInfo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    //    public String storeFile(MultipartFile file) {
//        // Normalize file name
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//
//        try {
//            // Check if the file's name contains invalid characters
//            if(fileName.contains("..")) {
//                throw new RuntimeException("Filename contains invalid path sequence " + fileName);
//            }
//
//            // Copy file to the target location
//            Path targetLocation = this.fileStorageLocation.resolve(fileName);
//            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//
////            return fileName;
//            // Return the full path
//            return targetLocation.toString();
//        } catch (IOException ex) {
//            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
//        }
//    }

//    public Map<String, Object> storeFileAndGetInfo(MultipartFile file) {
//        // Generate a unique filename to avoid collisions
//        String originalFilename = file.getOriginalFilename();
//        String extension = "";
//
//        if (originalFilename != null && originalFilename.contains(".")) {
//            extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
//        }
//
//        // Generate unique filename
//        String uniqueFilename = UUID.randomUUID().toString() + (extension.isEmpty() ? "" : "." + extension);
//
//        try {
//            // Copy file to the target location
//            Path targetLocation = this.fileStorageLocation.resolve(uniqueFilename);
//            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//
//            // Create and return file info map
//            Map<String, Object> fileInfo = new HashMap<>();
//            fileInfo.put("fileName", uniqueFilename);
////            fileInfo.put("storedFileName", uniqueFilename);
//            fileInfo.put("filePath", targetLocation.toString());
//            fileInfo.put("fileSize", file.getSize());
//            fileInfo.put("fileType", file.getContentType());
//            fileInfo.put("fileExtension", extension);
////            fileInfo.put("fileUrl", "/uploads/" + uniqueFilename); // Relative URL path
//
//            return fileInfo;
//        } catch (IOException ex) {
//            throw new RuntimeException("Could not store file " + originalFilename, ex);
//        }
//    }
}
