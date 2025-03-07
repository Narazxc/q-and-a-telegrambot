package com.example.telegrambot.controller;

import com.example.telegrambot.model.ModuleModel;
import com.example.telegrambot.service.ModuleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/modules")
public class ModuleController {

    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @GetMapping("")
    public List<ModuleModel> getAllModules() {
        return moduleService.getAllModules();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuleModel> getModuleById(@PathVariable UUID id) {
        ModuleModel moduleModel = moduleService.findModuleById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Module not found"));

        return ResponseEntity.ok(moduleModel);
    }

    // Save a new module
    @PostMapping
    public ResponseEntity<ModuleModel> saveModule(@RequestBody ModuleModel moduleModel) {
        ModuleModel savedModule = moduleService.createModule(moduleModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedModule);
    }

    // Update an existing module
    @PutMapping("/{id}")
    public ResponseEntity<ModuleModel> updateModule(@PathVariable UUID id, @RequestBody ModuleModel updatedModule) {
        try {
            ModuleModel module = moduleService.updateModule(id, updatedModule);
            return ResponseEntity.ok(module);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

        // Delete a module by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable UUID id) {
        try {
            moduleService.deleteModule(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

















//    // Get all modules
//    @GetMapping("")
//    public ResponseEntity<List<ModuleModel>> getAllModules() {
//        List<ModuleModel> modules = moduleService.getAllModules();
//        return ResponseEntity.ok(modules);
//    }

//    // Get module by ID
//    @GetMapping("/{id}")
//    public ResponseEntity<ModuleModel> getModuleById(@PathVariable Long id) {
//        Optional<ModuleModel> moduleModel = moduleService.getModuleById(id);
//        return moduleModel.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }

//
//    // Save a new module
//    @PostMapping
//    public ResponseEntity<ModuleModel> saveModule(@RequestBody ModuleModel moduleModel) {
//        ModuleModel savedModule = moduleService.saveModule(moduleModel);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedModule);
//    }
//
//    // Update an existing module
//    @PutMapping("/{id}")
//    public ResponseEntity<ModuleModel> updateModule(@PathVariable Long id, @RequestBody ModuleModel updatedModule) {
//        try {
//            ModuleModel module = moduleService.updateModule(id, updatedModule);
//            return ResponseEntity.ok(module);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    // Delete a module by ID
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteModule(@PathVariable Long id) {
//        try {
//            moduleService.deleteModule(id);
//            return ResponseEntity.noContent().build();
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }