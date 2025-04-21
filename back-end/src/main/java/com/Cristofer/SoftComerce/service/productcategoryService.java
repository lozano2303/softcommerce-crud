package com.Cristofer.SoftComerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Cristofer.SoftComerce.DTO.productcategoryDTO;
import com.Cristofer.SoftComerce.model.category;
import com.Cristofer.SoftComerce.model.product;
import com.Cristofer.SoftComerce.model.productcategory;
import com.Cristofer.SoftComerce.model.productcategoryId;
import com.Cristofer.SoftComerce.repository.Icategory;
import com.Cristofer.SoftComerce.repository.Iproduct;
import com.Cristofer.SoftComerce.repository.Iproductcategory;

@Service
public class productcategoryService {

    @Autowired
    private Iproductcategory productcategoryRepository;

    @Autowired
    private Iproduct productRepository;

    @Autowired
    private Icategory categoryRepository;

    // Guardar una relación product-category
    public void save(productcategoryDTO productcategoryDTO) {
        product product = productRepository.findById(productcategoryDTO.getProductID()).orElse(null);
        category category = categoryRepository.findById(productcategoryDTO.getCategoryID()).orElse(null);

        if (product != null && category != null) {
            productcategoryId id = new productcategoryId();
            id.setProductID(productcategoryDTO.getProductID());
            id.setCategoryID(productcategoryDTO.getCategoryID());

            productcategory productcategory = new productcategory();
            productcategory.setId(id);
            productcategory.setProduct(product);
            productcategory.setCategory(category);

            productcategoryRepository.save(productcategory);
        }
    }

    // Convertir de productcategory a productcategoryDTO
    public productcategoryDTO convertToDTO(productcategory productcategory) {
        return new productcategoryDTO(
            productcategory.getProduct().getProductID(),
            productcategory.getCategory().getcategoryID()
        );
    }

    // Convertir de productcategoryDTO a productcategory
    public productcategory convertToModel(productcategoryDTO productcategoryDTO) {
        productcategoryId id = new productcategoryId();
        id.setProductID(productcategoryDTO.getProductID());
        id.setCategoryID(productcategoryDTO.getCategoryID());

        productcategory productcategory = new productcategory();
        productcategory.setId(id);

        return productcategory;
    }
}