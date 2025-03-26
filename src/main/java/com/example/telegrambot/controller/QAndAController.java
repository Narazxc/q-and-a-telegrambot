package com.example.telegrambot.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.telegrambot.dto.QAndADTO;
import com.example.telegrambot.dto.QAndAResponseDTO;
import com.example.telegrambot.service.QAndAService;
import com.example.telegrambot.util.ApiResponse;
import com.example.telegrambot.config.FileUploadConfig;
import com.example.telegrambot.service.FileStorageService;
import com.example.telegrambot.util.FileInfo;

@RestController
@RequestMapping("/api/v1/q-and-a")
public class QAndAController {

    private final QAndAService qAndAService;

    //  File
    private final FileStorageService fileStorageService;
    private final FileUploadConfig fileUploadConfig;

    public QAndAController(QAndAService qAndAService,
                           FileStorageService fileStorageService,
                           FileUploadConfig fileUploadConfig)
    {
        this.qAndAService = qAndAService;
        this.fileStorageService = fileStorageService;
        this.fileUploadConfig = fileUploadConfig;
    }

    // Get all Q&As
    @GetMapping
    public ResponseEntity<ApiResponse<List<QAndAResponseDTO>>> getAllQAndAs() {

        List<QAndAResponseDTO> allQAndAs = qAndAService.getAllQAndAs();

        // Structuring response
        ApiResponse<List<QAndAResponseDTO>> response = new ApiResponse<>("success", allQAndAs);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Get Q and A based on ID
    @GetMapping("/id/{id}")
    public ResponseEntity<ApiResponse<QAndAResponseDTO>> getQAndA(@PathVariable UUID id) {

        QAndAResponseDTO qAndA = qAndAService.getQAndA(id);

        // Structuring response
        ApiResponse<QAndAResponseDTO> response = new ApiResponse<>("success", qAndA);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Get all Q and A based on module name
    @GetMapping("/modules/{moduleName}")
    public ResponseEntity<ApiResponse<List<QAndAResponseDTO>>> getQuestionsByModuleName(@PathVariable String moduleName) {

        List<QAndAResponseDTO> qAndAByModule = qAndAService.getQuestionsByModuleName(moduleName);

        // Structuring response
        ApiResponse<List<QAndAResponseDTO>> response = new ApiResponse<>("success", qAndAByModule);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


//    // Create Q and A for JSON only (WORKING)
//    @PostMapping
//    public ResponseEntity<ApiResponse<QAndAResponseDTO>> createQuestion(@Valid @RequestBody QAndADTO qAndADTO) {
//
//        QAndAResponseDTO createdQandA = qAndAService.createQAndA(qAndADTO, null);
//
//        //  Return the created question DTO with HTTP status 201 (Created)
//        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("success", createdQandA));
//    }


    // Create Q and A for Form-data (incoming data with file)
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<QAndAResponseDTO>> createQuestion(
            @RequestParam(value="answerImageFile", required = false) MultipartFile answerImageFile,
            @ModelAttribute QAndADTO qAndADTO) {


        if (answerImageFile != null) {
            FileInfo fileInfo = fileStorageService.getFileInfo(answerImageFile);
            System.out.println(fileInfo.getFileName() + "\n"
                    + fileInfo.getFilePath() + "\n"
                    + fileInfo.getFileSize() + "\n"
                    + fileInfo.getFileType() + "\n"
                    + fileInfo.getFileExtension());
        }

        System.out.println("qAndADTO:" + qAndADTO);


        if (answerImageFile != null && !answerImageFile.isEmpty()) {

            // Handle file upload logic
            if (answerImageFile.getSize() > fileUploadConfig.getMaxFileSize()) { // Example: 5MB limit
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(null);
            }

            String contentType = answerImageFile.getContentType();
            if (contentType == null || !fileUploadConfig.getAllowedTypes().contains(answerImageFile.getContentType())) {
                // need custom exception here
                throw new IllegalArgumentException("Please input images only!");
            }

            // You could save the file, process it, etc.
            QAndAResponseDTO createdQandA = qAndAService.createQAndA(qAndADTO, answerImageFile);

            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("success", createdQandA));
        } else {
            // Handle case when no file is provided
            QAndAResponseDTO createdQandA = qAndAService.createQAndA(qAndADTO, null);

            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("success", createdQandA));
        }
    }

    // Update an existing Q&A
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<QAndAResponseDTO>> updateQAndA(@PathVariable UUID id, @Valid @RequestBody QAndADTO qAndADTO) {

        QAndAResponseDTO qAndAResponseDTO = qAndAService.updateQAndA(id, qAndADTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("success", qAndAResponseDTO));
    }

    // Delete a Q&A by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<QAndAResponseDTO>> deleteQAndA(@PathVariable UUID id) {

        qAndAService.deleteQAndA(id);
        return ResponseEntity.noContent().build();
    }

//    @PostMapping("/bulk-insert")
//    public ResponseEntity<List<QAndAResponseDTO>> insertQAndAs(@RequestBody List<QAndADTO> qAndADTOs) {
//        List<QAndAResponseDTO> savedQAndAs = qAndAService.createQAndAs(qAndADTOs);
//        return ResponseEntity.ok(savedQAndAs);
//    }
}
