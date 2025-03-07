package com.example.telegrambot.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.telegrambot.Exception.Module.ModuleNotFoundException;
import com.example.telegrambot.Exception.QAndA.DuplicateQAndACodeException;
import com.example.telegrambot.Exception.QAndA.QAndANotFoundException;
import com.example.telegrambot.dto.QAndADTO;
import com.example.telegrambot.dto.QAndAResponseDTO;
import com.example.telegrambot.model.ModuleModel;
import com.example.telegrambot.model.QAndAModel;
import com.example.telegrambot.repository.ModuleRepository;
import com.example.telegrambot.repository.QAndARepository;
import org.springframework.stereotype.Service;

@Service
public class QAndAService {

    private final QAndARepository qAndARepository;
    private final ModuleRepository moduleRepository;

    public QAndAService(QAndARepository qAndARepository, ModuleRepository moduleRepository) {
        this.qAndARepository = qAndARepository;
        this.moduleRepository = moduleRepository;
    }

    // Fetch and map QAndAModels to QAndAResponseDTO
    public List<QAndAResponseDTO> getAllQAndAs() {
        List<QAndAModel> qAndAs = qAndARepository.findAll();

        // Map each QAndAModel to QAndAResponseDTO
        return qAndAs.stream()
                .map(q -> new QAndAResponseDTO(
                        q.getId(),
                        q.getQuestionCode(),
                        q.getQuestion(),
                        q.getAnswer(),
//                        q.getModule().getName(),   // Get module name
//                        q.getModule().getId()      // Get module_id
                        (q.getModule() != null) ? q.getModule().getName() : "Unknown Module", // Handle null module
                        (q.getModule() != null) ? q.getModule().getId() : null,  // Handle null module_id
                        q.getModule().getFullName()
                ))
                .collect(Collectors.toList());
    }

    public String getAnswerByQuestionCode(String questionCode) {
        return qAndARepository.findAnswerByQuestionCode(questionCode)
                .orElse("No answer found for this question.");
    }

    public String getQuestionByQuestionCode(String questionCode) {
        return qAndARepository.findQuestionByQuestionCode(questionCode)
                .orElse("No question found for this question.");
    }


    public List<QAndAResponseDTO> getQuestionsByModuleName(String moduleName) {
        List<QAndAModel> questions = qAndARepository.findByModuleName(moduleName);
        return questions.stream()
                .map(q -> new QAndAResponseDTO(
                        q.getId(),
                        q.getQuestionCode(),
                        q.getQuestion(),
                        q.getAnswer(),
                        q.getModule().getName(),   // Get module name
                        q.getModule().getId(),      // Get module_id
                        q.getModule().getFullName()))
                .collect(Collectors.toList());
    }

    // Save a new Q&A
    public QAndAModel createQAndA(QAndAModel qAndAModel) {
        return qAndARepository.save(qAndAModel);
    }

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

    public QAndAResponseDTO createQuestion(QAndADTO qAndADTO) {
        // Guard clause: Check if a question with the same questionCode already exists
        if (qAndARepository.existsByQuestionCode(qAndADTO.questionCode())) {
            throw new DuplicateQAndACodeException("Question code '" + qAndADTO.questionCode() + "' already exists.");
        }

        // Step 1: Find the Module based on the moduleId
        ModuleModel moduleModel = moduleRepository.findById(qAndADTO.moduleId())
                .orElseThrow(() -> new ModuleNotFoundException(qAndADTO.moduleId()));

        // Step 2: Create a new QAndAModel instance and set values
        QAndAModel newQuestion = new QAndAModel();
        newQuestion.setQuestionCode(qAndADTO.questionCode());
        newQuestion.setQuestion(qAndADTO.question());
        newQuestion.setAnswer(qAndADTO.answer());
        newQuestion.setModule(moduleModel);  // Set the Module

        // Step 3: Save the new question to the repository
        QAndAModel savedQuestion = qAndARepository.save(newQuestion);

        // Step 4: Map the saved entity to QAndAResponseDTO and return it
        return new QAndAResponseDTO(
                savedQuestion.getId(),
                savedQuestion.getQuestionCode(),
                savedQuestion.getQuestion(),
                savedQuestion.getAnswer(),
                savedQuestion.getModule().getName(),
                savedQuestion.getModule().getId(),
                savedQuestion.getModule().getFullName());
    }

    public QAndAResponseDTO updateQAndA(UUID id, QAndADTO request){

        // 1. Find to see if the id exist
        QAndAModel existingQAndA = qAndARepository.findById(id)
                .orElseThrow(() -> new QAndANotFoundException(id));

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
                updatedQAndA.getModule().getFullName());
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


//    public List<QAndAResponseDTO> createQAndAs(List<QAndADTO> qAndADTOs) {
//        // Extract all moduleIds
//        Set<UUID> moduleIds = qAndADTOs.stream()
//                .map(QAndADTO::moduleId)
//                .collect(Collectors.toSet());
//
//        // Fetch all required modules in one query
//        List<ModuleModel> modules = moduleRepository.findAllById(moduleIds);
//
//        // Create a map for quick module lookup
//        Map<UUID, ModuleModel> moduleMap = modules.stream()
//                .collect(Collectors.toMap(ModuleModel::getId, module -> module));
//
//        // Check if all moduleIds exist
//        Set<UUID> foundModuleIds = moduleMap.keySet();
//        moduleIds.stream()
//                .filter(id -> !foundModuleIds.contains(id))
//                .findFirst()
//                .ifPresent(id -> {
//                    throw new ModuleNotFoundException(id);
//                });
//
//        // Create entities using the module map
//        List<QAndAModel> qAndAEntities = qAndADTOs.stream()
//                .map(dto -> {
//                    ModuleModel moduleModel = moduleMap.get(dto.moduleId());
//                    QAndAModel newQuestion = new QAndAModel();
//                    newQuestion.setQuestionCode(dto.questionCode());
//                    newQuestion.setQuestion(dto.question());
//                    newQuestion.setAnswer(dto.answer());
//                    newQuestion.setModule(moduleModel);
//                    return newQuestion;
//                })
//                .collect(Collectors.toList());
//
//        // Save all entities in one batch operation
//        List<QAndAModel> savedEntities = qAndARepository.saveAll(qAndAEntities);
//
//        // Convert saved entities to DTOs
//        List<QAndAResponseDTO> responseDTOs = savedEntities.stream()
//                .map(entity -> new QAndAResponseDTO(
//                        entity.getId(),
//                        entity.getQuestionCode(),
//                        entity.getQuestion(),
//                        entity.getAnswer(),
//                        entity.getModule().getName(),
//                        entity.getModule().getId() // Make sure this matches your field name
//                ))
//                .collect(Collectors.toList());
//
//        return responseDTOs;
//    }
}
