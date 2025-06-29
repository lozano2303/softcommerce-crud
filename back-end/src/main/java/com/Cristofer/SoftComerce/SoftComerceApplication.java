package com.Cristofer.SoftComerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SoftComerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoftComerceApplication.class, args);

		// // Generar una nueva clave HMAC-SHA256 segura:
        //Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        //byte[] keyBytes = key.getEncoded();
        //System.out.println("Generated key: " + Base64.getEncoder().encodeToString(keyBytes));
	}

}
