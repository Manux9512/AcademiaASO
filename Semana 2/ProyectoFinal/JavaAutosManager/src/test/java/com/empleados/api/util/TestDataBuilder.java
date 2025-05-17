package com.empleados.api.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.empleados.api.dto.AutoDTO;
import com.empleados.api.model.Auto;

/**
 * Clase utilitaria para generar datos de prueba
 */
public class TestDataBuilder {

    /**
     * Crea un objeto Auto para pruebas
     */
    public static Auto createAuto() {
        return new Auto(
                1L,
                "Juan",
                "Pérez",
                "Nissan",
                "Sentra",
                "Negro",
                LocalDate.of(2020, 1, 15),
                "juan.perez@example.com",
                LocalDate.of(2020, 1, 15),
                new BigDecimal("50000.00"),
                "Tecnología",
                1
        );
    }

    /**
     * Crea un objeto AutoDTO para pruebas
     */
    public static AutoDTO createAutoDTO() {
        return new AutoDTO(
                1L,
                "Juan",
                "Pérez",
                "Nissan",
                "Sentra",
                "Negro",
                LocalDate.of(2020, 1, 15),
                "juan.perez@example.com",
                LocalDate.of(2020, 1, 15),
                new BigDecimal("50000.00"),
                "Tecnología",
                1
        );
    }

    /**
     * Crea un segundo Auto con datos diferentes para pruebas
     */
    public static Auto createAuto2() {
        return new Auto(
                2L,
                "Ana",
                "García",
                "Ford",
                "Mustang",
                "Negro",
                LocalDate.of(2020, 1, 15),
                "ana.garcia@example.com",
                LocalDate.of(2019, 5, 10),
                new BigDecimal("48000.00"),
                "Recursos Humanos",
                2
        );
    }

    /**
     * Crea un segundo AutoDTO con datos diferentes para pruebas
     */
    public static AutoDTO createAutoDTO2() {
        return new AutoDTO(
                2L,
                "Ana",
                "García",
                "Ford",
                "Mustang",
                "Negro",
                LocalDate.of(2020, 1, 15),
                "ana.garcia@example.com",
                LocalDate.of(2019, 5, 10),
                new BigDecimal("48000.00"),
                "Recursos Humanos"
                ,2
        );
    }

    /**
     * Crea un nuevo AutoDTO sin ID (para creación)
     */
    public static AutoDTO createNewAutoDTO() {
        return new AutoDTO(
                null,
                "Carlos",
                "Rodríguez",
                "Honda",
                "Civic",
                "Azul",
                LocalDate.of(2020, 1, 15),
                "carlos.rodriguez@example.com",
                LocalDate.of(2021, 3, 22),
                new BigDecimal("52000.00"),
                "Tecnología",
                1
        );
    }

    /**
     * Crea un AutoDTO con datos inválidos para pruebas
     */
    public static AutoDTO createInvalidAutoDTO() {
        return new AutoDTO(
                null,
                "", // Nombre vacío (inválido)
                "", // Apellido vacío (inválido)
                "", // Marca vacía (inválida)
                "", // Modelo vacío (inválido)
                "", // Color vacío (inválido)
                LocalDate.now().plusDays(1), // Fecha futura (inválida)
                "email-invalido", // Email inválido
                LocalDate.now().plusDays(1), // Fecha futura (inválida)
                new BigDecimal("-1000.00"), // Salario negativo
                "",
                0
        );
    }

    /**
     * Crea una lista de AutoDTO para pruebas
     */
    public static List<AutoDTO> createAutoDTOList() {
        List<AutoDTO> automoviles = new ArrayList<>();
        automoviles.add(createAutoDTO());
        automoviles.add(createAutoDTO2());
        return automoviles;
    }

    /**
     * Crea una lista de Auto para pruebas
     */
    public static List<Auto> createAutoList() {
        List<Auto> automoviles = new ArrayList<>();
        automoviles.add(createAuto());
        automoviles.add(createAuto2());
        return automoviles;
    }

    /**
     * Crea una lista de Empleados por departamento
     */
    public static List<Auto> createAutomovilesByDepartamento(String departamento) {
        List<Auto> automoviles = new ArrayList<>();
        
        if ("Tecnología".equals(departamento)) {
            automoviles.add(createAuto());
        } else if ("Recursos Humanos".equals(departamento)) {
            automoviles.add(createAuto2());
        }
        
        return automoviles;
    }

    /**
     * Crea una lista de EmpleadosDTO por departamento
     */
    public static List<AutoDTO> createAutomovilesDTOByDepartamento(String departamento) {
        List<AutoDTO> automoviles = new ArrayList<>();
        
        if ("Tecnología".equals(departamento)) {
            automoviles.add(createAutoDTO());
        } else if ("Recursos Humanos".equals(departamento)) {
            automoviles.add(createAutoDTO2());
        }
        
        return automoviles;
    }
}