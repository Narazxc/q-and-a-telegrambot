package com.example.telegrambot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.UUID;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.telegrambot.exception.Module.ModuleNotFoundException;
import com.example.telegrambot.exception.QAndA.DuplicateQAndACodeException;
import com.example.telegrambot.exception.QAndA.QAndANotFoundException;
import com.example.telegrambot.dto.QAndADTO;
import com.example.telegrambot.dto.QAndAResponseDTO;
import com.example.telegrambot.model.ModuleModel;
import com.example.telegrambot.model.QAndAModel;
import com.example.telegrambot.repository.ModuleRepository;
import com.example.telegrambot.repository.QAndARepository;
import com.example.telegrambot.model.AnswerImageModel;
import com.example.telegrambot.util.FileInfo;

@Service
public class QAndAService {

    private final QAndARepository qAndARepository;
    private final ModuleRepository moduleRepository;

    // File
    private final FileStorageService fileStorageService; // contain method to store file

    public QAndAService(QAndARepository qAndARepository,
                        ModuleRepository moduleRepository,
                        FileStorageService fileStorageService
    ) {
        this.qAndARepository = qAndARepository;
        this.moduleRepository = moduleRepository;
        this.fileStorageService = fileStorageService;
    }

    // Fetch and map QAndAModels to QAndAResponseDTO
    public List<QAndAResponseDTO> getAllQAndAs() {
        List<QAndAModel> qAndAs = qAndARepository.findAll(Sort.by(Sort.Direction.ASC, "createdAt"));

        // Map each QAndAModel to QAndAResponseDTO
        List<QAndAResponseDTO> QAndAResponseDTOs = qAndAs.stream()
                .map(q -> new QAndAResponseDTO(
                        q.getId(),
                        q.getQuestionCode(),
                        q.getQuestion(),
                        q.getAnswer(),
                        (q.getModule() != null) ? q.getModule().getName() : "", // Handle null module
                        (q.getModule() != null) ? q.getModule().getId() : null,  // Handle null module_id
                        (q.getModule() != null) ? q.getModule().getFullName() : "",
                        (q.getAnswerImageModel() != null) ? q.getAnswerImageModel().getImagePath() : "", // ✅ Fix NPE risk
                        q.getCreatedAt(),
                        q.getUpdatedAt()
                ))
                .collect(Collectors.toList());

        return QAndAResponseDTOs;
    }

    public String getAnswerByQuestionCode(String questionCode) {
        return qAndARepository.findAnswerByQuestionCode(questionCode)
                .orElse("");
    }

    public String getQuestionByQuestionCode(String questionCode) {
        return qAndARepository.findQuestionByQuestionCode(questionCode)
                .orElse("");
    }

    public Optional<QAndAResponseDTO> getQAndAByQuestionCode(String questionCode) {
        return qAndARepository.findByQuestionCode(questionCode)
                .map(q -> new QAndAResponseDTO(
                        q.getId(),
                        q.getQuestionCode(),
                        q.getQuestion(),
                        q.getAnswer(),
                        q.getModule(),
                        q.getModuleId(),
                        q.getModuleFullName(),
                        q.getImagePath(),
                        convertToOffsetDateTime(q.getCreatedAt()),
                        convertToOffsetDateTime(q.getUpdatedAt())
                ));
    }

    // Converts Instant to OffsetDateTime while keeping the correct format
    private OffsetDateTime convertToOffsetDateTime(Instant instant) {
        return instant != null ? instant.atOffset(ZoneOffset.UTC) : null; // Change to your required offset
    }

    public QAndAResponseDTO getQAndA(UUID id) {
        QAndAModel qAndA = qAndARepository.findById(id)
                .orElseThrow(() -> new QAndANotFoundException(id));

        // map QAndAModel to responseDTO
        QAndAResponseDTO qAndAResponseDTO = new QAndAResponseDTO(
                qAndA.getId(),
                qAndA.getQuestionCode(),
                qAndA.getQuestion(),
                qAndA.getAnswer(),
                (qAndA.getModule() != null) ? qAndA.getModule().getName() : "", // Handle null module
                (qAndA.getModule() != null) ? qAndA.getModule().getId() : null,  // Handle null module_id
                (qAndA.getModule() != null) ? qAndA.getModule().getFullName() : "",
                null,
                qAndA.getCreatedAt(),
                qAndA.getUpdatedAt()

        );
        return qAndAResponseDTO;
    }


