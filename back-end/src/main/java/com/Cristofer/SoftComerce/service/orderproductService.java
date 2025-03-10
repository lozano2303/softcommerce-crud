package com.Cristofer.SoftComerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Cristofer.SoftComerce.DTO.orderproductDTO;
import com.Cristofer.SoftComerce.model.order;
import com.Cristofer.SoftComerce.model.orderproduct;
import com.Cristofer.SoftComerce.model.orderproductId;
import com.Cristofer.SoftComerce.model.product;
import com.Cristofer.SoftComerce.repository.Iorder;
import com.Cristofer.SoftComerce.repository.Iorderproduct;
import com.Cristofer.SoftComerce.repository.Iproduct;

@Service
public class orderproductService {

    @Autowired
    private Iorderproduct orderproductRepository;

    @Autowired
    private Iorder orderRepository;

    @Autowired
    private Iproduct productRepository;

    // Guardar una relación order-product
    public void save(orderproductDTO orderproductDTO) {
        order order = orderRepository.findById(orderproductDTO.getOrderID()).orElse(null);
        product product = productRepository.findById(orderproductDTO.getProductID()).orElse(null);

        if (order != null && product != null) {
            orderproductId id = new orderproductId();
            id.setOrderID(orderproductDTO.getOrderID());
            id.setProductID(orderproductDTO.getProductID());

            orderproduct orderproduct = new orderproduct();
            orderproduct.setId(id);
            orderproduct.setOrder(order);
            orderproduct.setProduct(product);
            orderproduct.setQuantity(orderproductDTO.getQuantity());
            orderproduct.setSubtotal(orderproductDTO.getSubtotal());

            orderproductRepository.save(orderproduct);
        }
    }

    // Convertir de orderproduct a orderproductDTO
    public orderproductDTO convertToDTO(orderproduct orderproduct) {
        return new orderproductDTO(
            orderproduct.getOrder().getOrderID(),
            orderproduct.getProduct().getproductID(),
            orderproduct.getQuantity(),
            orderproduct.getSubtotal()
        );
    }

    // Convertir de orderproductDTO a orderproduct
    public orderproduct convertToModel(orderproductDTO orderproductDTO) {
        orderproductId id = new orderproductId();
        id.setOrderID(orderproductDTO.getOrderID());
        id.setProductID(orderproductDTO.getProductID());

        orderproduct orderproduct = new orderproduct();
        orderproduct.setId(id);
        orderproduct.setQuantity(orderproductDTO.getQuantity());
        orderproduct.setSubtotal(orderproductDTO.getSubtotal());

        return orderproduct;
    }
}