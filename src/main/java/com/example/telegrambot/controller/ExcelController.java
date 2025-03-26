package com.example.telegrambot.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.example.telegrambot.dto.QAndADTO;
import com.example.telegrambot.dto.QAndAResponseDTO;
import com.example.telegrambot.service.ExcelReaderService;
import com.example.telegrambot.service.QAndAService;
import com.example.telegrambot.util.ApiResponse;


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

        // Define allowed MIME types for Excel
        Set<String> ALLOWED_TYPES = Set.of(
                "application/vnd.ms-excel",  // .xls
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" // .xlsx
        );

        // Define allowed file extensions
        Set<String> ALLOWED_EXTENSIONS = Set.of("xls", "xlsx");


        // Check if no file is sent
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("error", "Please upload an Excel file."));
        }

        // Validate file type (MIME type)
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body(new ApiResponse<>("error", "Invalid file type. Please upload an Excel file."));
        }

        // Validate file extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body(new ApiResponse<>("error", "Invalid file extension. Only .xls and .xlsx are allowed."));
            }
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
