package com.Cristofer.SoftComerce.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Cristofer.SoftComerce.DTO.ResponseDTO;
import com.Cristofer.SoftComerce.DTO.ShippingDTO;
import com.Cristofer.SoftComerce.model.Order;
import com.Cristofer.SoftComerce.model.Shipping;
import com.Cristofer.SoftComerce.repository.IOrder;
import com.Cristofer.SoftComerce.repository.IShipping;

@Service
public class ShippingService {

    @Autowired
    private IShipping shippingRepo;

    @Autowired
    private IOrder orderRepo;

    // Listar todos los envíos
    public List<Shipping> findAll() {
        return shippingRepo.findAll();
    }

    // Buscar envío por ID
    public Optional<Shipping> findById(int id) {
        return shippingRepo.findById(id);
    }

    // Verificar si un envío existe por ID
    public boolean existsById(int id) {
        return shippingRepo.existsById(id);
    }

    @Autowired
    private IShipping shippingRepository; // Asegúrate de que está correctamente inyectado

    // Método para filtrar envíos por campos opcionales
    public List<ShippingDTO> filterShippings(Integer orderID, String address, String city, String country, String postalCode) {
        List<Shipping> shippings = shippingRepository.filterShippings(orderID, address, city, country, postalCode);
        return shippings.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Guardar envío con validaciones
    @Transactional
    public ResponseDTO save(ShippingDTO shippingDTO) {
        // Validar que la orden exista
        Optional<Order> orderOpt = orderRepo.findById(shippingDTO.getOrderID());
        if (!orderOpt.isPresent()) {
            return new ResponseDTO("error", "Orden no encontrada");
        }

        // Validaciones adicionales
        if (shippingDTO.getAddress().length() < 5 || shippingDTO.getAddress().length() > 100) {
            return new ResponseDTO("error", "La dirección debe tener entre 5 y 100 caracteres");
        }

        if (shippingDTO.getCity().length() < 2 || shippingDTO.getCity().length() > 50) {
            return new ResponseDTO("error", "La ciudad debe tener entre 2 y 50 caracteres");
        }

        try {
            // Convertir DTO a entidad y guardar
            Shipping shippingEntity = convertToModel(shippingDTO, orderOpt.get());
            shippingRepo.save(shippingEntity);

            return new ResponseDTO("success", "Envío registrado correctamente");
        } catch (DataAccessException e) {
            return new ResponseDTO("error", "Error de base de datos al guardar el envío");
        } catch (Exception e) {
            return new ResponseDTO("error", "Error inesperado al guardar el envío");
        }
    }

    // Actualizar envío por ID
    @Transactional
    public ResponseDTO update(int id, ShippingDTO shippingDTO) {
        Optional<Shipping> existingShipping = findById(id);
        if (!existingShipping.isPresent()) {
            return new ResponseDTO("error", "Envío no encontrado");
        }

        // Validar que la orden exista
        Optional<Order> orderOpt = orderRepo.findById(shippingDTO.getOrderID());
        if (!orderOpt.isPresent()) {
            return new ResponseDTO("error", "Orden no encontrada");
        }

        // Validaciones adicionales
        if (shippingDTO.getAddress().length() < 5 || shippingDTO.getAddress().length() > 100) {
            return new ResponseDTO("error", "La dirección debe tener entre 5 y 100 caracteres");
        }

        if (shippingDTO.getCity().length() < 2 || shippingDTO.getCity().length() > 50) {
            return new ResponseDTO("error", "La ciudad debe tener entre 2 y 50 caracteres");
        }

        try {
            // Actualizar datos del envío
            Shipping shippingToUpdate = existingShipping.get();
            shippingToUpdate.setOrder(orderOpt.get());
            shippingToUpdate.setAddress(shippingDTO.getAddress());
            shippingToUpdate.setCity(shippingDTO.getCity());
            shippingToUpdate.setCountry(shippingDTO.getCountry());
            shippingToUpdate.setPostalCode(shippingDTO.getPostalCode());
            shippingToUpdate.setCreatedAt(LocalDateTime.now());

            shippingRepo.save(shippingToUpdate);

            return new ResponseDTO("success", "Envío actualizado exitosamente");
        } catch (DataAccessException e) {
            return new ResponseDTO("error", "Error de base de datos al actualizar el envío");
        } catch (Exception e) {
            return new ResponseDTO("error", "Error inesperado al actualizar el envío");
        }
    }

    // Eliminar envío por ID (desactivar)
    @Transactional
    public ResponseDTO deactivateShipping(int id) {
        Optional<Shipping> shippingEntity = shippingRepository.findById(id);
        if (!shippingEntity.isPresent()) {
            return new ResponseDTO("error", "Envío no encontrado");
        }

        try {
            Shipping shippingToDeactivate = shippingEntity.get();
            shippingToDeactivate.setStatus(false);
            shippingRepository.save(shippingToDeactivate);
            return new ResponseDTO("success", "Envío desactivado correctamente");
        } catch (DataAccessException e) {
            return new ResponseDTO("error", "Error de base de datos al desactivar el envío");
        } catch (Exception e) {
            return new ResponseDTO("error", "Error inesperado al desactivar el envío");
        }
    }

    // Reactivar envío por ID
    @Transactional
    public ResponseDTO reactivateShipping(int id) {
        Optional<Shipping> shippingEntity = shippingRepository.findById(id);
        if (!shippingEntity.isPresent()) {
            return new ResponseDTO("error", "Envío no encontrado");
        }

        try {
            Shipping shippingToReactivate = shippingEntity.get();
            shippingToReactivate.setStatus(true);
            shippingRepository.save(shippingToReactivate);
            return new ResponseDTO("success", "Envío reactivado correctamente");
        } catch (DataAccessException e) {
            return new ResponseDTO("error", "Error de base de datos al reactivar el envío");
        } catch (Exception e) {
            return new ResponseDTO("error", "Error inesperado al reactivar el envío");
        }
    }

    // Convertir entidad a DTO
    public ShippingDTO convertToDTO(Shipping shippingEntity) {
        return new ShippingDTO(
            shippingEntity.getOrder().getOrderID(),
            shippingEntity.getAddress(),
            shippingEntity.getCity(),
            shippingEntity.getCountry(),
            shippingEntity.getPostalCode(),
            shippingEntity.getCreatedAt()
        );
    }

    // Convertir DTO a entidad
    public Shipping convertToModel(ShippingDTO shippingDTO, Order order) {
        return new Shipping(
            0, // ID autogenerado
            order,
            shippingDTO.getAddress(),
            shippingDTO.getCity(),
            shippingDTO.getCountry(),
            shippingDTO.getPostalCode(),
            true, // Estado inicial como true
            LocalDateTime.now()
        );
    }
}