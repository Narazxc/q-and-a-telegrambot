package com.example.telegrambot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;


@Entity
@Table(name = "modules")
public class ModuleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    @Column(unique = true)
//    @NotBlank(message = "Name cannot be empty")
    private String name;
    @Column(name = "full_name") // Explicit mapping
    private String fullName = "";

    @Override
    public String toString() {
        return "ModuleModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }


    // No-argument constructor (required by JPA)
    public ModuleModel() { }

    // Constructor with parameters (optional, for convenience)
    public ModuleModel(UUID id, String name, String fullName) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
    }

    // Getters and Setters

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModuleModel that = (ModuleModel) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() { return id.hashCode(); }
}


//    public List<QAndAModel> getQAndAs() {
//        return qAndAs;
//    }
//
//    public void setQAndAs(List<QAndAModel> qAndAs) {
//        this.qAndAs = qAndAs;
//    }

// Optional: Override toString, equals, and hashCode methods if needed