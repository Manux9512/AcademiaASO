package com.empleados.api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.empleados.api.model.Auto;
import com.empleados.api.repository.AutoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * Clase para inicializar datos de prueba
 */
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(AutoRepository autoRepository) {
        return args -> {
            // Verificamos si ya hay datos cargados
            if (autoRepository.count() == 0) {
                // Creamos 20 empleados de prueba
                Auto[] automoviles = {
                    new Auto(null, "Juan", "Pérez","Audi","Q7","Rojo",LocalDate.of(2020, 1, 15),"juan.perez@example.com", 
                        LocalDate.of(2020, 1, 15), new BigDecimal("50000.00"), "Tecnología", 20),
                    new Auto(null, "María", "Gómez","BMW","X5","Azul",LocalDate.of(2020, 2, 20),"maria.gomez@example.com", 
                        LocalDate.of(2020, 2, 20), new BigDecimal("60000.00"), "Mecánica", 25),
                    new Auto(null, "Carlos", " Rodríguez","Mercedes-Benz","C-Class","Negro",LocalDate.of(2020, 3, 10),"carlos.rodriguez@example.com", 
                        LocalDate.of(2020, 3, 10), new BigDecimal("70000.00"), "Mecánica", 30),
                };
                
                autoRepository.saveAll(Arrays.asList(automoviles));
                
                System.out.println("¡Se han cargado autos de prueba en la base de datos!");
            }
        };
    }
}