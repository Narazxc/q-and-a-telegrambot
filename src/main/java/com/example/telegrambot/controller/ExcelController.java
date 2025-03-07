//package com.example.telegrambot.controller;
//
//import com.example.telegrambot.dto.QAndADTO;
//import com.example.telegrambot.dto.QAndAResponseDTO;
//import com.example.telegrambot.service.ExcelReaderService;
//import com.example.telegrambot.service.QAndAService;
//import com.example.telegrambot.util.ApiResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/v1/excel")
//public class ExcelController {
//
//    private final ExcelReaderService excelReaderService;
//    private final QAndAService qAndAService;
//
//    public ExcelController(ExcelReaderService excelReaderService, QAndAService qAndAService) {
//        this.excelReaderService = excelReaderService;
//        this.qAndAService = qAndAService;
//    }
//
//
//    @PostMapping("/upload")
//    public ResponseEntity<ApiResponse<List<QAndADTO>>> uploadExcelFile(@RequestParam("file") MultipartFile file) throws IOException {
//
//        if (file.isEmpty()) {
//
//            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Error", "Please upload an Excel file."));
//        }
//
//        try {
//            // Use ExcelService to convert the file into DTOs
//            List<QAndADTO> qAndADTOs = excelReaderService.readExcelFile(file);
//
//
//            // Use QAndAService to save DTOs in bulk
//            List<QAndAResponseDTO> savedQAndAs = qAndAService.createQAndAs(qAndADTOs);
//
//        //  ResponseEntity.ok(savedQAndAs);
//            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Success", qAndADTOs));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("Failed",  e.getMessage()));
//        }
//
////        Map<String, Object> response = new LinkedHashMap<>();  // Use LinkedHashMap to maintain insertion order
////        response.put("status", "Success");
////        response.put("length", data.size());  // Get the length of the data list
////        response.put("data", data);
//
////        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }
//}
