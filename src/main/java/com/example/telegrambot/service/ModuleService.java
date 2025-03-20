package com.example.telegrambot.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.telegrambot.exception.Module.DuplicateModuleNameException;
import com.example.telegrambot.exception.Module.ModuleNotFoundException;
import com.example.telegrambot.model.ModuleModel;
import com.example.telegrambot.repository.ModuleRepository;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;

    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    // Get all modules
    public List<ModuleModel> getAllModules() {
        return moduleRepository.findAll(Sort.by(Sort.Direction.ASC, "createdAt"));
    }

    // Get module by ID
    public Optional<ModuleModel> findModuleById(UUID id) {
        // Check if the module with the provided id exist in the database
        if (!moduleRepository.existsById(id)) {
            throw new ModuleNotFoundException(id);
        }

        return moduleRepository.findById(id);
    }


    public ModuleModel createModule(@Valid ModuleModel moduleModel) {

        // Check for duplicate name in service layer, before repo throw sql error
        if (moduleRepository.existsByName(moduleModel.getName())) {
            throw new DuplicateModuleNameException(moduleModel.getName());
        }
        return moduleRepository.save(moduleModel);
    }

    // Update an existing module
    public ModuleModel updateModule(UUID id, ModuleModel newModuleData) {

        // Check if this module actually exist
        if (!moduleRepository.existsById(id)) {
            throw new ModuleNotFoundException(id);
        }

        // Check if the name already used to avoid duplicate module name (excluding the current module)
        if (moduleRepository.existsByName(newModuleData.getName())) {
            throw new DuplicateModuleNameException(newModuleData.getName());
        }

        // Retrieve the existing module from the database
        ModuleModel module = moduleRepository.findById(id).orElseThrow(() -> new ModuleNotFoundException(id));

        // Update fields only if they are provided (non-null)
        if (newModuleData.getName() != null) {
            module.setName(newModuleData.getName());
        }

        if (!newModuleData.getFullName().isEmpty()) {
            module.setFullName(newModuleData.getFullName());
        }

        return moduleRepository.save(module);
    }


    // Delete a module by ID
    public void deleteModule(UUID id) {
        // Guard clause Check if the module exists, throw exception if not found
        if (!moduleRepository.existsById(id)) throw new ModuleNotFoundException(id);

        // Delete the module from the repository
        moduleRepository.deleteById(id);
    }
}

//========================================================================
//    // Save a new module
//    public ModuleModel createModule(ModuleModel moduleModel) {
//        return moduleRepository.save(moduleModel);
//    }

//    // Update an existing module
//    public ModuleModel updateModule(Long id, ModuleModel updatedModuleModel) {
//        if (moduleRepository.existsById(id)) {
//            updatedModuleModel.setId(id);
//            return moduleRepository.save(updatedModuleModel);
//        }
//        throw new ModuleNotFoundException(id); // Throw custom exception
//    }