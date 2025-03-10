package com.Cristofer.SoftComerce.service;

import com.Cristofer.SoftComerce.DTO.paymentorderDTO;
import com.Cristofer.SoftComerce.model.order;
import com.Cristofer.SoftComerce.model.payment;
import com.Cristofer.SoftComerce.model.paymentorder;
import com.Cristofer.SoftComerce.model.paymentorderId;
import com.Cristofer.SoftComerce.repository.Ipaymentorder;
import com.Cristofer.SoftComerce.repository.Ipayment;
import com.Cristofer.SoftComerce.repository.Iorder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class paymentorderService {

    @Autowired
    private Ipaymentorder paymentorderRepository;

    @Autowired
    private Ipayment paymentRepository;

    @Autowired
    private Iorder orderRepository;

    // Guardar una relación payment-order
    public void save(paymentorderDTO paymentorderDTO) {
        payment payment = paymentRepository.findById(paymentorderDTO.getPaymentID()).orElse(null);
        order order = orderRepository.findById(paymentorderDTO.getOrderID()).orElse(null);

        if (payment != null && order != null) {
            paymentorderId id = new paymentorderId();
            id.setPaymentID(paymentorderDTO.getPaymentID());
            id.setOrderID(paymentorderDTO.getOrderID());

            paymentorder paymentorder = new paymentorder();
            paymentorder.setId(id);
            paymentorder.setPayment(payment);
            paymentorder.setOrder(order);

            paymentorderRepository.save(paymentorder);
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