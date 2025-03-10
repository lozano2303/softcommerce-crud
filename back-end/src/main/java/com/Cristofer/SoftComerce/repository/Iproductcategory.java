package com.Cristofer.SoftComerce.repository;

import com.Cristofer.SoftComerce.model.productcategory;
import com.Cristofer.SoftComerce.model.productcategoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Iproductcategory extends JpaRepository<productcategory, productcategoryId> {
    // Métodos personalizados si es necesario
}