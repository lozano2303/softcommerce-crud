package com.Cristofer.SoftComerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Cristofer.SoftComerce.model.PageRole;

public interface IPageRole extends JpaRepository<PageRole, Integer> {
    List<PageRole> findByPageID_PageID(int pageID);
    List<PageRole> findByRoleID_RoleID(int roleID);
    boolean existsByPageID_PageIDAndRoleID_RoleID(int pageID, int roleID);
}