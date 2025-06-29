package com.Cristofer.SoftComerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Cristofer.SoftComerce.DTO.PermissionRoleDTO;
import com.Cristofer.SoftComerce.model.PermissionRole;
import com.Cristofer.SoftComerce.repository.IPermissionRole;
import com.Cristofer.SoftComerce.repository.IPageRole;
import com.Cristofer.SoftComerce.model.PageRole;

@Service
public class PermissionRoleService {

    @Autowired
    private IPermissionRole permissionRoleRepository;

    @Autowired
    private IPageRole pageRoleRepository;

    // Listar todos los permisos
    public List<PermissionRole> findAll() {
        return permissionRoleRepository.findAll();
    }

    // Buscar por ID
    public Optional<PermissionRole> findById(int id) {
        return permissionRoleRepository.findById(id);
    }

    // Buscar por PageRoleID
    public List<PermissionRole> findByPageRoleID(int pageRoleID) {
        return permissionRoleRepository.findByPageRoleID_PageRoleID(pageRoleID);
    }

    // Buscar por tipo de permiso
    public List<PermissionRole> findByPermissionType(String permissionType) {
        return permissionRoleRepository.findByPermissionType(permissionType);
    }

    // Registrar un nuevo permiso
    @Transactional
    public String register(PermissionRoleDTO dto) {
        if (dto.getPermissionType() == null || dto.getPageRoleID() == null) {
            return "Tipo de permiso y PageRole deben ser proporcionados";
        }
        int pageRoleID = dto.getPageRoleID().getPageRoleID();
        String permissionType = dto.getPermissionType().name();

        if (!pageRoleRepository.existsById(pageRoleID)) {
            return "PageRole no encontrado";
        }
        if (permissionRoleRepository.existsByPermissionTypeAndPageRoleID_PageRoleID(permissionType, pageRoleID)) {
            return "Ya existe un permiso de ese tipo para este PageRole";
        }

        try {
            PageRole pageRole = pageRoleRepository.findById(pageRoleID).get();
            PermissionRole permissionRole = new PermissionRole(0, permissionType, pageRole);
            permissionRoleRepository.save(permissionRole);
            return "Permiso registrado correctamente";
        } catch (DataAccessException e) {
            return "Error de base de datos al guardar el permiso";
        } catch (Exception e) {
            return "Error inesperado al guardar el permiso";
        }
    }

    // Actualizar permiso por ID
    @Transactional
    public String update(int id, PermissionRoleDTO dto) {
        Optional<PermissionRole> permissionRoleOptional = permissionRoleRepository.findById(id);
        if (!permissionRoleOptional.isPresent()) {
            return "Permiso no encontrado";
        }
        if (dto.getPermissionType() == null || dto.getPageRoleID() == null) {
            return "Tipo de permiso y PageRole deben ser proporcionados";
        }
        int pageRoleID = dto.getPageRoleID().getPageRoleID();
        String permissionType = dto.getPermissionType().name();

        if (!pageRoleRepository.existsById(pageRoleID)) {
            return "PageRole no encontrado";
        }
        // Solo impedir si el nuevo permiso sería duplicado y no es el mismo registro
        PermissionRole current = permissionRoleOptional.get();
        boolean sameData = current.getPermissionType().equals(permissionType) && 
                           current.getPageRoleID().getPageRoleID() == pageRoleID;
        boolean duplicated = permissionRoleRepository.existsByPermissionTypeAndPageRoleID_PageRoleID(permissionType, pageRoleID);

        if (duplicated && !sameData) {
            return "Ya existe un permiso de ese tipo para este PageRole";
        }

        try {
            PageRole pageRole = pageRoleRepository.findById(pageRoleID).get();
            current.setPermissionType(permissionType);
            current.setPageRoleID(pageRole);
            permissionRoleRepository.save(current);
            return "Permiso actualizado correctamente";
        } catch (DataAccessException e) {
            return "Error de base de datos al actualizar el permiso";
        } catch (Exception e) {
            return "Error inesperado al actualizar el permiso";
        }
    }

    // Eliminar permiso por ID
    @Transactional
    public String deleteById(int id) {
        Optional<PermissionRole> permissionRoleOptional = permissionRoleRepository.findById(id);
        if (!permissionRoleOptional.isPresent()) {
            return "Permiso no encontrado";
        }
        try {
            permissionRoleRepository.deleteById(id);
            return "Permiso eliminado correctamente";
        } catch (DataAccessException e) {
            return "Error de base de datos al eliminar el permiso";
        } catch (Exception e) {
            return "Error inesperado al eliminar el permiso";
        }
    }

    // Convertir DTO a entidad
    public PermissionRole convertToModel(PermissionRoleDTO dto) {
        return new PermissionRole(0, dto.getPermissionType().name(), dto.getPageRoleID());
    }

    // Convertir entidad a DTO
    public PermissionRoleDTO convertToDTO(PermissionRole pr) {
        PermissionRoleDTO.PermissionType type = PermissionRoleDTO.PermissionType.valueOf(pr.getPermissionType());
        return new PermissionRoleDTO(type, pr.getPageRoleID());
    }
}