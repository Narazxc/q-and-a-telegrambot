//package com.example.telegrambot.service;
//
//import com.example.telegrambot.dto.QAndADTO;
//import com.example.telegrambot.model.ModuleModel;
//import com.example.telegrambot.repository.ModuleRepository;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.UUID;
//
//
//@Service
//public class ExcelReaderService {
//
//    private final ModuleRepository moduleRepository;
//
//    public ExcelReaderService(ModuleRepository moduleRepository) {
//        this.moduleRepository = moduleRepository;
//    }
//
//// Work and Optimize
//public List<QAndADTO> readExcelFile(MultipartFile file) throws IOException {
//    List<QAndADTO> questionList = new ArrayList<>();
//
//    try (InputStream inputStream = file.getInputStream();
//         Workbook workbook = new XSSFWorkbook(inputStream)) {
//
//        Sheet sheet = workbook.getSheetAt(0);
//        Iterator<Row> rowIterator = sheet.iterator();
//
//        // Skipping header row
//        if (rowIterator.hasNext()) rowIterator.next(); // Skip header row safely
//
//        while (rowIterator.hasNext()) {
//            Row row = rowIterator.next();
//
//            // Read values safely
//            String questionCode = getCellValue(row, 0);
//            String question = getCellValue(row, 1);
//            String answer = getCellValue(row, 2);
//            String moduleName = getCellValue(row, 3);
//
//            try {
//                // Query for moduleId by using module name
//                UUID moduleId = findModuleIdByName(moduleName); // Query moduleId
//
//                // Create and add DTO
//                questionList.add(new QAndADTO(questionCode, question, answer, moduleId));
//
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//    }
//
//    return questionList;
//}
//
//    // Utility method to handle nulls and different cell types
//    private String getCellValue(Row row, int cellIndex) {
//        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//        return (cell == null) ? "" : cell.toString();
//    }
//
//    private UUID findModuleIdByName(String moduleName) {
//        return moduleRepository.findByName(moduleName)
//                .map(ModuleModel::getId) // If found, get UUID
//                .orElseThrow(() -> new RuntimeException("Module not found: " + moduleName));
//    }
//}
//
//
//