    public List<QAndAResponseDTO> getQuestionsByModuleName(String moduleName) {
        List<QAndAModel> questions = qAndARepository.findByModuleName(moduleName, Sort.by(Sort.Direction.ASC, "createdAt"));

        List<QAndAResponseDTO> QAndAResponseDTOs = questions.stream()
                .map(q -> new QAndAResponseDTO(
                        q.getId(),
                        q.getQuestionCode(),
                        q.getQuestion(),
                        q.getAnswer(),
                        (q.getModule() != null) ? q.getModule().getName() : "", // Handle null module
                        (q.getModule() != null) ? q.getModule().getId() : null,  // Handle null module_id
                        (q.getModule() != null) ? q.getModule().getFullName() : "",
                        null,
                        q.getCreatedAt(),
                        q.getUpdatedAt()
                ))
                .collect(Collectors.toList());
        return QAndAResponseDTOs;
    }

    // Save a new Q&A
//    public QAndAModel createQAndA(QAndAModel qAndAModel) {
//        return qAndARepository.save(qAndAModel);
//    }

//    public QAndAResponseDTO createQuestion(QAndADTO qAndADTO) {
//        // Step 1: Find the Module based on the moduleId
//        ModuleModel moduleModel = moduleRepository.findById(qAndADTO.moduleId())
//                .orElseThrow(() -> new RuntimeException("Module not found"));
//
//
//        // Step 2: Create a new QAndAModel instance and set values
//        QAndAModel newQuestion = new QAndAModel();
//        newQuestion.setQuestionCode(qAndADTO.questionCode());
//        newQuestion.setQuestion(qAndADTO.question());
//        newQuestion.setAnswer(qAndADTO.answer());
//        newQuestion.setModule(moduleModel);  // Set the Module
//
//
//
//        // Step 3: Save the new question to the repository
//        QAndAModel savedQuestion = qAndARepository.save(newQuestion);
//
//        // Step 4: Map the saved entity to QAndAResponseDTO and return it
//        return new QAndAResponseDTO(
//                savedQuestion.getId(),  // ID of the newly created question
//                savedQuestion.getQuestionCode(),
//                savedQuestion.getQuestion(),
//                savedQuestion.getAnswer(),
//                savedQuestion.getModule().getName(),  // Assuming you want module name, not id
//                savedQuestion.getModule().getId()
//        );
//    }

////     Working
//    public QAndAResponseDTO createQAndA(QAndADTO qAndADTO,  MultipartFile file) {
//        // Step 1: Guard clause: Check if a q and a with the same questionCode already exists
//        if (qAndARepository.existsByQuestionCode(qAndADTO.questionCode())) {
//            throw new DuplicateQAndACodeException("Question code '" + qAndADTO.questionCode() + "' already exists.");
//        }
//
//        // Step 2: Find the Module based on the moduleId
//        ModuleModel moduleModel = moduleRepository.findById(qAndADTO.moduleId())
//                .orElseThrow(() -> new ModuleNotFoundException(qAndADTO.moduleId()));
//
//        // Step 3: Create a new QAndAModel instance and set values
//        QAndAModel newQuestion = new QAndAModel();
//        newQuestion.setQuestionCode(qAndADTO.questionCode());
//        newQuestion.setQuestion(qAndADTO.question());
//        newQuestion.setAnswer(qAndADTO.answer());
//        newQuestion.setModule(moduleModel);  // Set the Module
//
//        // Step 4: Save the new question to the repository
//        QAndAModel savedQuestion = qAndARepository.save(newQuestion);
//
//        // Step 5: Map the saved entity to QAndAResponseDTO and return it
//        return new QAndAResponseDTO(
//                savedQuestion.getId(),
//                savedQuestion.getQuestionCode(),
//                savedQuestion.getQuestion(),
//                savedQuestion.getAnswer(),
//                savedQuestion.getModule().getName(),
//                savedQuestion.getModule().getId(),
//                savedQuestion.getModule().getFullName(),
//                savedQuestion.getCreatedAt(),
//                savedQuestion.getUpdatedAt()
//        );
//    }



//    @Transactional
    public QAndAResponseDTO createQAndA(QAndADTO qAndADTO, MultipartFile file) {
        // Step 1: Guard clause: Check if a q and a with the same questionCode already exists
        if (qAndARepository.existsByQuestionCode(qAndADTO.questionCode())) {
            throw new DuplicateQAndACodeException("Question code '" + qAndADTO.questionCode() + "' already exists.");
        }

        // Step 2: Find the Module based on the moduleId
        ModuleModel moduleModel = moduleRepository.findById(qAndADTO.moduleId())
                .orElseThrow(() -> new ModuleNotFoundException(qAndADTO.moduleId()));

        // Step 3: Create a new QAndAModel instance and set values
        QAndAModel newQuestion = new QAndAModel();
        newQuestion.setQuestionCode(qAndADTO.questionCode());
        newQuestion.setQuestion(qAndADTO.question());
        newQuestion.setAnswer(qAndADTO.answer());
        newQuestion.setModuleModel(moduleModel); // ADD THIS LINE

        // Save the QAndA first to get its ID
        QAndAModel savedQuestion = qAndARepository.save(newQuestion);

        // Step 4: Handle file if present
        if (file != null && !file.isEmpty()) {
            // Save file and get path
            FileInfo fileInfo = fileStorageService.storeFileAndGetInfo(file);

            // name
            // path
            // size
            // type
            // extension

            // Create and set AnswerImageModel
            AnswerImageModel answerImage = new AnswerImageModel();
            answerImage.setImageName(fileInfo.getFileName());
            answerImage.setImagePath(fileInfo.getFilePath());
            answerImage.setImageSize(fileInfo.getFileSize());
            answerImage.setImageType(fileInfo.getFileType());
            answerImage.setImageExtension(fileInfo.getFileExtension());
            answerImage.setqAndA(savedQuestion);  // Link to the QAndA


            // Update the QAndA with the image reference
            savedQuestion.setAnswerImageModel(answerImage);
            savedQuestion = qAndARepository.save(savedQuestion);
        }

        // Step 5: Map the saved entity to QAndAResponseDTO and return it
        QAndAResponseDTO responseDTO = new QAndAResponseDTO(
                savedQuestion.getId(),
                savedQuestion.getQuestionCode(),
                savedQuestion.getQuestion(),
                savedQuestion.getAnswer(),
                savedQuestion.getModuleModel().getName(),
                savedQuestion.getModuleModel().getId(),
                savedQuestion.getModuleModel().getFullName(),
                null,
                savedQuestion.getCreatedAt(),
                savedQuestion.getUpdatedAt()
        );

        return responseDTO;
    }




