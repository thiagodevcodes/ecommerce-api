package com.ecommerce.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ecommerce.app.controllers.dtos.batch.BatchResponseDto;
import com.ecommerce.app.models.BatchJob;
import com.ecommerce.app.models.enums.BatchStatus;
import com.ecommerce.app.repositories.BatchJobRepository;
import com.ecommerce.app.services.exceptions.DataIntegrityException;
import com.ecommerce.app.services.interfaces.BatchItemProcessor;

@Service
public class BatchProcessorService {
    @Autowired
    private BatchJobRepository batchJobRepository;

    @Autowired
    private ModelMapper modelMapper;

    public BatchResponseDto findById(Long id) {
        Optional<BatchJob> batch = batchJobRepository.findById(id);

        if (!batch.isPresent()) {
            throw new DataIntegrityException("O processo em lote não existe na base de dados");
        }

        BatchResponseDto batchDto = modelMapper.map(batch.get(), BatchResponseDto.class);

        return batchDto;
    }

    @Async
    public <T> void processBatchJob(Long batchJobId, List<T> items, BatchItemProcessor<T> processor) {
        BatchJob job = batchJobRepository.findById(batchJobId)
                .orElseThrow(() -> new RuntimeException("BatchJob não encontrado"));

        job.setStatus(BatchStatus.RUNNING);
        job.setTotalItems(items.size());
        batchJobRepository.save(job);

        List<String> errors = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            T item = items.get(i);

            try {
                processor.process(item);
                job.setSuccessItems(job.getSuccessItems() + 1);
            } catch (Exception e) {
                // Armazena o erro, mas continua
                errors.add("Item " + (i + 1) + ": " + e.getMessage());
                job.setFailedItems(job.getFailedItems() + 1);
            }

            job.setProcessedItems(i + 1);
            batchJobRepository.save(job);
        }

        if (errors.isEmpty()) {
            job.setStatus(BatchStatus.SUCCESS);
        } else if (errors.size() == items.size()) {
            job.setStatus(BatchStatus.FAILED);
        } else {
            job.setStatus(BatchStatus.PARTIAL); // você pode criar esse enum
        }

        job.setErrorMessage(String.join(", ", errors));
        batchJobRepository.save(job);
    }
}