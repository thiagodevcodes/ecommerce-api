package com.ecommerce.app.controllers.dtos.product;

import org.springframework.data.domain.Sort;

import com.ecommerce.app.models.enums.ProductSortField;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
public class ProductFilterRequest {
    @Parameter(description = "Preço mínimo", in = ParameterIn.QUERY)
    private Float priceMin;

    @Parameter(description = "Preço máximo", in = ParameterIn.QUERY)
    private Float priceMax;

    @Parameter(description = "Nome do produto", in = ParameterIn.QUERY)
    private String name;

    @Parameter(description = "Cor do produto", in = ParameterIn.QUERY)
    private String color;

    @Parameter(description = "Página", example = "0", in = ParameterIn.QUERY)
    private Integer page = 0;

    @Parameter(description = "Tamanho da página", example = "10", in = ParameterIn.QUERY)
    private Integer size = 10;

    @Parameter(description = "Campo para ordenação", example = "name", in = ParameterIn.QUERY)
    private ProductSortField sort = ProductSortField.name;

    @Parameter(description = "Direção da ordenação", example = "ASC", in = ParameterIn.QUERY)
    private Sort.Direction direction = Sort.Direction.ASC;
}
