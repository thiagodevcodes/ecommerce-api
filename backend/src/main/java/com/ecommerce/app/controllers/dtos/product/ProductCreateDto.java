package com.ecommerce.app.controllers.dtos.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class ProductCreateDto {
    private Long id;

    @NotBlank(message = "O Nome não pode estar em branco.")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "A cor não pode estar em branco.")
    private String color;

    @NotNull(message = "O preço não pode ser nulo")
    private Float price;
}
