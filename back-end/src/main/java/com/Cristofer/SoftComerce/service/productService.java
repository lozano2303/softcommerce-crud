package com.Cristofer.SoftComerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Cristofer.SoftComerce.DTO.productDTO;
import com.Cristofer.SoftComerce.model.product;
import com.Cristofer.SoftComerce.repository.Iproduct;

import java.time.LocalDateTime;

@Service
public class productService {
    @Autowired
    private Iproduct data;

    public void save (productDTO productDTO) {
        product productRegister = convertToModel(productDTO);
        data.save(productRegister);
    }

    public productDTO convertToDTO(product product) {
        productDTO productdto = new productDTO(
            product.getname(),
            product.getdescription(),
            product.getprice(),
            product.getstock());
            return productdto;
    }

    public product convertToModel(productDTO productDTO){
        product product = new product(
            0,
            productDTO.getName(),
            productDTO.getDescription(),
            productDTO.getPrice(),
            productDTO.getStock(),
            LocalDateTime.now());
        return product;
    }
}
