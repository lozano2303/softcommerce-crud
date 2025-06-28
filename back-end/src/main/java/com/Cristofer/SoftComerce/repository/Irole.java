package com.Cristofer.SoftComerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Cristofer.SoftComerce.model.Role;

import java.util.Optional;

public interface IRole extends JpaRepository<Role, Integer> {

    // Buscar un rol por su nombre
    @Query("SELECT r FROM Role r WHERE r.name = :name")
    Optional<Role> findByName(@Param("name") String name);

    // Verificar si un rol existe por su nombre
    @Query("SELECT COUNT(r) > 0 FROM Role r WHERE r.name = :name")
    boolean existsByName(@Param("name") String name);
}