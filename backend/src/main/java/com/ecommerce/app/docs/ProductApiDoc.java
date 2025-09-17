package com.ecommerce.app.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.ecommerce.app.controllers.dtos.page.PageResponseDto;
import com.ecommerce.app.controllers.dtos.product.ProductCreateDto;
import com.ecommerce.app.controllers.dtos.product.ProductFilterRequest;
import com.ecommerce.app.controllers.dtos.product.ProductResponseDto;
import com.ecommerce.app.controllers.dtos.product.ProductUpdateDto;

@Tag(name = "Produtos", description = "Endpoints de gerenciamento de produtos")
public interface ProductApiDoc {
    @Operation(summary = "Buscar produtos", description = "Retorna uma lista paginada de produtos de acordo com os filtros informados.")
    public ResponseEntity<PageResponseDto<ProductResponseDto>> findAll(ProductFilterRequest productFilterRequest);

    @Operation(summary = "Atualizar Produto", description = "Atualiza um produto de acordo com o id")
    public ResponseEntity<ProductResponseDto> updateById(@Valid @RequestBody ProductUpdateDto product,
            @PathVariable("id") Long id, BindingResult br);

    @Operation(summary = "Buscar Produto por id", description = "Retorna um produto de acordo com o id")
    public ResponseEntity<ProductResponseDto> findById(@PathVariable("id") Long id);

    @Operation(summary = "Inserir Produto", description = "Insere um produto na base dados")
    public ResponseEntity<ProductResponseDto> insert(@Valid @RequestBody ProductCreateDto product, BindingResult br);

    @Operation(summary = "Inserir Produtos em Lote", description = "Insere diversos produtos em lote na base dados")
    public ResponseEntity<Map<String, Object>> insertBatchAsync(@RequestBody List<ProductCreateDto> dtos);

    @Operation(summary = "Remover Produto", description = "Remove um produto de acordo com o id")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id);

}