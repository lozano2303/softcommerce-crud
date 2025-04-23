package com.Cristofer.SoftComerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
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

    // Guardar una relación payment-order con validaciones
    @Transactional
    public responseDTO save(paymentorderDTO paymentorderDTO) {
        // Validar que el método de pago exista
        Optional<payment> paymentEntity = paymentRepository.findById(paymentorderDTO.getPaymentID());
        if (!paymentEntity.isPresent()) {
            return new responseDTO("Método de pago no encontrado", "error");
        }

        // Validar que la orden exista
        Optional<order> orderEntity = orderRepository.findById(paymentorderDTO.getOrderID());
        if (!orderEntity.isPresent()) {
            return new responseDTO("Orden no encontrada", "error");
        }

        try {
            // Convertir DTO a entidad y guardar
            paymentorderId id = new paymentorderId();
            id.setPaymentID(paymentorderDTO.getPaymentID());
            id.setOrderID(paymentorderDTO.getOrderID());

            paymentorder paymentorder = new paymentorder();
            paymentorder.setId(id);
            paymentorder.setPayment(paymentEntity.get());
            paymentorder.setOrder(orderEntity.get());

            paymentorderRepository.save(paymentorder);

            return new responseDTO("Relación método de pago-orden registrada correctamente", "success");
        } catch (DataAccessException e) {
            return new responseDTO("Error de base de datos al guardar la relación", "error");
        } catch (Exception e) {
            return new responseDTO("Error inesperado al guardar la relación", "error");
        }
    }

    // Actualizar una relación por ID
    @Transactional
    public responseDTO update(paymentorderId id, paymentorderDTO paymentorderDTO) {
        Optional<paymentorder> existingPaymentOrder = findById(id);
        if (!existingPaymentOrder.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "La relación no existe");
        }

        // Validar que el método de pago exista
        Optional<payment> paymentEntity = paymentRepository.findById(paymentorderDTO.getPaymentID());
        if (!paymentEntity.isPresent()) {
            return new responseDTO("Método de pago no encontrado", "error");
        }

        // Validar que la orden exista
        Optional<order> orderEntity = orderRepository.findById(paymentorderDTO.getOrderID());
        if (!orderEntity.isPresent()) {
            return new responseDTO("Orden no encontrada", "error");
        }

        try {
            // Actualizar datos de la relación
            paymentorder paymentOrderToUpdate = existingPaymentOrder.get();
            paymentOrderToUpdate.setPayment(paymentEntity.get());
            paymentOrderToUpdate.setOrder(orderEntity.get());

            paymentorderRepository.save(paymentOrderToUpdate);

            return new responseDTO("Relación método de pago-orden actualizada exitosamente", "success");
        } catch (DataAccessException e) {
            return new responseDTO("Error de base de datos al actualizar la relación", "error");
        } catch (Exception e) {
            return new responseDTO("Error inesperado al actualizar la relación", "error");
        }
    }

    // Eliminar una relación por ID
    @Transactional
    public responseDTO deleteById(paymentorderId id) {
        Optional<paymentorder> existingPaymentOrder = findById(id);
        if (!existingPaymentOrder.isPresent()) {
            return new responseDTO("Relación método de pago-orden no encontrada", "error");
        }

        try {
            paymentorderRepository.deleteById(id);
            return new responseDTO("Relación método de pago-orden eliminada correctamente", "success");
        } catch (DataAccessException e) {
            return new responseDTO("Error de base de datos al eliminar la relación", "error");
        } catch (Exception e) {
            return new responseDTO("Error inesperado al eliminar la relación", "error");
        }
    }

    // Convertir de paymentorder a paymentorderDTO
    public paymentorderDTO convertToDTO(paymentorder paymentorder) {
        return new paymentorderDTO(
            paymentorder.getPayment().getPaymentID(),
            paymentorder.getOrder().getOrderID()
        );
    }

    // Convertir de paymentorderDTO a paymentorder
    public paymentorder convertToModel(paymentorderDTO paymentorderDTO) {
        paymentorderId id = new paymentorderId();
        id.setPaymentID(paymentorderDTO.getPaymentID());
        id.setOrderID(paymentorderDTO.getOrderID());

        paymentorder paymentorder = new paymentorder();
        paymentorder.setId(id);

        return paymentorder;
    }
}