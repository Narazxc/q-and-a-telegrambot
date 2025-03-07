package com.example.telegrambot.controller;

import com.example.telegrambot.dto.QAndADTO;
import com.example.telegrambot.dto.QAndAResponseDTO;
import com.example.telegrambot.service.QAndAService;
import com.example.telegrambot.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/q-and-a")
public class QAndAController {

    private final QAndAService qAndAService;

    public QAndAController(QAndAService qAndAService) {
        this.qAndAService = qAndAService;
    }

    // Get all Q&As
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<QAndAResponseDTO>>> getAllQAndAs() {

        List<QAndAResponseDTO> allQAndAs = qAndAService.getAllQAndAs();

        // Structuring response
        ApiResponse<List<QAndAResponseDTO>> response = new ApiResponse<>("success", allQAndAs);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    @GetMapping("/{moduleName}")
    public ResponseEntity<ApiResponse<List<QAndAResponseDTO>>> getQuestionsByModuleName(@PathVariable String moduleName) {

        List<QAndAResponseDTO> qAndAByModule = qAndAService.getQuestionsByModuleName(moduleName);

        // Structuring response
        ApiResponse<List<QAndAResponseDTO>> response = new ApiResponse<>("success", qAndAByModule);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    @PostMapping
    public ResponseEntity<ApiResponse<QAndAResponseDTO>> createQuestion(@RequestBody QAndADTO qAndADTO) {

        QAndAResponseDTO createdQuestion = qAndAService.createQuestion(qAndADTO);
        // Return the created question DTO with HTTP status 201 (Created)
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Success", createdQuestion));
    }

    // ==========================================================================================


    // Update an existing Q&A
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<QAndAResponseDTO>> updateQAndA(@PathVariable Long id, @RequestBody QAndADTO qAndADTO) {


        try {
            QAndAResponseDTO qAndAResponseDTO = qAndAService.updateQAndA(id, qAndADTO);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Success", qAndAResponseDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("Error", "No Q and A found with that id"));
        }
    }

    // Delete a Q&A by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<QAndAResponseDTO>> deleteQAndA(@PathVariable Long id) {
        try {
            qAndAService.deleteQAndA(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("Error", "No Q and A found with that id"));
        }
    }
}


//====== OLD CODE ==========================================================//
//=================  Get all QAndAs    =========================================================

//    // Get all Q&As
//    @GetMapping("")
//    public List<QAndAResponseDTO> getAllQAndAs() {
//        return qAndAService.getAllQAndAs();
//    }

//===========================================================================================

// =====         Create QAndA      ============================================================

//    @PostMapping
//    public ResponseEntity<ApiResponse<QAndAResponseDTO>> createQuestion(@RequestBody QAndADTO qAndADTO) {
//        try {
//            QAndAResponseDTO createdQuestion = qAndAService.createQuestion(qAndADTO);
//            // Return the created question DTO with HTTP status 201 (Created)
//            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Success", createdQuestion));
//        } catch (DataIntegrityViolationException e) {
//            System.out.println(e.getMessage());
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>("Error", "The question code already exists. Please use a unique code.") );
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("Fail", "Something went wrong. Please try again later."));
//        }
//    }



//    @PostMapping
//    public ApiResponse<QAndAResponseDTO> createQuestion(@RequestBody QAndADTO qAndADTO) {
//
//            QAndAResponseDTO createdQuestion = qAndAService.createQuestion(qAndADTO);
//            // Return the created question DTO with HTTP status 201 (Created)
//            return new ApiResponse<>("success", createdQuestion);
//    }

// ==========================================================================================================











//======================================================================

//==========  Old Old Code ==================================//

//    // Get all Q&As
//    @GetMapping("")
//    public List<QAndAModel> getAllQAndAs() {
//        return qAndAService.getAllQAndAs();
//    }

//    // Get all Q&As
//    @GetMapping
//    public ResponseEntity<List<QAndAModel>> getAllQAndAs() {
//        List<QAndAModel> qAndAs = qAndAService.getAllQAndAs();
//        return ResponseEntity.ok(qAndAs);
//    }
//
//    // Get Q&A by ID
//    @GetMapping("/{id}")
//    public ResponseEntity<QAndAModel> getQAndAById(@PathVariable Long id) {
//        Optional<QAndAModel> qAndAModel = qAndAService.getQAndAById(id);
//        return qAndAModel.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // Save a new Q&A
//    @PostMapping
//    public ResponseEntity<QAndAModel> saveQAndA(@RequestBody QAndAModel qAndAModel) {
//        QAndAModel savedQAndA = qAndAService.saveQAndA(qAndAModel);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedQAndA);
//    }
//
//    // Update an existing Q&A
//    @PutMapping("/{id}")
//    public ResponseEntity<QAndAModel> updateQAndA(@PathVariable Long id, @RequestBody QAndAModel updatedQAndA) {
//        try {
//            QAndAModel qAndAModel = qAndAService.updateQAndA(id, updatedQAndA);
//            return ResponseEntity.ok(qAndAModel);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    // Delete a Q&A by ID
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteQAndA(@PathVariable Long id) {
//        try {
//            qAndAService.deleteQAndA(id);
//            return ResponseEntity.noContent().build();
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
