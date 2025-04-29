package com.Cristofer.SoftComerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Cristofer.SoftComerce.DTO.paymentorderDTO;
import com.Cristofer.SoftComerce.DTO.responseDTO;
import com.Cristofer.SoftComerce.model.order;
import com.Cristofer.SoftComerce.model.payment;
import com.Cristofer.SoftComerce.model.paymentorder;
import com.Cristofer.SoftComerce.model.paymentorderId;
import com.Cristofer.SoftComerce.repository.Iorder;
import com.Cristofer.SoftComerce.repository.Ipayment;
import com.Cristofer.SoftComerce.repository.Ipaymentorder;

@Service
public class paymentorderService {

    @Autowired
    private Ipaymentorder paymentorderRepository;

    @Autowired
    private Ipayment paymentRepository;

    @Autowired
    private Iorder orderRepository;

    // Listar todas las relaciones payment-order
    public List<paymentorder> findAll() {
        return paymentorderRepository.findAll();
    }

    // Buscar una relación por ID
    public Optional<paymentorder> findById(paymentorderId id) {
        return paymentorderRepository.findById(id);
    }

    // Método para filtrar relaciones payment-order
    public List<paymentorder> filterPaymentOrders(Integer paymentID, Integer orderID) {
        return paymentorderRepository.filterPaymentOrders(paymentID, orderID);
    }

    @Transactional
    public responseDTO save(paymentorderDTO paymentorderDTO) {
        try {
            // Validación 1: Campos requeridos
            if (paymentorderDTO.getPaymentID() <= 0 || paymentorderDTO.getOrderID() <= 0) {
                return new responseDTO("error", "Los IDs de pago y orden deben ser mayores a cero");
            }

            // Validación 2: Verificar existencia y estado del pago
            Optional<payment> paymentEntity = paymentRepository.findById(paymentorderDTO.getPaymentID());
            if (!paymentEntity.isPresent()) {
                return new responseDTO("error", "No se encontró el pago con ID: " + paymentorderDTO.getPaymentID());
            }
            if (!paymentEntity.get().isStatus()) {
                return new responseDTO("error", "El pago #" + paymentorderDTO.getPaymentID() + " está inactivo");
            }

            // Validación 3: Verificar existencia de la orden
            Optional<order> orderEntity = orderRepository.findById(paymentorderDTO.getOrderID());
            if (!orderEntity.isPresent()) {
                return new responseDTO("error", "No se encontró la orden con ID: " + paymentorderDTO.getOrderID());
            }

            // Validación 4: Verificar relación duplicada
            paymentorderId id = new paymentorderId();
            id.setPaymentID(paymentorderDTO.getPaymentID());
            id.setOrderID(paymentorderDTO.getOrderID());
            
            if (paymentorderRepository.existsById(id)) {
                return new responseDTO("error", "Ya existe una relación entre el pago #" + 
                    paymentorderDTO.getPaymentID() + " y la orden #" + 
                    paymentorderDTO.getOrderID());
            }

            // Crear y guardar la nueva relación
            paymentorder newPaymentOrder = new paymentorder();
            newPaymentOrder.setId(id);
            newPaymentOrder.setPayment(paymentEntity.get());
            newPaymentOrder.setOrder(orderEntity.get());
            paymentorderRepository.save(newPaymentOrder);

            // Actualizar el estado de la orden
            order orderToUpdate = orderEntity.get();
            if (!orderToUpdate.isStatus()) {
                orderToUpdate.setStatus(true);
                orderRepository.save(orderToUpdate);
            }

            return new responseDTO(
                "success", 
                "Relación creada: Pago #" + paymentorderDTO.getPaymentID() + 
                " asignado a Orden #" + paymentorderDTO.getOrderID()
            );

        } catch (DataAccessException e) {
            return new responseDTO(
                "error", 
                "Error de base de datos: " + e.getMostSpecificCause().getMessage()
            );
        } catch (Exception e) {
            return new responseDTO(
                "error", 
                "Error inesperado: " + e.getMessage()
            );
        }
    }

    @Transactional
    public responseDTO update(paymentorderId id, paymentorderDTO paymentorderDTO) {
        try {
            // Validación 1: Verificar existencia de la relación
            Optional<paymentorder> existingPaymentOrder = paymentorderRepository.findById(id);
            if (!existingPaymentOrder.isPresent()) {
                return new responseDTO(
                    "error",
                    "No se encontró la relación con Pago #" + id.getPaymentID() + 
                    " y Orden #" + id.getOrderID()
                );
            }

            // Validación 2: Verificar nuevo pago
            Optional<payment> paymentEntity = paymentRepository.findById(paymentorderDTO.getPaymentID());
            if (!paymentEntity.isPresent()) {
                return new responseDTO(
                    "error",
                    "No se encontró el pago con ID: " + paymentorderDTO.getPaymentID()
                );
            }

            // Validación 3: Verificar nueva orden
            Optional<order> orderEntity = orderRepository.findById(paymentorderDTO.getOrderID());
            if (!orderEntity.isPresent()) {
                return new responseDTO(
                    "error",
                    "No se encontró la orden con ID: " + paymentorderDTO.getOrderID()
                );
            }

            // Actualizar la relación
            paymentorder paymentOrderToUpdate = existingPaymentOrder.get();
            paymentOrderToUpdate.setPayment(paymentEntity.get());
            paymentOrderToUpdate.setOrder(orderEntity.get());
            paymentorderRepository.save(paymentOrderToUpdate);

            return new responseDTO(
                "success",
                "Relación actualizada exitosamente"
            );

        } catch (DataAccessException e) {
            return new responseDTO(
                "error",
                "Error de base de datos al actualizar: " + e.getMostSpecificCause().getMessage()
            );
        } catch (Exception e) {
            return new responseDTO(
                "error",
                "Error inesperado al actualizar: " + e.getMessage()
            );
        }
    }

    @Transactional
    public responseDTO deleteById(paymentorderId id) {
        try {
            // Verificar existencia
            if (!paymentorderRepository.existsById(id)) {
                return new responseDTO(
                    "error",
                    "No se encontró la relación con Pago #" + id.getPaymentID() + 
                    " y Orden #" + id.getOrderID()
                );
            }

            paymentorderRepository.deleteById(id);
            return new responseDTO(
                "success",
                "Relación eliminada: Pago #" + id.getPaymentID() + 
                " y Orden #" + id.getOrderID()
            );

        } catch (DataAccessException e) {
            return new responseDTO(
                "error",
                "Error de base de datos al eliminar: " + e.getMostSpecificCause().getMessage()
            );
        } catch (Exception e) {
            return new responseDTO(
                "error",
                "Error inesperado al eliminar: " + e.getMessage()
            );
        }
    }
    // Métodos de conversión
    public paymentorderDTO convertToDTO(paymentorder paymentorder) {
        return new paymentorderDTO(
            paymentorder.getPayment().getPaymentID(),
            paymentorder.getOrder().getOrderID()
        );
    }

    public paymentorder convertToModel(paymentorderDTO paymentorderDTO) {
        paymentorderId id = new paymentorderId();
        id.setPaymentID(paymentorderDTO.getPaymentID());
        id.setOrderID(paymentorderDTO.getOrderID());

        paymentorder paymentorder = new paymentorder();
        paymentorder.setId(id);

        return paymentorder;
    }
}