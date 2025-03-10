package com.Cristofer.SoftComerce.repository;

import com.Cristofer.SoftComerce.model.userrole;
import com.Cristofer.SoftComerce.model.userroleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Iuserrole extends JpaRepository<userrole, userroleId> {
    // Métodos personalizados si es necesario
}