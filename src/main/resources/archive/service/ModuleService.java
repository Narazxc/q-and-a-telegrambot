package com.example.telegrambot.service;

import com.example.telegrambot.model.ModuleModel;
import com.example.telegrambot.repository.ModuleRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;

    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    // Get all modules
    public List<ModuleModel> getAllModules() {
        return moduleRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    // Get module by ID
    public Optional<ModuleModel> findModuleById(Long id) {
        return moduleRepository.findById(id);
    }

    // Save a new module
    public ModuleModel createModule(ModuleModel moduleModel) {
        return moduleRepository.save(moduleModel);
    }

    // Update an existing module
    public ModuleModel updateModule(Long id, ModuleModel updatedModuleModel) {
        if (moduleRepository.existsById(id)) {
            updatedModuleModel.setId(id);
            return moduleRepository.save(updatedModuleModel);
        }
        throw new RuntimeException("Module with id " + id + " not found");
    }

    // Delete a module by ID
    public void deleteModule(Long id) {
        if (moduleRepository.existsById(id)) {
            moduleRepository.deleteById(id);
        } else {
            throw new RuntimeException("Module with id " + id + " not found");
        }
    }
}
