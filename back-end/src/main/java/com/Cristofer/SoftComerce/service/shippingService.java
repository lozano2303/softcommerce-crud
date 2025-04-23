package com.Cristofer.SoftComerce.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Cristofer.SoftComerce.DTO.responseDTO;
import com.Cristofer.SoftComerce.DTO.shippingDTO;
import com.Cristofer.SoftComerce.model.order;
import com.Cristofer.SoftComerce.model.shipping;
import com.Cristofer.SoftComerce.repository.Iorder;
import com.Cristofer.SoftComerce.repository.Ishipping;

@Service
public class shippingService {

    @Autowired
    private Ishipping shippingRepo;

    @Autowired
    private Iorder orderRepo;

    // Listar todos los envíos
    public List<shipping> findAll() {
        return shippingRepo.findAll();
    }

    // Buscar envío por ID
    public Optional<shipping> findById(int id) {
        return shippingRepo.findById(id);
    }

    // Verificar si un envío existe por ID
    public boolean existsById(int id) {
        return shippingRepo.existsById(id);
    }

    @Autowired
    private Ishipping shippingRepository; // Asegúrate de que está correctamente inyectado

    // Método para filtrar envíos por campos opcionales
    public List<shippingDTO> filterShippings(Integer orderID, String address, String city, String country, String postalCode) {
        List<shipping> shippings = shippingRepository.filterShippings(orderID, address, city, country, postalCode);
        return shippings.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Guardar envío con validaciones
    @Transactional
    public responseDTO save(shippingDTO shippingDTO) {
        // Validar que la orden exista
        Optional<order> orderOpt = orderRepo.findById(shippingDTO.getOrderID());
        if (!orderOpt.isPresent()) {
            return new responseDTO("Orden no encontrada", "error");
        }

        // Validaciones adicionales
        if (shippingDTO.getAddress().length() < 5 || shippingDTO.getAddress().length() > 100) {
            return new responseDTO("La dirección debe tener entre 5 y 100 caracteres", "error");
        }

        if (shippingDTO.getCity().length() < 2 || shippingDTO.getCity().length() > 50) {
            return new responseDTO("La ciudad debe tener entre 2 y 50 caracteres", "error");
        }

        try {
            // Convertir DTO a entidad y guardar
            shipping shippingEntity = convertToModel(shippingDTO, orderOpt.get());
            shippingRepo.save(shippingEntity);

            return new responseDTO("Envío registrado correctamente", "success");
        } catch (DataAccessException e) {
            return new responseDTO("Error de base de datos al guardar el envío", "error");
        } catch (Exception e) {
            return new responseDTO("Error inesperado al guardar el envío", "error");
        }
    }

    // Actualizar envío por ID
    @Transactional
    public responseDTO update(int id, shippingDTO shippingDTO) {
        Optional<shipping> existingShipping = findById(id);
        if (!existingShipping.isPresent()) {
            return new responseDTO("Envío no encontrado", "error");
        }

        // Validar que la orden exista
        Optional<order> orderOpt = orderRepo.findById(shippingDTO.getOrderID());
        if (!orderOpt.isPresent()) {
            return new responseDTO("Orden no encontrada", "error");
        }

        // Validaciones adicionales
        if (shippingDTO.getAddress().length() < 5 || shippingDTO.getAddress().length() > 100) {
            return new responseDTO("La dirección debe tener entre 5 y 100 caracteres", "error");
        }

        if (shippingDTO.getCity().length() < 2 || shippingDTO.getCity().length() > 50) {
            return new responseDTO("La ciudad debe tener entre 2 y 50 caracteres", "error");
        }

        try {
            // Actualizar datos del envío
            shipping shippingToUpdate = existingShipping.get();
            shippingToUpdate.setOrder(orderOpt.get());
            shippingToUpdate.setAddress(shippingDTO.getAddress());
            shippingToUpdate.setCity(shippingDTO.getCity());
            shippingToUpdate.setCountry(shippingDTO.getCountry());
            shippingToUpdate.setPostal_code(shippingDTO.getPostal_code());
            shippingToUpdate.setCreated_at(LocalDateTime.now());

            shippingRepo.save(shippingToUpdate);

            return new responseDTO("Envío actualizado exitosamente", "success");
        } catch (DataAccessException e) {
            return new responseDTO("Error de base de datos al actualizar el envío", "error");
        } catch (Exception e) {
            return new responseDTO("Error inesperado al actualizar el envío", "error");
        }
    }

    // Eliminar envío por ID
    @Transactional
    public responseDTO deleteById(int id) {
        Optional<shipping> shippingEntity = findById(id);
        if (!shippingEntity.isPresent()) {
            return new responseDTO("Envío no encontrado", "error");
        }

        try {
            shippingRepo.deleteById(id);
            return new responseDTO("Envío eliminado correctamente", "success");
        } catch (DataAccessException e) {
            return new responseDTO("Error de base de datos al eliminar el envío", "error");
        } catch (Exception e) {
            return new responseDTO("Error inesperado al eliminar el envío", "error");
        }
    }

    // Convertir entidad a DTO
    public shippingDTO convertToDTO(shipping shippingEntity) {
        return new shippingDTO(
            shippingEntity.getOrder().getOrderID(),
            shippingEntity.getAddress(),
            shippingEntity.getCity(),
            shippingEntity.getCountry(),
            shippingEntity.getPostal_code(),
            shippingEntity.getCreated_at()
        );
    }

    // Convertir DTO a entidad
    public shipping convertToModel(shippingDTO shippingDTO, order order) {
        return new shipping(
            0, // ID autogenerado
            order,
            shippingDTO.getAddress(),
            shippingDTO.getCity(),
            shippingDTO.getCountry(),
            shippingDTO.getPostal_code(),
            true, // Estado inicial como true
            LocalDateTime.now()
        );
    }
}