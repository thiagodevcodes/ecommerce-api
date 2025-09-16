package com.ecommerce.app.controllers.dtos.product;

import org.springframework.data.domain.Sort;
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
    private Long id;
    private Float priceMin;
    private Float priceMax;
    private String name;
    private String color;

    private int page = 0;
    private int size = 10;
    private String sort = "id";
    private Sort.Direction direction = Sort.Direction.ASC;
}
