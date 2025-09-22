package com.ecommerce.app.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.ecommerce.app.models.enums.BatchStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="batch_job")
public class BatchJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    private BatchStatus status; // PENDING, RUNNING, SUCCESS, FAILED

    @Column(name = "total_items")
    private Integer totalItems = 0;

    @Column(name = "processed_items")
    private Integer processedItems = 0;

    @Column(name = "failed_items")
    private Integer failedItems = 0;

    @Column(name = "success_items")
    private Integer successItems = 0;

    @Column(name = "model")
    private String model;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}