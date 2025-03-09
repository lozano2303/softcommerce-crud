package com.Cristofer.SoftComerce.repository;

import com.Cristofer.SoftComerce.model.review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Ireview extends JpaRepository<review, Integer> {
    // Puedes agregar métodos personalizados aquí si es necesario
}