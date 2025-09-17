package com.ecommerce.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ecommerce.app.controllers.dtos.product.ProductCreateDto;
import com.ecommerce.app.controllers.dtos.product.ProductResponseDto;
import com.ecommerce.app.controllers.dtos.product.ProductUpdateDto;
import com.ecommerce.app.models.ProductModel;
import com.ecommerce.app.repositories.ProductRepository;
import com.ecommerce.app.services.exceptions.ConstraintException;
import com.ecommerce.app.services.exceptions.DataIntegrityException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Validator validator;

    @PersistenceContext
    private EntityManager entityManager;

    public List<ProductResponseDto> findAll() {
        List<ProductModel> products = productRepository.findAll();
        return convertListToDtoList(products);
    }

    public Page<ProductResponseDto> findAllWithFilter(Float min, Float max, String name, String color,
            Pageable pageable) {
        return productRepository.findWithFilter(min, max, name, color, pageable)
                .map(this::convertModelToResponseDto);
    }

    public ProductResponseDto insert(ProductCreateDto productDto) {
        try {
            ProductModel newProduct = convertRequestDtoToModel(productDto);

            newProduct = productRepository.save(newProduct);
            return convertModelToResponseDto(newProduct);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) do Aluno não foi(foram) preenchido(s).");
        }
    }

    @Async
    @Transactional
    public void insertBatch(List<ProductCreateDto> dtos) {
        List<ProductModel> batch = new ArrayList<>();
        int batchSize = 50;

        for (int i = 0; i < dtos.size(); i++) {
            ProductCreateDto dto = dtos.get(i);

            // Validação individual
            Set<ConstraintViolation<ProductCreateDto>> violations = validator.validate(dto);
            if (!violations.isEmpty()) {
                throw new ConstraintException(
                        "Erro no produto " + (i + 1) + ": " + violations.iterator().next().getMessage());
            }

            batch.add(convertRequestDtoToModel(dto));

            // Persistir batch
            if (batch.size() == batchSize || i == dtos.size() - 1) {
                productRepository.saveAll(batch);
                entityManager.flush(); // envia para o DB
                entityManager.clear(); // limpa memória
                batch.clear();
            }
        }
    }

    public ProductResponseDto findById(Long id) {
        Optional<ProductModel> product = productRepository.findById(id);

        if (!product.isPresent()) {
            throw new DataIntegrityException("O produto não existe na base dados");
        }

        ProductResponseDto productDto = convertModelToResponseDto(product.get());

        return productDto;
    }

    public ProductResponseDto updateById(ProductUpdateDto productDto, Long id) {
        try {
            Optional<ProductModel> product = productRepository.findById(id);

            if (!product.isPresent()) {
                throw new DataIntegrityException("O produto não existe na base dados");
            }

            ProductModel productUpdated = product.get();

            if (productDto.getName() != null) {
                productUpdated.setName(productDto.getName());
            }
            if (productDto.getPrice() != null) {
                productUpdated.setPrice(productDto.getPrice());
            }
            if (productDto.getColor() != null) {
                productUpdated.setColor(productDto.getColor());
            }

            productRepository.save(productUpdated);
            return convertModelToResponseDto(productUpdated);
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Não foi possível atualizar o produto!");
        }
    }

    public void delete(Long id) {
        try {
            if (productRepository.existsById(id)) {
                productRepository.deleteById(id);
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir um Aluno!");
        }
    }

    private List<ProductResponseDto> convertListToDtoList(List<ProductModel> list) {
        List<ProductResponseDto> productDtoList = new ArrayList<>();
        for (ProductModel productModel : list) {
            ProductResponseDto alunoDto = this.convertModelToResponseDto(productModel);
            productDtoList.add(alunoDto);
        }
        return productDtoList;
    }

    private ProductResponseDto convertModelToResponseDto(ProductModel model) {
        ProductResponseDto dto = new ProductResponseDto();

        dto.setName(model.getName());
        dto.setPrice(model.getPrice());
        dto.setColor(model.getColor());

        return dto;
    }

    private ProductModel convertRequestDtoToModel(ProductCreateDto dto) {
        ProductModel model = new ProductModel();

        model.setName(dto.getName());
        model.setPrice(dto.getPrice());
        model.setColor(dto.getColor());

        return model;
    }
}
