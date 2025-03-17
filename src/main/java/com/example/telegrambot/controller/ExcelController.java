package com.example.telegrambot.controller;

import com.example.telegrambot.dto.QAndADTO;
import com.example.telegrambot.dto.QAndAResponseDTO;
import com.example.telegrambot.service.ExcelReaderService;
import com.example.telegrambot.service.QAndAService;
import com.example.telegrambot.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/excel")
public class ExcelController {

    private final ExcelReaderService excelReaderService;
    private final QAndAService qAndAService;

    public ExcelController(ExcelReaderService excelReaderService, QAndAService qAndAService) {
        this.excelReaderService = excelReaderService;
        this.qAndAService = qAndAService;
    }


    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<?>> uploadExcelFile(@RequestParam("file") MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("error", "Please upload an Excel file."));
        }


        // Use ExcelService to convert the file into DTOs
        List<?> result = excelReaderService.readExcelFile(file);

        if (!result.isEmpty()) {
            Object firstElement = result.get(0);
            if (firstElement instanceof QAndADTO) {
                System.out.println("This is a list of QAndADTOs");

                // Use QAndAService to save DTOs in bulk
                List<QAndAResponseDTO> savedQAndAs = qAndAService.createQAndAs((List<QAndADTO>) result);
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("success", savedQAndAs));

            } else if (firstElement instanceof String) {
                System.out.println("This is a list of error messages");

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("error", (List<String>) result));
            }
        } else {
            System.out.println("The list is empty");
        }

        // Add a fallback response for any other cases
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("error", "Unable to process the Excel file."));
    }
}
