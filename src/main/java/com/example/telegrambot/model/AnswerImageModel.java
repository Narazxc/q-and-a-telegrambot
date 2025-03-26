package com.example.telegrambot.model;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "answer_images")
public class AnswerImageModel {

    @Id
    @GeneratedValue
    private UUID id;

    private String description;

    @Column(nullable = false)
    private String imageName;

    @Column(nullable = false)
    private String imagePath;

    @Column(nullable = false)
    private Long imageSize;

    @Column(nullable = false)
    private String imageType;

    @Column(nullable = false)
    private String imageExtension;

    @OneToOne
//    @OneToOne(mappedBy = "answerImageModel")
    @JoinColumn(name = "q_and_a_id", referencedColumnName = "id", unique = true)
    private QAndAModel qAndA;

    @CreationTimestamp
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;


    public QAndAModel getqAndA() {
        return qAndA;
    }

    public void setqAndA(QAndAModel qAndA) {
        this.qAndA = qAndA;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Long getImageSize() {
        return imageSize;
    }

    public void setImageSize(Long imageSize) {
        this.imageSize = imageSize;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageExtension() {
        return imageExtension;
    }

    public void setImageExtension(String imageExtension) {
        this.imageExtension = imageExtension;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AnswerImageModel that = (AnswerImageModel) o;
        return Objects.equals(id, that.id) && Objects.equals(description, that.description) && Objects.equals(imageName, that.imageName) && Objects.equals(imagePath, that.imagePath) && Objects.equals(imageSize, that.imageSize) && Objects.equals(imageType, that.imageType) && Objects.equals(imageExtension, that.imageExtension) && Objects.equals(qAndA, that.qAndA) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, imageName, imagePath, imageSize, imageType, imageExtension, qAndA, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "AnswerImageModel{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", imageName='" + imageName + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", imageSize=" + imageSize +
                ", imageType='" + imageType + '\'' +
                ", imageExtension='" + imageExtension + '\'' +
                ", qAndA=" + qAndA +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
