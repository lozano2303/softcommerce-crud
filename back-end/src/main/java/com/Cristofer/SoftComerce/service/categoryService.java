package com.Cristofer.SoftComerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Cristofer.SoftComerce.DTO.categoryDTO;
import com.Cristofer.SoftComerce.model.category;
import com.Cristofer.SoftComerce.repository.Icategory;



@Service
public class categoryService {
    @Autowired
    private Icategory data;

    public void save(categoryDTO categoryDTO){
        category categoryRegister = convertToModel(categoryDTO);
        data.save(categoryRegister);
    }

    public categoryDTO convertToDTO(category category){
        categoryDTO categorydto = new categoryDTO(
        category.getCategoryName());
        return categorydto;
        
    }

    public category convertToModel(categoryDTO categoryDTO){
        category category = new category(
            0,
        categoryDTO.getCategoryName());
        return category;
    }

}
