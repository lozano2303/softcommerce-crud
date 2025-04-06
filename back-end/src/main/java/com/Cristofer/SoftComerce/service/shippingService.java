package com.Cristofer.SoftComerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Cristofer.SoftComerce.DTO.shippingDTO;
import com.Cristofer.SoftComerce.model.shipping;
import com.Cristofer.SoftComerce.model.order;
import com.Cristofer.SoftComerce.repository.Ishipping;
import com.Cristofer.SoftComerce.repository.Iorder;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class shippingService {
    
    @Autowired
    private Ishipping shippingRepo;

    @Autowired
    private Iorder orderRepo;

    public void save(shippingDTO shippingDTO) {
        Optional<order> orderOpt = orderRepo.findById(shippingDTO.getOrderID());
        if (orderOpt.isPresent()) {
            shipping shippingRegister = convertToModel(shippingDTO, orderOpt.get());
            shippingRepo.save(shippingRegister);
        } else {
            throw new RuntimeException("Order not found");
        }
    }

    public shippingDTO convertToDTO(shipping shipping) {
        return new shippingDTO(
            shipping.getOrder().getOrderID(),
            shipping.getAddress(),
            shipping.getCity(),
            shipping.getCountry(),
            shipping.getPostal_code(),
            shipping.getCreated_at()
        );
    }

    public shipping convertToModel(shippingDTO shippingDTO, order order) {
        return new shipping(
            0,
            order,
            shippingDTO.getAddress(),
            shippingDTO.getCity(),
            shippingDTO.getCountry(),
            shippingDTO.getPostal_code(),
            true,
            LocalDateTime.now()
        );
    }
}
