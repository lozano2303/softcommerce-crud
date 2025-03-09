package com.Cristofer.SoftComerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Cristofer.SoftComerce.model.order;

public interface Iorder extends JpaRepository<order, Integer> {
    /* CRUD */
}
