package com.Cristofer.SoftComerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Cristofer.SoftComerce.model.product;

public interface Iproduct extends JpaRepository<product, Integer> {

    @Query("SELECT p FROM product p WHERE "
         + "(:name IS NULL OR p.name LIKE %:name%) AND "
         + "(:category IS NULL OR p.category.categoryName LIKE %:category%) AND "
         + "(:price IS NULL OR p.price = :price) AND "
         + "(:status IS NULL OR p.status = :status) AND "
         + "(:stock IS NULL OR p.stock = :stock)")
    List<product> filterProducts(@Param("name") String name,
                                  @Param("category") String category,
                                  @Param("price") Double price,
                                  @Param("status") Boolean status,
                                  @Param("stock") Integer stock);
}