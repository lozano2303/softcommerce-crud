package com.Cristofer.SoftComerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Cristofer.SoftComerce.DTO.PaymentOrderDTO;
import com.Cristofer.SoftComerce.DTO.ResponseDTO;
import com.Cristofer.SoftComerce.model.Order;
import com.Cristofer.SoftComerce.model.Payment;
import com.Cristofer.SoftComerce.model.PaymentOrder;
import com.Cristofer.SoftComerce.model.PaymentOrderId;
import com.Cristofer.SoftComerce.repository.IOrder;
import com.Cristofer.SoftComerce.repository.IPayment;
import com.Cristofer.SoftComerce.repository.IPaymentOrder;

@Service
public class PaymentOrderService {

    @Autowired
    private IPaymentOrder paymentOrderRepository;

    @Autowired
    private IPayment paymentRepository;

    @Autowired
    private IOrder orderRepository;

    // Listar todas las relaciones payment-order
    public List<PaymentOrder> findAll() {
        return paymentOrderRepository.findAll();
    }

    // Buscar una relación por ID
    public Optional<PaymentOrder> findById(PaymentOrderId id) {
        return paymentOrderRepository.findById(id);
    }

    // Método para filtrar relaciones payment-order
    public List<PaymentOrder> filterPaymentOrders(Integer paymentID, Integer orderID) {
        return paymentOrderRepository.filterPaymentOrders(paymentID, orderID);
    }

    @Transactional
    public ResponseDTO save(PaymentOrderDTO paymentOrderDTO) {
        try {
            // Validación 1: Campos requeridos
            if (paymentOrderDTO.getPaymentID() <= 0 || paymentOrderDTO.getOrderID() <= 0) {
                return new ResponseDTO("error", "Los IDs de pago y orden deben ser mayores a cero");
            }

            // Validación 2: Verificar existencia y estado del pago
            Optional<Payment> paymentEntity = paymentRepository.findById(paymentOrderDTO.getPaymentID());
            if (!paymentEntity.isPresent()) {
                return new ResponseDTO("error", "No se encontró el pago con ID: " + paymentOrderDTO.getPaymentID());
            }
            if (!paymentEntity.get().isStatus()) {
                return new ResponseDTO("error", "El pago #" + paymentOrderDTO.getPaymentID() + " está inactivo");
            }

            // Validación 3: Verificar existencia de la orden
            Optional<Order> orderEntity = orderRepository.findById(paymentOrderDTO.getOrderID());
            if (!orderEntity.isPresent()) {
                return new ResponseDTO("error", "No se encontró la orden con ID: " + paymentOrderDTO.getOrderID());
            }

            // Validación 4: Verificar relación duplicada
            PaymentOrderId id = new PaymentOrderId();
            id.setPaymentID(paymentOrderDTO.getPaymentID());
            id.setOrderID(paymentOrderDTO.getOrderID());

            if (paymentOrderRepository.existsById(id)) {
                return new ResponseDTO("error", "Ya existe una relación entre el pago #" +
                    paymentOrderDTO.getPaymentID() + " y la orden #" +
                    paymentOrderDTO.getOrderID());
            }

            // Crear y guardar la nueva relación
            PaymentOrder newPaymentOrder = new PaymentOrder();
            newPaymentOrder.setId(id);
            newPaymentOrder.setPayment(paymentEntity.get());
            newPaymentOrder.setOrder(orderEntity.get());
            paymentOrderRepository.save(newPaymentOrder);

            // Actualizar el estado de la orden
            Order orderToUpdate = orderEntity.get();
            if (!orderToUpdate.isStatus()) {
                orderToUpdate.setStatus(true);
                orderRepository.save(orderToUpdate);
            }

            return new ResponseDTO(
                "success",
                "Relación creada: Pago #" + paymentOrderDTO.getPaymentID() +
                " asignado a Orden #" + paymentOrderDTO.getOrderID()
            );

        } catch (DataAccessException e) {
            return new ResponseDTO(
                "error",
                "Error de base de datos: " + e.getMostSpecificCause().getMessage()
            );
        } catch (Exception e) {
            return new ResponseDTO(
                "error",
                "Error inesperado: " + e.getMessage()
            );
        }
    }

    @Transactional
    public ResponseDTO update(PaymentOrderId id, PaymentOrderDTO paymentOrderDTO) {
        try {
            // Validación 1: Verificar existencia de la relación
            Optional<PaymentOrder> existingPaymentOrder = paymentOrderRepository.findById(id);
            if (!existingPaymentOrder.isPresent()) {
                return new ResponseDTO(
                    "error",
                    "No se encontró la relación con Pago #" + id.getPaymentID() +
                    " y Orden #" + id.getOrderID()
                );
            }

            // Validación 2: Verificar nuevo pago
            Optional<Payment> paymentEntity = paymentRepository.findById(paymentOrderDTO.getPaymentID());
            if (!paymentEntity.isPresent()) {
                return new ResponseDTO(
                    "error",
                    "No se encontró el pago con ID: " + paymentOrderDTO.getPaymentID()
                );
            }

            // Validación 3: Verificar nueva orden
            Optional<Order> orderEntity = orderRepository.findById(paymentOrderDTO.getOrderID());
            if (!orderEntity.isPresent()) {
                return new ResponseDTO(
                    "error",
                    "No se encontró la orden con ID: " + paymentOrderDTO.getOrderID()
                );
            }

            // Actualizar la relación
            PaymentOrder paymentOrderToUpdate = existingPaymentOrder.get();
            paymentOrderToUpdate.setPayment(paymentEntity.get());
            paymentOrderToUpdate.setOrder(orderEntity.get());
            paymentOrderRepository.save(paymentOrderToUpdate);

            return new ResponseDTO(
                "success",
                "Relación actualizada exitosamente"
            );

        } catch (DataAccessException e) {
            return new ResponseDTO(
                "error",
                "Error de base de datos al actualizar: " + e.getMostSpecificCause().getMessage()
            );
        } catch (Exception e) {
            return new ResponseDTO(
                "error",
                "Error inesperado al actualizar: " + e.getMessage()
            );
        }
    }

    @Transactional
    public ResponseDTO deleteById(PaymentOrderId id) {
        try {
            // Verificar existencia
            if (!paymentOrderRepository.existsById(id)) {
                return new ResponseDTO(
                    "error",
                    "No se encontró la relación con Pago #" + id.getPaymentID() +
                    " y Orden #" + id.getOrderID()
                );
            }

            paymentOrderRepository.deleteById(id);
            return new ResponseDTO(
                "success",
                "Relación eliminada: Pago #" + id.getPaymentID() +
                " y Orden #" + id.getOrderID()
            );

        } catch (DataAccessException e) {
            return new ResponseDTO(
                "error",
                "Error de base de datos al eliminar: " + e.getMostSpecificCause().getMessage()
            );
        } catch (Exception e) {
            return new ResponseDTO(
                "error",
                "Error inesperado al eliminar: " + e.getMessage()
            );
        }
    }

    // Métodos de conversión
    public PaymentOrderDTO convertToDTO(PaymentOrder paymentOrder) {
        return new PaymentOrderDTO(
            paymentOrder.getPayment().getPaymentID(),
            paymentOrder.getOrder().getOrderID()
        );
    }

    public PaymentOrder convertToModel(PaymentOrderDTO paymentOrderDTO) {
        PaymentOrderId id = new PaymentOrderId();
        id.setPaymentID(paymentOrderDTO.getPaymentID());
        id.setOrderID(paymentOrderDTO.getOrderID());

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setId(id);

        return paymentOrder;
    }
}