package com.example.telegrambot.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.telegrambot.model.ModuleModel;
import com.example.telegrambot.service.ModuleService;
import com.example.telegrambot.util.ApiResponse;

@RestController
@RequestMapping("/api/v1/modules")
public class ModuleController {

    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    // Get all module
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<ModuleModel>>> getAllModules() {
        List<ModuleModel> modules  = moduleService.getAllModules();
        return ResponseEntity.status(HttpStatus.OK).body( new ApiResponse<>("success", modules));
    }

    // Get module by id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Optional<ModuleModel>>> getModuleById(@PathVariable UUID id) {
        Optional<ModuleModel> moduleModel = moduleService.findModuleById(id);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("success", moduleModel));
    }

    // Create a new module
    @PostMapping("")
    public ResponseEntity<ApiResponse<ModuleModel>> createModule(@Valid @RequestBody ModuleModel moduleModel) {
        ModuleModel savedModule = moduleService.createModule(moduleModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("success", savedModule));
    }

    // Update an existing module
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ModuleModel>> updateModule(@PathVariable UUID id, @RequestBody ModuleModel newModuleData) {

        ModuleModel updatedModule = moduleService.updateModule(id, newModuleData);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("success", updatedModule));
    }

    // Delete a module by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteModule(@PathVariable UUID id) {

        moduleService.deleteModule(id);
        return ResponseEntity.noContent().build();
    }
}
