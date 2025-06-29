package com.Cristofer.SoftComerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Cristofer.SoftComerce.model.Page;

public interface IPage extends JpaRepository<Page, Integer> {
    boolean existsByName(String name);
}