package com.ecommerce.app.controllers.dtos.batch;

import java.time.LocalDateTime;

import com.ecommerce.app.models.enums.BatchStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BatchResponseDto {
    private Long id;
    private BatchStatus status;
    private Integer totalItems;
    private Integer processedItems;
    private Integer successItems;
    private Integer failedItems;
    private String errorMessage;
    private String model;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
