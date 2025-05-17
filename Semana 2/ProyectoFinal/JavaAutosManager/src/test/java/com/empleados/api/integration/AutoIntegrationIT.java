package com.empleados.api.integration;

import com.empleados.api.dto.AutoDTO;
import com.empleados.api.model.Auto;
import com.empleados.api.repository.AutoRepository;
import com.empleados.api.util.TestDataBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración para Auto
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AutoIntegrationIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AutoRepository empleadoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        // Limpiar la base de datos antes de cada prueba
        empleadoRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        // Limpiar la base de datos después de cada prueba
        empleadoRepository.deleteAll();
    }

    @Test
    @DisplayName("Debe integrarse correctamente al crear, actualizar y eliminar un empleado")
    void crudOperations_ShouldWorkEndToEnd() throws Exception {
        // Primero, verificamos que la base de datos está vacía
        mockMvc.perform(get("/api/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        // 1. Crear un nuevo empleado
        AutoDTO newEmpleadoDTO = TestDataBuilder.createNewAutoDTO();
        String newEmpleadoJson = objectMapper.writeValueAsString(newEmpleadoDTO);

        String createdEmpleadoJson = mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newEmpleadoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nombre", is("Carlos")))
                .andExpect(jsonPath("$.email", is("carlos.rodriguez@example.com")))
                .andReturn().getResponse().getContentAsString();

        AutoDTO createdEmpleadoDTO = objectMapper.readValue(createdEmpleadoJson, AutoDTO.class);
        Long empleadoId = createdEmpleadoDTO.getId();

        // 2. Verificar que el empleado fue creado en la base de datos
        mockMvc.perform(get("/api/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(empleadoId.intValue())));

        // 3. Obtener empleado por ID
        mockMvc.perform(get("/api/empleados/{id}", empleadoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(empleadoId.intValue())))
                .andExpect(jsonPath("$.nombre", is("Carlos")))
                .andExpect(jsonPath("$.email", is("carlos.rodriguez@example.com")));

        // 4. Actualizar empleado
        createdEmpleadoDTO.setNombre("Carlos Alberto");
        createdEmpleadoDTO.setCosto(new BigDecimal("60000.00"));

        mockMvc.perform(put("/api/empleados/{id}", empleadoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createdEmpleadoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Carlos Alberto")))
                .andExpect(jsonPath("$.salario", is(60000.00)));

        // 5. Verificar la actualización en la base de datos
        mockMvc.perform(get("/api/empleados/{id}", empleadoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Carlos Alberto")))
                .andExpect(jsonPath("$.salario", is(60000.00)));

        // 6. Eliminar empleado
        mockMvc.perform(delete("/api/empleados/{id}", empleadoId))
                .andExpect(status().isNoContent());

        // 7. Verificar que el empleado fue eliminado
        mockMvc.perform(get("/api/empleados/{id}", empleadoId))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Debe filtrar empleados por departamento")
    void getEmpleadosByDepartamento_ShouldFilterCorrectly() throws Exception {
        // Preparar datos de prueba
        Auto empleado1 = TestDataBuilder.createAuto();
        empleado1.setId(null);
        
        Auto empleado2 = TestDataBuilder.createAuto2();
        empleado2.setId(null);
        
        Auto empleado3 = new Auto(
                null, 
                "Pedro", 
                "Sánchez", 
                "Nissan",
                "Sentra",
                "Negro",
                LocalDate.of(2020, 1, 15),
                "pedro.sanchez@example.com", 
                LocalDate.of(2021, 3, 5), 
                new BigDecimal("52000.00"), 
                "Tecnología",
                1
        );
        
        empleadoRepository.save(empleado1);
        empleadoRepository.save(empleado2);
        empleadoRepository.save(empleado3);

        // Buscar empleados por departamento Tecnología
        mockMvc.perform(get("/api/empleados/departamento/{departamento}", "Tecnología"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].departamento", is("Tecnología")))
                .andExpect(jsonPath("$[1].departamento", is("Tecnología")));

        // Buscar empleados por departamento Recursos Humanos
        mockMvc.perform(get("/api/empleados/departamento/{departamento}", "Recursos Humanos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].departamento", is("Recursos Humanos")));

        // Buscar empleados por departamento que no existe
        mockMvc.perform(get("/api/empleados/departamento/{departamento}", "Marketing"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Debe validar las restricciones al crear un empleado")
    void createEmpleado_WithInvalidData_ShouldValidateConstraints() throws Exception {
        // Auto con email duplicado
        Auto empleado = TestDataBuilder.createAuto();
        empleado.setId(null);
        empleadoRepository.save(empleado);

        AutoDTO duplicateEmailDTO = new AutoDTO(
                null,
                "Otro",
                "Auto",
                "Ford",
                "Mustang",
                "Negro",
                LocalDate.now(),
                "juan.perez@example.com", // Email duplicado
                LocalDate.now(),
                new BigDecimal("45000.00"),
                "Marketing",
                1
        );

        mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateEmailDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", containsString("Ya existe un empleado")));

        // Auto con datos inválidos
        AutoDTO invalidDTO = new AutoDTO(
                null,
                "", // Nombre vacío
                "", // Apellido vacío,
                "",
                "",
                "",
                LocalDate.now().plusDays(1), // Fecha futura
                "invalid-email", // Email inválido
                LocalDate.now().plusDays(1), // Fecha futura
                new BigDecimal("-1000.00"), // Salario negativo
                null,
                1
        );

        mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debe manejar correctamente la excepción de recurso no encontrado")
    void getNonExistingEmpleado_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/empleados/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("999")));
                
        mockMvc.perform(put("/api/empleados/{id}", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestDataBuilder.createAutoDTO())))
                .andExpect(status().isNotFound());
                
        mockMvc.perform(delete("/api/empleados/{id}", 999))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("Debe probar el rendimiento de las operaciones básicas")
    void performanceTest_ShouldCompleteInReasonableTime() throws Exception {
        // Crear múltiples empleados para la prueba
        for (int i = 0; i < 10; i++) {
            Auto empleado = new Auto(
                    null,
                    "Nombre" + i,
                    "Apellido" + i,
                    "Marca" + i,
                    "Modelo" + i,
                    "Color" + i,
                    LocalDate.now().minusYears(1),
                    "email" + i + "@example.com",
                    LocalDate.now().minusYears(1),
                    new BigDecimal("50000.00"),
                    "Departamento" + (i % 3),
                    1
            );
            empleadoRepository.save(empleado);
        }
        
        // Medir tiempo de respuesta para obtener todos los empleados
        long startTime = System.currentTimeMillis();
        mockMvc.perform(get("/api/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)));
        long endTime = System.currentTimeMillis();
        
        // La operación debería completarse en menos de 1 segundo
        long executionTime = endTime - startTime;
        
        // Verificar rendimiento
        assertEquals(10, empleadoRepository.count());
        assertTrue(executionTime < 1000, "La operación tardó demasiado: " + executionTime + "ms");
    }
    
    // Método auxiliar para verificar si la respuesta se completa en un tiempo razonable
    private void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}