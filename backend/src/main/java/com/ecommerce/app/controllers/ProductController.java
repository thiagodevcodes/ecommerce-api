package com.ecommerce.app.controllers;

import java.util.List;
import java.util.Map;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.app.controllers.dtos.page.PageResponseDto;
import com.ecommerce.app.controllers.dtos.product.ProductCreateDto;
import com.ecommerce.app.controllers.dtos.product.ProductFilterRequest;
import com.ecommerce.app.controllers.dtos.product.ProductResponseDto;
import com.ecommerce.app.controllers.dtos.product.ProductUpdateDto;
import com.ecommerce.app.docs.ProductApiDoc;
import com.ecommerce.app.models.BatchJob;
import com.ecommerce.app.models.ProductModel;
import com.ecommerce.app.repositories.BatchJobRepository;
import com.ecommerce.app.services.ProductService;
import com.ecommerce.app.services.exceptions.ConstraintException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController implements ProductApiDoc {
    @Autowired
    private ProductService productService;

    @Autowired
    private BatchJobRepository batchJobRepository;

    @GetMapping
    public ResponseEntity<PageResponseDto<ProductResponseDto>> findAll(
            @ParameterObject ProductFilterRequest filterRequest) {
        Pageable pageable = PageRequest.of(
                filterRequest.getPage(),
                filterRequest.getSize(),
                filterRequest.getDirection(),
                filterRequest.getSort().name());

        Page<ProductResponseDto> result = productService.findAllWithFilter(
                filterRequest.getPriceMin(),
                filterRequest.getPriceMax(),
                filterRequest.getName(),
                filterRequest.getColor(),
                pageable);

        return ResponseEntity.ok(PageResponseDto.from(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> findById(@PathVariable("id") Long id) {
        ProductResponseDto productDto = productService.findById(id);
        return ResponseEntity.ok().body(productDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateById(@Valid @RequestBody ProductUpdateDto product,
            @PathVariable("id") Long id, BindingResult br) {
        if (br.hasErrors())
            throw new ConstraintException(br.getAllErrors().get(0).getDefaultMessage());

        ProductResponseDto productUpdated = productService.updateById(product, id);
        return ResponseEntity.ok().body(productUpdated);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> insert(@Valid @RequestBody ProductCreateDto product, BindingResult br) {
        if (br.hasErrors())
            throw new ConstraintException(br.getAllErrors().get(0).getDefaultMessage());

        ProductResponseDto alunoDto = productService.insert(product);
        return ResponseEntity.ok().body(alunoDto);
    }

    @PostMapping("/batch")
    public ResponseEntity<Map<String, Object>> insertBatchAsync(@RequestBody List<ProductCreateDto> dtos) {
        BatchJob job = new BatchJob();
        job.setModel(ProductModel.class.getSimpleName());
        job = batchJobRepository.save(job);
        

        productService.insertProductsAsync(job.getId(), dtos);

        Map<String, Object> response = Map.of(
                "message", "Processamento de lote iniciado com sucesso.",
                "id", job.getId(),
                "items", dtos.size(),
                "model", job.getModel(),
                "statusUrl", "/batch/" + job.getId());

        return ResponseEntity.accepted().body(response);
    }

    @DeleteMapping("/batch")
    public ResponseEntity<Map<String, Object>> deleteBatchAsync(@RequestBody List<ProductCreateDto> dtos) {
        BatchJob job = new BatchJob();
        job = batchJobRepository.save(job);

        productService.insertProductsAsync(job.getId(), dtos);

        Map<String, Object> response = Map.of(
                "message", "Processamento de lote iniciado com sucesso.",
                "id", job.getId(),
                "statusUrl", "/batch/" + job.getId());

        return ResponseEntity.accepted().body(response);
    }

    @GetMapping("/batch/{id}")
    public ResponseEntity<BatchJob> getBatchStatus(@PathVariable Long id) {
        return ResponseEntity.of(batchJobRepository.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}