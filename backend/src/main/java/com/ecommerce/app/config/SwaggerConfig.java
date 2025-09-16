package com.ecommerce.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Produtos")   // 👉 Nome do projeto no Swagger
                        .description("Documentação da API para gerenciamento de produtos")
                        .version("1.0.0"));
    }
}