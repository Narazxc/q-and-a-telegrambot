package com.example.telegrambot.model;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Entity
@Table(name = "q_and_a")
public class QAndAModel {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // This is for numeric id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String questionCode;
    private String question;
    private String answer;

    // Foreign key referencing the Module entity
    @ManyToOne
    @JoinColumn(name = "module_id", referencedColumnName = "id") // Specifies the foreign key column
    private ModuleModel moduleModel;

    @CreationTimestamp
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    // No-argument constructor (required by JPA)
    public QAndAModel() {
    }

    // Constructor with parameters (optional, for convenience)
    public QAndAModel(UUID id, String questionCode, String question, String answer, ModuleModel moduleModel) {
        this.id = id;
        this.questionCode = questionCode;
        this.question = question;
        this.answer = answer;
        this.moduleModel = moduleModel;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getQuestionCode() {
        return questionCode;
    }
    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public ModuleModel getModule() {
        return moduleModel;
    }
    public void setModule(ModuleModel moduleModel) {
        this.moduleModel = moduleModel;
    }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    // Optional: Override toString, equals, and hashCode methods if needed
    @Override
    public String toString() {
        return "QAndAModel{" +
                "id=" + id +
                ", questionCode='" + questionCode + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", module=" + moduleModel +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QAndAModel that = (QAndAModel) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
