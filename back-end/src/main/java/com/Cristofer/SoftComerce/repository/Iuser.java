package com.Cristofer.SoftComerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Cristofer.SoftComerce.model.User;

public interface IUser extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.status != false")
    List<User> getListUserActive();

    @Query("""
        SELECT u
        FROM User u
        WHERE (:name IS NULL OR u.name LIKE %:name%) 
            AND (:email IS NULL OR u.email LIKE %:email%) 
            AND (:status IS NULL OR u.status = :status)
    """)
    List<User> filterUser(
        @Param("name") String name,
        @Param("email") String email,
        @Param("status") Boolean status
    );

    // Verificar si un correo ya está registrado
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);

    // Buscar un usuario por correo electrónico
    Optional<User> findByEmail(String email);
}