package com.Cristofer.SoftComerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Cristofer.SoftComerce.model.Product;

public interface IProduct extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE "
         + "(:name IS NULL OR p.name LIKE %:name%) AND "
         + "(:category IS NULL OR p.category.categoryName LIKE %:category%) AND "
         + "(:price IS NULL OR p.price = :price) AND "
         + "(:status IS NULL OR p.status = :status) AND "
         + "(:stock IS NULL OR p.stock = :stock)")
    List<Product> filterProducts(@Param("name") String name,
                                 @Param("category") String category,
                                 @Param("price") Double price,
                                 @Param("status") Boolean status,
                                 @Param("stock") Integer stock);
}