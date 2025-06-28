package com.Cristofer.SoftComerce.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Cristofer.SoftComerce.DTO.ResponseDTO;
import com.Cristofer.SoftComerce.DTO.ReviewDTO;
import com.Cristofer.SoftComerce.model.Product;
import com.Cristofer.SoftComerce.model.Review;
import com.Cristofer.SoftComerce.model.User;
import com.Cristofer.SoftComerce.repository.IProduct;
import com.Cristofer.SoftComerce.repository.IReview;
import com.Cristofer.SoftComerce.repository.IUser;

@Service
public class ReviewService {

    @Autowired
    private IReview reviewRepository;

    // Método para obtener todas las reseñas
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    // Método para buscar una reseña por ID
    public Optional<Review> findById(int id) {
        return reviewRepository.findById(id);
    }

    public List<ReviewDTO> filterReviews(Integer rating, String comment, Integer userID, Integer productID) {
        List<Review> reviews = reviewRepository.filterReviews(rating, comment, userID, productID);
        return reviews.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Autowired
    private IUser userRepository;

    @Autowired
    private IProduct productRepository;

    // Guardar una reseña con validaciones
    @Transactional
    public ResponseDTO save(ReviewDTO reviewDTO) {
        // Validar que el usuario exista
        Optional<User> userEntity = userRepository.findById(reviewDTO.getUserID());
        if (!userEntity.isPresent()) {
            return new ResponseDTO("error", "Usuario no encontrado");
        }

        // Validar que el producto exista
        Optional<Product> productEntity = productRepository.findById(reviewDTO.getProductID());
        if (!productEntity.isPresent()) {
            return new ResponseDTO("error", "Producto no encontrado");
        }

        // Validaciones adicionales
        if (reviewDTO.getRating() < 1 || reviewDTO.getRating() > 5) {
            return new ResponseDTO("error", "La calificación debe estar entre 1 y 5");
        }

        if (reviewDTO.getComment().length() < 1 || reviewDTO.getComment().length() > 255) {
            return new ResponseDTO("error", "El comentario debe estar entre 1 y 255 caracteres");
        }

        try {
            // Convertir DTO a entidad y guardar
            Review reviewEntity = convertToModel(reviewDTO);
            reviewEntity.setUser(userEntity.get());
            reviewEntity.setProduct(productEntity.get());
            reviewRepository.save(reviewEntity);

            return new ResponseDTO("success", "Reseña registrada correctamente");
        } catch (DataAccessException e) {
            return new ResponseDTO("error", "Error de base de datos al guardar la reseña");
        } catch (Exception e) {
            return new ResponseDTO("error", "Error inesperado al guardar la reseña");
        }
    }

    // Actualizar una reseña por ID
    @Transactional
    public ResponseDTO update(int id, ReviewDTO reviewDTO) {
        Optional<Review> existingReview = reviewRepository.findById(id);
        if (!existingReview.isPresent()) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "La reseña no existe");
        }

        // Validar que el usuario exista
        Optional<User> userEntity = userRepository.findById(reviewDTO.getUserID());
        if (!userEntity.isPresent()) {
            return new ResponseDTO("error", "Usuario no encontrado");
        }

        // Validar que el producto exista
        Optional<Product> productEntity = productRepository.findById(reviewDTO.getProductID());
        if (!productEntity.isPresent()) {
            return new ResponseDTO("error", "Producto no encontrado");
        }

        // Validaciones adicionales
        if (reviewDTO.getRating() < 1 || reviewDTO.getRating() > 5) {
            return new ResponseDTO("error", "La calificación debe estar entre 1 y 5");
        }

        if (reviewDTO.getComment().length() < 1 || reviewDTO.getComment().length() > 255) {
            return new ResponseDTO("error", "El comentario debe estar entre 1 y 255 caracteres");
        }

        try {
            // Actualizar datos de la reseña
            Review reviewToUpdate = existingReview.get();
            reviewToUpdate.setRating(reviewDTO.getRating());
            reviewToUpdate.setComment(reviewDTO.getComment());
            reviewToUpdate.setCreatedAt(LocalDateTime.now());
            reviewToUpdate.setUser(userEntity.get());
            reviewToUpdate.setProduct(productEntity.get());

            reviewRepository.save(reviewToUpdate);

            return new ResponseDTO("success", "Reseña actualizada exitosamente");
        } catch (DataAccessException e) {
            return new ResponseDTO("error", "Error de base de datos al actualizar la reseña");
        } catch (Exception e) {
            return new ResponseDTO("error", "Error inesperado al actualizar la reseña");
        }
    }

    // Eliminar una reseña por ID
    @Transactional
    public ResponseDTO deleteById(int id) {
        Optional<Review> reviewEntity = reviewRepository.findById(id);
        if (!reviewEntity.isPresent()) {
            return new ResponseDTO("error", "Reseña no encontrada");
        }

        try {
            reviewRepository.deleteById(id);
            return new ResponseDTO("success", "Reseña eliminada correctamente");
        } catch (DataAccessException e) {
            return new ResponseDTO("error", "Error de base de datos al eliminar la reseña");
        } catch (Exception e) {
            return new ResponseDTO("error", "Error inesperado al eliminar la reseña");
        }
    }

    // Convertir entidad a DTO
    public ReviewDTO convertToDTO(Review reviewEntity) {
        return new ReviewDTO(
            reviewEntity.getRating(),
            reviewEntity.getComment(),
            reviewEntity.getUser().getUserID(),
            reviewEntity.getProduct().getProductID()
        );
    }

    // Convertir DTO a entidad
    public Review convertToModel(ReviewDTO reviewDTO) {
        Optional<User> userEntity = userRepository.findById(reviewDTO.getUserID());
        Optional<Product> productEntity = productRepository.findById(reviewDTO.getProductID());

        if (!userEntity.isPresent() || !productEntity.isPresent()) {
            throw new IllegalArgumentException("Usuario o Producto no encontrado");
        }

        return new Review(
            0, // ID autogenerado
            reviewDTO.getRating(),
            reviewDTO.getComment(),
            LocalDateTime.now(),
            userEntity.get(),
            productEntity.get()
        );
    }
}