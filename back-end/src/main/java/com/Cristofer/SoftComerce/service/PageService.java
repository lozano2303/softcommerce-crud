package com.Cristofer.SoftComerce.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Cristofer.SoftComerce.DTO.PageDTO;
import com.Cristofer.SoftComerce.model.Page;
import com.Cristofer.SoftComerce.repository.IPage;

@Service
public class PageService {

    @Autowired
    private IPage pageRepository;

    // Listar todas las páginas
    public List<Page> findAll() {
        return pageRepository.findAll();
    }

    // Buscar página por ID
    public Optional<Page> findById(int id) {
        return pageRepository.findById(id);
    }

    // Verificar si una página existe por ID
    public boolean existsById(int id) {
        return pageRepository.existsById(id);
    }

    // Registrar nueva página
    @Transactional
    public String register(PageDTO pageDTO) {
        if (pageRepository.existsByName(pageDTO.getName())) {
            return "El nombre de la página ya existe";
        }
        try {
            Page page = convertToModel(pageDTO);
            pageRepository.save(page);
            return "Página registrada correctamente";
        } catch (DataAccessException e) {
            return "Error de base de datos al guardar la página";
        } catch (Exception e) {
            return "Error inesperado al guardar la página";
        }
    }

    // Actualizar página por ID
    @Transactional
    public String update(int id, PageDTO pageDTO) {
        Optional<Page> pageOptional = pageRepository.findById(id);
        if (!pageOptional.isPresent()) {
            return "Página no encontrada";
        }

        try {
            Page page = pageOptional.get();
            if (!page.getName().equals(pageDTO.getName()) && pageRepository.existsByName(pageDTO.getName())) {
                return "El nombre de la página ya existe";
            }
            page.setName(pageDTO.getName());
            pageRepository.save(page);
            return "Página actualizada correctamente";
        } catch (DataAccessException e) {
            return "Error de base de datos al actualizar la página";
        } catch (Exception e) {
            return "Error inesperado al actualizar la página";
        }
    }

    // Eliminar página por ID
    @Transactional
    public String deleteById(int id) {
        Optional<Page> pageOptional = pageRepository.findById(id);
        if (!pageOptional.isPresent()) {
            return "Página no encontrada";
        }
        try {
            pageRepository.deleteById(id);
            return "Página eliminada correctamente";
        } catch (DataAccessException e) {
            return "Error de base de datos al eliminar la página";
        } catch (Exception e) {
            return "Error inesperado al eliminar la página";
        }
    }

    // Convertir DTO a entidad
    public Page convertToModel(PageDTO pageDTO) {
        return new Page(0, pageDTO.getName());
    }

    // Convertir entidad a DTO
    public PageDTO convertToDTO(Page page) {
        return new PageDTO(page.getName());
    }
}