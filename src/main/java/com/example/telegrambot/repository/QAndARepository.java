package com.example.telegrambot.repository;

import com.example.telegrambot.model.QAndAModel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QAndARepository extends JpaRepository<QAndAModel, UUID> {
    // Custom query methods can be added here if needed

    // Custom query to find all QAndAModel where the associated ModuleModel's name is "PO"
//    @Query("SELECT q FROM QAndAModel q WHERE q.moduleModel.name = :moduleName")

    // With sort
    @Query("SELECT q FROM QAndAModel q WHERE LOWER(q.moduleModel.name) = LOWER(:moduleName)")
    List<QAndAModel> findByModuleName(String moduleName, Sort sort);

    // Without sort
    @Query("SELECT q FROM QAndAModel q WHERE LOWER(q.moduleModel.name) = LOWER(:moduleName)")
    List<QAndAModel> findByModuleName(String moduleName);

    @Query("SELECT q.answer FROM QAndAModel q WHERE q.questionCode ILIKE :questionCode")
    Optional<String> findAnswerByQuestionCode(String questionCode);

    @Query("SELECT q.question FROM QAndAModel q WHERE q.questionCode ILIKE :questionCode")
    Optional<String> findQuestionByQuestionCode(String questionCode);


    // Custom JPQL query to check if a QAndAModel exists by questionCode
    @Query("SELECT CASE WHEN COUNT(q) > 0 THEN true ELSE false END FROM QAndAModel q WHERE q.questionCode = :questionCode")
    boolean existsByQuestionCode(String questionCode);

    boolean existsById(UUID id);
}