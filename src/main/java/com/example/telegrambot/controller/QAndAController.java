package com.example.telegrambot.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.telegrambot.dto.QAndADTO;
import com.example.telegrambot.dto.QAndAResponseDTO;
import com.example.telegrambot.service.QAndAService;
import com.example.telegrambot.util.ApiResponse;

@RestController
@RequestMapping("/api/v1/q-and-a")
public class QAndAController {

    private final QAndAService qAndAService;

    public QAndAController(QAndAService qAndAService) {
        this.qAndAService = qAndAService;
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



    // Create Q and A
    @PostMapping
    public ResponseEntity<ApiResponse<QAndAResponseDTO>> createQuestion(@Valid @RequestBody QAndADTO qAndADTO) {

        QAndAResponseDTO createdQandA = qAndAService.createQAndA(qAndADTO);
        // Return the created question DTO with HTTP status 201 (Created)
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("success", createdQandA));
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