    public QAndAResponseDTO updateQAndA(UUID id, QAndADTO request){
        // 1. Find to see if the id exist
        QAndAModel existingQAndA = qAndARepository.findById(id)
                .orElseThrow(() -> new QAndANotFoundException(id));

        System.out.println("Request question code:" + request.questionCode());
        System.out.println("Existing q and a question code" + existingQAndA.getQuestionCode());

        // Step 2: Guard clause to check for duplicate questionCode
        // Only check for duplicates if the questionCode is being changed
        if (!existingQAndA.getQuestionCode().equals(request.questionCode()) && qAndARepository.existsByQuestionCode(request.questionCode())) {
            throw new DuplicateQAndACodeException("Question code '" + request.questionCode() + "' already exists.");
        }

        // Update fields
        existingQAndA.setQuestionCode(request.questionCode());
        existingQAndA.setQuestion(request.question());
        existingQAndA.setAnswer(request.answer());

        // Fetch module if moduleId is present in the request
        if (request.moduleId() != null) {
            ModuleModel moduleModel = moduleRepository.findById(request.moduleId())
                    .orElseThrow(() -> new ModuleNotFoundException(request.moduleId()));
            existingQAndA.setModule(moduleModel);
        }

        // Save the updated entity
        QAndAModel updatedQAndA = qAndARepository.save(existingQAndA);

        // Return mapped response
        return new QAndAResponseDTO(
                updatedQAndA.getId(),
                updatedQAndA.getQuestionCode(),
                updatedQAndA.getQuestion(),
                updatedQAndA.getAnswer(),
                updatedQAndA.getModule().getName(),
                updatedQAndA.getModule().getId(),
                updatedQAndA.getModule().getFullName(),
                null,
                updatedQAndA.getCreatedAt(),
                updatedQAndA.getUpdatedAt()
        );
    }

