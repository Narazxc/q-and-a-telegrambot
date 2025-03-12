package com.example.telegrambot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;


@Entity
@Table(name = "modules")
public class ModuleModel {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // This is for numeric id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Column(name = "full_name") // Explicit mapping
    private String fullName = "";

    @CreationTimestamp
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;


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

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    @Override
    public String toString() {
        return "ModuleModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
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