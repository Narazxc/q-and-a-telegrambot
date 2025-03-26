package com.example.telegrambot.model;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "q_and_a")
public class QAndAModel {

//    @GeneratedValue(strategy = GenerationType.IDENTITY) // This is for numeric id
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String questionCode;

    @Column(columnDefinition = "TEXT")
    private String question;

    @Column(columnDefinition = "TEXT")
    private String answer;

    // Foreign key referencing the Module entity
    @ManyToOne
    @JoinColumn(name = "module_id", referencedColumnName = "id") // Specifies the foreign key column
    private ModuleModel moduleModel;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "answer_image_id", referencedColumnName = "id", unique = true)
    private AnswerImageModel answerImageModel; // ✅ Corrected `mappedBy` to match `qAndA` in AnswerImageModel

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

//    // Constructor with parameters (optional, for convenience)
//    public QAndAModel(UUID id, String questionCode, String question, String answer, ModuleModel moduleModel, AnswerImageModel answerImageModel) {
//        this.id = id;
//        this.questionCode = questionCode;
//        this.question = question;
//        this.answer = answer;
//        this.moduleModel = moduleModel;
//        this.answerImageModel = answerImageModel;
//    }

    public ModuleModel getModuleModel() {
        return moduleModel;
    }

    public void setModuleModel(ModuleModel moduleModel) {
        this.moduleModel = moduleModel;
    }

    // Getters and Setters
    public AnswerImageModel getAnswerImageModel() { return answerImageModel; }
    public void setAnswerImageModel(AnswerImageModel answerImageModel) { this.answerImageModel = answerImageModel; }

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

    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getCreatedAt() { return createdAt; }

    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        QAndAModel that = (QAndAModel) o;
        return Objects.equals(id, that.id)
                && Objects.equals(questionCode, that.questionCode)
                && Objects.equals(question, that.question)
                && Objects.equals(answer, that.answer)
                && Objects.equals(moduleModel, that.moduleModel)
                && Objects.equals(answerImageModel, that.answerImageModel)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, questionCode, question, answer, moduleModel, answerImageModel, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "QAndAModel{" +
                "id=" + id +
                ", questionCode='" + questionCode + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", moduleModel=" + moduleModel +
                ", answerImageModel=" + answerImageModel +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
