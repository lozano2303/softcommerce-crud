package com.Cristofer.SoftComerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Cristofer.SoftComerce.model.product;

public interface Iproduct extends JpaRepository<product, Integer>{
    /*CRUD */
}
