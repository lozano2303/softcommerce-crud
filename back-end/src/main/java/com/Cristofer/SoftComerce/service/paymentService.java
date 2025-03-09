package com.Cristofer.SoftComerce.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Cristofer.SoftComerce.DTO.paymentDTO;
import com.Cristofer.SoftComerce.model.payment;
import com.Cristofer.SoftComerce.model.user;
import com.Cristofer.SoftComerce.repository.Ipayment; // Asegúrate de tener un repositorio de usuarios
import com.Cristofer.SoftComerce.repository.Iuser;

@Service
public class paymentService {
    @Autowired
    private Ipayment data;

    @Autowired
    private Iuser userRepository;  // Agregar el repositorio para el usuario

    public void save(paymentDTO paymentDTO) {
        payment paymentRegister = convertToModel(paymentDTO);
        data.save(paymentRegister);
    }

    public paymentDTO convertToDTO(payment payment) {
        paymentDTO paymentDTO = new paymentDTO(
            payment.getUser().getuserID(),
            payment.getAmount(),
            payment.getMethod(),
            payment.getStatus()
        );
        return paymentDTO;
    }

    public payment convertToModel(paymentDTO paymentDTO) {
        user user = findUserById(paymentDTO.getUserID());
        payment payment = new payment(
            0,
            user,
            paymentDTO.getAmount(),
            paymentDTO.getMethod(),
            paymentDTO.getStatus(),
            LocalDateTime.now());
        return payment;
    }

    private user findUserById(int userID) {
        // Usando el repositorio para buscar el usuario por ID
        return userRepository.findById(userID).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
