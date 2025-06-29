package com.Cristofer.SoftComerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Cristofer.SoftComerce.DTO.PageRoleDTO;
import com.Cristofer.SoftComerce.model.Page;
import com.Cristofer.SoftComerce.model.PageRole;
import com.Cristofer.SoftComerce.model.Role;
import com.Cristofer.SoftComerce.repository.IPage;
import com.Cristofer.SoftComerce.repository.IPageRole;
import com.Cristofer.SoftComerce.repository.IRole;

@Service
public class PageRoleService {

    @Autowired
    private IPageRole pageRoleRepository;

    @Autowired
    private IPage pageRepository;

    @Autowired
    private IRole roleRepository;

    // Listar todas las relaciones PageRole
    public List<PageRole> findAll() {
        return pageRoleRepository.findAll();
    }

    // Buscar por ID
    public Optional<PageRole> findById(int id) {
        return pageRoleRepository.findById(id);
    }

    // Buscar por PageID
    public List<PageRole> findByPageID(int pageID) {
        return pageRoleRepository.findByPageID_PageID(pageID);
    }

    // Buscar por RoleID
    public List<PageRole> findByRoleID(int roleID) {
        return pageRoleRepository.findByRoleID_RoleID(roleID);
    }

    // Registrar nueva relación
    @Transactional
    public String register(PageRoleDTO dto) {
        if (dto.getPageID() == null || dto.getRoleID() == null) {
            return "Página y Rol deben ser proporcionados";
        }
        int pageID = dto.getPageID().getPageID();
        int roleID = dto.getRoleID().getRoleID();

        if (!pageRepository.existsById(pageID)) {
            return "Página no encontrada";
        }
        if (!roleRepository.existsById(roleID)) {
            return "Rol no encontrado";
        }
        if (pageRoleRepository.existsByPageID_PageIDAndRoleID_RoleID(pageID, roleID)) {
            return "Ya existe una relación para esa Página y Rol";
        }

        try {
            Page page = pageRepository.findById(pageID).get();
            Role role = roleRepository.findById(roleID).get();
            PageRole pageRole = new PageRole(0, page, role);
            pageRoleRepository.save(pageRole);
            return "Relación Página-Rol registrada correctamente";
        } catch (DataAccessException e) {
            return "Error de base de datos al guardar la relación";
        } catch (Exception e) {
            return "Error inesperado al guardar la relación";
        }
    }

    // Actualizar relación por ID
    @Transactional
    public String update(int id, PageRoleDTO dto) {
        Optional<PageRole> pageRoleOptional = pageRoleRepository.findById(id);
        if (!pageRoleOptional.isPresent()) {
            return "Relación Página-Rol no encontrada";
        }
        if (dto.getPageID() == null || dto.getRoleID() == null) {
            return "Página y Rol deben ser proporcionados";
        }
        int pageID = dto.getPageID().getPageID();
        int roleID = dto.getRoleID().getRoleID();

        if (!pageRepository.existsById(pageID)) {
            return "Página no encontrada";
        }
        if (!roleRepository.existsById(roleID)) {
            return "Rol no encontrado";
        }
        if (pageRoleRepository.existsByPageID_PageIDAndRoleID_RoleID(pageID, roleID)
                && !(pageRoleOptional.get().getPageID().getPageID() == pageID
                && pageRoleOptional.get().getRoleID().getRoleID() == roleID)) {
            return "Ya existe una relación para esa Página y Rol";
        }

        try {
            PageRole pageRole = pageRoleOptional.get();
            Page page = pageRepository.findById(pageID).get();
            Role role = roleRepository.findById(roleID).get();
            pageRole.setPageID(page);
            pageRole.setRoleID(role);
            pageRoleRepository.save(pageRole);
            return "Relación Página-Rol actualizada correctamente";
        } catch (DataAccessException e) {
            return "Error de base de datos al actualizar la relación";
        } catch (Exception e) {
            return "Error inesperado al actualizar la relación";
        }
    }

    // Eliminar relación por ID
    @Transactional
    public String deleteById(int id) {
        Optional<PageRole> pageRoleOptional = pageRoleRepository.findById(id);
        if (!pageRoleOptional.isPresent()) {
            return "Relación Página-Rol no encontrada";
        }
        try {
            pageRoleRepository.deleteById(id);
            return "Relación Página-Rol eliminada correctamente";
        } catch (DataAccessException e) {
            return "Error de base de datos al eliminar la relación";
        } catch (Exception e) {
            return "Error inesperado al eliminar la relación";
        }
    }

    // Convertir DTO a entidad
    public PageRole convertToModel(PageRoleDTO dto) {
        return new PageRole(0, dto.getPageID(), dto.getRoleID());
    }

    // Convertir entidad a DTO
    public PageRoleDTO convertToDTO(PageRole pageRole) {
        return new PageRoleDTO(pageRole.getPageID(), pageRole.getRoleID());
    }
}