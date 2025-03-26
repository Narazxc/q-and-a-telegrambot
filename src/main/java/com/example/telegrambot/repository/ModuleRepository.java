package com.example.telegrambot.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.telegrambot.model.ModuleModel;


@Repository
public interface ModuleRepository extends JpaRepository<ModuleModel, UUID> {
    // Custom query methods can be added here if needed
    List<ModuleModel> findByName(String name);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END FROM ModuleModel m WHERE m.name = :name")
    boolean existsByName(@Param("name") String name);
}
