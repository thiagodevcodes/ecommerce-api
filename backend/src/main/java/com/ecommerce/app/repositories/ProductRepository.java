package com.ecommerce.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.app.models.ProductModel;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {
    List<ProductModel> findByName(String name);

    List<ProductModel> findByColor(String color);

    List<ProductModel> findByPriceBetween(Float min, Float max);

    @Query(value = "SELECT * FROM products p " +
        "WHERE (:id IS NULL OR p.id = :id) " +
        "AND (:min IS NULL OR p.price >= :min) " +
        "AND (:max IS NULL OR p.price <= :max) " +
        "AND (:name IS NULL OR p.name ILIKE CAST(CONCAT('%', :name, '%') AS text)) " +
        "AND (:color IS NULL OR p.color ILIKE CAST(:color AS text))",
        nativeQuery = true)
    List<ProductModel> findWithFilter(@Param("id") Long id,
                                    @Param("min") Float min,
                                    @Param("max") Float max,
                                    @Param("name") String name,
                                    @Param("color") String color);
}
