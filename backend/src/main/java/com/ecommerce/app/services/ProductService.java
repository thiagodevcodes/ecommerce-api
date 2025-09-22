package com.ecommerce.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ecommerce.app.controllers.dtos.product.ProductCreateDto;
import com.ecommerce.app.controllers.dtos.product.ProductResponseDto;
import com.ecommerce.app.controllers.dtos.product.ProductUpdateDto;
import com.ecommerce.app.models.ProductModel;
import com.ecommerce.app.repositories.ProductRepository;
import com.ecommerce.app.services.exceptions.DataIntegrityException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BatchProcessorService batchProcessorService;

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
            if (productRepository.existsByName(productDto.getName())) 
                throw new DataIntegrityException("Produto com esse nome já existe");
            
            ProductModel newProduct = convertRequestDtoToModel(productDto);
            newProduct = productRepository.save(newProduct);
            return convertModelToResponseDto(newProduct);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Erro ao inserir um produto");
        }
    }

    public void insertProductsAsync(Long batchJobId, List<ProductCreateDto> dtos) {
        // Usa o serviço genérico para processar o lote
        batchProcessorService.processBatchJob(batchJobId, dtos, this::insert);
    }

    public void deleteProductsAsync(Long batchJobId, List<Long> ids) {
        batchProcessorService.processBatchJob(batchJobId, ids, productRepository::deleteById);
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
