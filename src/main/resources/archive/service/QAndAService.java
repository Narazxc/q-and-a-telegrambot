package com.example.telegrambot.service;

import com.example.telegrambot.dto.QAndADTO;
import com.example.telegrambot.dto.QAndAResponseDTO;
import com.example.telegrambot.model.ModuleModel;
import com.example.telegrambot.model.QAndAModel;
import com.example.telegrambot.repository.ModuleRepository;
import com.example.telegrambot.repository.QAndARepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
                        q.getModule().getName(),   // Get module name
                        q.getModule().getId(),      // Get module_id
                        q.getModule().getFullName()))
                .collect(Collectors.toList());
    }

    public String getAnswerByQuestionCode(String questionCode) {
        return qAndARepository.findAnswerByQuestionCode(questionCode)
                .orElse("No answer found for this question.");
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

    public QAndAResponseDTO createQuestion(QAndADTO qAndADTO) {
        // Step 1: Find the Module based on the moduleId
        ModuleModel moduleModel = moduleRepository.findById(qAndADTO.moduleId())
                .orElseThrow(() -> new RuntimeException("Module not found"));

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
                savedQuestion.getId(),  // ID of the newly created question
                savedQuestion.getQuestionCode(),
                savedQuestion.getQuestion(),
                savedQuestion.getAnswer(),
                savedQuestion.getModule().getName(),  // Assuming you want module name, not id
                savedQuestion.getModule().getId(),
                q.getModule().getFullName());
    }

    public QAndAResponseDTO updateQAndA(Long id, QAndADTO request){

        // 1. Find to see if the id exist
        QAndAModel existingQAndA = qAndARepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Q&A with id " + id + " not found"));

        // Update fields
        existingQAndA.setQuestionCode(request.questionCode());
        existingQAndA.setQuestion(request.question());
        existingQAndA.setAnswer(request.answer());

        // Fetch module if moduleId is present in the request
        if (request.moduleId() != null) {
            ModuleModel moduleModel = moduleRepository.findById(request.moduleId())
                    .orElseThrow(() -> new RuntimeException("Module not found"));
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
                q.getModule().getFullName());
    }

    public void deleteQAndA(Long id) {
        if (qAndARepository.existsById(id)) {
            qAndARepository.deleteById(id);
        } else {
            throw new RuntimeException("Q&A with id " + id + " not found");
        }
    }
}


//    // Get all Q&A
//    public List<QAndAModel> getAllQAndAs() {
//        return qAndARepository.findAll();
//    }

//    public List<QAndAModel> getQuestionsByModuleName(String moduleName) {
//        return qAndARepository.findByModuleName(moduleName);
//    }

//=====================================================================================

//    // Get Q&A by ID
//    public Optional<QAndAModel> getQAndAById(Long id) {
//        return qAndARepository.findById(id);
//    }
//
//    // Save a new Q&A
//    public QAndAModel saveQAndA(QAndAModel qAndAModel) {
//        return qAndARepository.save(qAndAModel);
//    }
//
//    // Update an existing Q&A
//    public QAndAModel updateQAndA(Long id, QAndAModel updatedQAndA) {
//        if (qAndARepository.existsById(id)) {
//            updatedQAndA.setId(id);
//            return qAndARepository.save(updatedQAndA);
//        }
//        throw new RuntimeException("Q&A with id " + id + " not found");
//    }
//
//    // Delete a Q&A by ID
//    public void deleteQAndA(Long id) {
//        if (qAndARepository.existsById(id)) {
//            qAndARepository.deleteById(id);
//        } else {
//            throw new RuntimeException("Q&A with id " + id + " not found");
//        }
//    }