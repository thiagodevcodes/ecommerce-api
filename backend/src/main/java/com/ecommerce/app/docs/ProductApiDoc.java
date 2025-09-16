package com.ecommerce.app.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.ecommerce.app.controllers.dtos.product.ProductCreateDto;
import com.ecommerce.app.controllers.dtos.product.ProductResponseDto;
import com.ecommerce.app.controllers.dtos.product.ProductUpdateDto;

import org.springframework.data.domain.Page;

@Tag(name = "Produtos", description = "Endpoints de gerenciamento de produtos")
public interface ProductApiDoc {
    @Operation(summary = "Buscar produtos", description = "Retorna uma lista paginada de produtos de acordo com os filtros informados.")
    public ResponseEntity<Page<ProductResponseDto>> findAll(
            @Parameter(description = "ID do produto") Long id,
            @Parameter(description = "Preço mínimo") Float min,
            @Parameter(description = "Preço máximo") Float max,
            @Parameter(description = "Nome do produto") String name,
            @Parameter(description = "Cor do produto") String color,
            @Parameter(description = "Número da página") int page,
            @Parameter(description = "Quantidade de itens por página") int size,
            @Parameter(description = "Campo de ordenação") String sort,
            @Parameter(description = "Direção da ordenação (ASC ou DESC)") String direction);

    @Operation(summary = "Atualizar Produto", description = "Atualiza um produto de acordo com o id")
    public ResponseEntity<ProductResponseDto> updateById(@Valid @RequestBody ProductUpdateDto product, @PathVariable("id") Long id, BindingResult br);

    @Operation(summary = "Buscar Produto por id", description = "Retorna um produto de acordo com o id")
    public ResponseEntity<ProductResponseDto> findById(@PathVariable("id") Long id);

    @Operation(summary = "Inserir Produto", description = "Insere um produto na base dados")
    public ResponseEntity<ProductResponseDto> insert(@Valid @RequestBody ProductCreateDto product, BindingResult br);

    @Operation(summary = "Remover Produto", description = "Remove um produto de acordo com o id")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id);

}