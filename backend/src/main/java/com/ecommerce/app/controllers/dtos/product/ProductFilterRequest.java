package com.ecommerce.app.controllers.dtos.product;

import org.springframework.data.domain.Sort;

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
    private int page;

    @Parameter(description = "Tamanho da página", example = "10", in = ParameterIn.QUERY)
    private int size;

    @Parameter(description = "Campo para ordenação", example = "name", in = ParameterIn.QUERY)
    private String sort;

    @Parameter(description = "Direção da ordenação", example = "ASC", in = ParameterIn.QUERY)
    private Sort.Direction direction;
}
