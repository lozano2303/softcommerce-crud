package com.Cristofer.SoftComerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Cristofer.SoftComerce.model.shipping;

public interface Ishipping extends JpaRepository<shipping, Integer> {
    /* CRUD */
}