    public void deleteQAndA(UUID id) {

        // Step 1: Check if Q&A with the specified ID exists
        if (!qAndARepository.existsById(id)) {
            // Step 2: If not found, throw the custom QAndANotFoundException
            throw new QAndANotFoundException(id);
        }

        // Step 3: If it exists, delete the Q&A
        qAndARepository.deleteById(id);
    }

    public List<QAndAResponseDTO> createQAndAs(List<QAndADTO> qAndADTOs) {

        List<QAndAModel> entities = new ArrayList<>();

        for (QAndADTO dto : qAndADTOs) {
            QAndAModel entity = new QAndAModel();
            entity.setQuestionCode(dto.questionCode());
            entity.setQuestion(dto.question());
            entity.setAnswer(dto.answer());

            // Find and set module by ID
            if (dto.moduleId() != null) {
                // You MUST query the module
                Optional<ModuleModel> moduleOptional = moduleRepository.findById(dto.moduleId());

                if (moduleOptional.isPresent()) {
                    ModuleModel module = moduleOptional.get();
                    entity.setModule(module); // Set the entire module object
                } else {
                    // Handle module not found case
                    System.out.println("Module not found with ID: " + dto.moduleId());
                    // You might want to log this or handle it differently
                }
            }

            // Add entity to list regardless of module status
            entities.add(entity);
        }

        try {
            // Save all entities
            List<QAndAModel> savedEntities = qAndARepository.saveAll(entities);

            // Convert to response DTOs / map to DTO
            List<QAndAResponseDTO> responseDTOs = savedEntities.stream()
                    .map(entity -> new QAndAResponseDTO(
                            entity.getId(),
                            entity.getQuestionCode(),
                            entity.getQuestion(),
                            entity.getAnswer(),
                            (entity.getModule() != null) ? entity.getModule().getName() : "",
                            entity.getModule() != null ? entity.getModule().getId() : null,
                            entity.getModule() != null ? entity.getModule().getFullName() : "",
                            null,
                            entity.getCreatedAt(),
                            entity.getUpdatedAt()
                    ))
                    .collect(Collectors.toList());

            return responseDTOs;

        } catch (Exception e) {

            // Extract question code
            String questionCode = extractQuestionCode(e.getMessage());

            throw new DuplicateQAndACodeException("Q and A with " + questionCode + " already exist, Please input new question code");
        }
    }

    public String extractQuestionCode(String errorMessage) {
        String questionCode = null;

        // Check if the message contains the detail about question_code
        if (errorMessage.contains("question_code")) {
            // Extract the content between "=(" and ")"
            int startIndex = errorMessage.indexOf("=(") + 2;
            int endIndex = errorMessage.indexOf(")", startIndex);

            if (startIndex >= 0 && endIndex >= 0) {
                questionCode = errorMessage.substring(startIndex, endIndex);
            }
        }

        return questionCode; // Will return "PR001"
    }
}


//        // check one by one for duplication?
//
//        //  insert into db
//        // Convert DTOs to entities
//        List<QAndAModel> entities = qAndADTOs.stream()
//                .map(dto -> {
//                    QAndAModel entity = new QAndAModel();
//                    entity.setQuestionCode(dto.questionCode());
//                    entity.setQuestion(dto.question());
//                    entity.setAnswer(dto.answer());
////                    entity.set(dto.fullName());
////                    entity.set(dto.moduleId());
//                    // Set other fields as needed
//                    return entity;
//                })
//                .collect(Collectors.toList());
//
//        // Save entities
//        List<QAndAModel> savedEntities = qAndARepository.saveAll(entities);
//
//        // Convert saved entities back to response DTOs
//        List<QAndAResponseDTO> responseDTOs = savedEntities.stream()
//                .map(entity -> new QAndAResponseDTO(
//                        entity.getId(),
//                        entity.getQuestionCode(),
//                        entity.getQuestion(),
//                        entity.getAnswer(),
//                        entity.getModule().getFullName(),
//                        entity.getModule().getId(), // Assuming this comes from the entity
//                        entity.getModule().getName(),
//                        entity.getCreatedAt(),
//                        entity.getUpdatedAt()
//                ))
//                .collect(Collectors.toList());
//
//        return responseDTOs;
// ======================================================================