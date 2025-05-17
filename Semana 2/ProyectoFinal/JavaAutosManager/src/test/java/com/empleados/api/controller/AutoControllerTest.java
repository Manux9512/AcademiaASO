package com.empleados.api.controller;

import com.empleados.api.dto.AutoDTO;
import com.empleados.api.exception.ResourceNotFoundException;
import com.empleados.api.service.AutoService;
import com.empleados.api.util.TestDataBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AutoController.class)
class AutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AutoService autoService;

    private ObjectMapper objectMapper;
    private AutoDTO autoDTO;
    private AutoDTO empleadoDTO2;
    private List<AutoDTO> autoDTOList;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        autoDTO = TestDataBuilder.createAutoDTO();
        empleadoDTO2 = TestDataBuilder.createAutoDTO2();
        autoDTOList = TestDataBuilder.createAutoDTOList();
    }

    @Test
    @DisplayName("Debe retornar todos los automoviles")
    void getAllAutomoviles_ShouldReturnListOfAutomoviles() throws Exception {
        // Arrange
        when(autoService.getAllAutomoviles()).thenReturn(autoDTOList);

        // Act & Assert
        mockMvc.perform(get("/api/automoviles")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("Juan")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nombre", is("Ana")));

        verify(autoService, times(1)).getAllAutomoviles();
    }
    
    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay automoviles")
    void getAllEmpleados_WhenNoEmpleados_ShouldReturnEmptyList() throws Exception {
        // Arrange
        when(autoService.getAllAutomoviles()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/automoviles")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(autoService, times(1)).getAllAutomoviles();
    }

    @Test
    @DisplayName("Debe retornar un auto por ID")
    void getAutoById_WhenAutoExists_ShouldReturnAuto() throws Exception {
        // Arrange
        when(autoService.getAutoById(1L)).thenReturn(autoDTO);

        // Act & Assert
        mockMvc.perform(get("/api/automoviles/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Juan")))
                .andExpect(jsonPath("$.apellido", is("Pérez")))
                .andExpect(jsonPath("$.email", is("juan.perez@example.com")));

        verify(autoService, times(1)).getAutoById(1L);
    }

    @Test
    @DisplayName("Debe retornar 404 cuando el auto no existe")
    void getAutoById_WhenAutoDoesNotExist_ShouldReturn404() throws Exception {
        // Arrange
        when(autoService.getAutoById(99L)).thenThrow(new ResourceNotFoundException("Empleado no encontrado con id: 99"));

        // Act & Assert
        mockMvc.perform(get("/api/automoviles/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("99")));

        verify(autoService, times(1)).getAutoById(99L);
    }

    @Test
    @DisplayName("Debe crear un nuevo auto")
    void createAuto_WithValidData_ShouldReturnCreatedAuto() throws Exception {
        // Arrange
        AutoDTO newAutoDTO = TestDataBuilder.createNewAutoDTO();
        AutoDTO createdAutoDTO = new AutoDTO(
                3L,
                newAutoDTO.getNombre(),
                newAutoDTO.getApellido(),
                newAutoDTO.getMarca(),
                newAutoDTO.getModelo(),
                newAutoDTO.getColor(),
                newAutoDTO.getFechaCompra(),
                newAutoDTO.getEmail(),
                newAutoDTO.getFechaContratacion(),
                newAutoDTO.getCosto(),
                newAutoDTO.getDepartamento(),
                newAutoDTO.getAntiguedad()

        );
        
        when(autoService.createAuto(any(AutoDTO.class))).thenReturn(createdAutoDTO);

        // Act & Assert
        mockMvc.perform(post("/api/automoviles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newAutoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.nombre", is("Carlos")))
                .andExpect(jsonPath("$.apellido", is("Rodríguez")));

        verify(autoService, times(1)).createAuto(any(AutoDTO.class));
    }

    @Test
    @DisplayName("Debe retornar 400 al crear con datos inválidos")
    void createAuto_WithInvalidData_ShouldReturn400() throws Exception {
        // Arrange
        AutoDTO invalidAutoDTO = TestDataBuilder.createInvalidAutoDTO();

        // Act & Assert
        mockMvc.perform(post("/api/automoviles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidAutoDTO)))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

        verify(autoService, never()).createAuto(any(AutoDTO.class));
    }
    
    @Test
    @DisplayName("Debe retornar 409 al crear con email duplicado")
    void createAutomoviles_WithDuplicateEmail_ShouldReturn409() throws Exception {
        // Arrange
        when(autoService.createAuto(any(AutoDTO.class)))
                .thenThrow(new DataIntegrityViolationException("Ya existe un auto con el email: juan.perez@example.com"));

        // Act & Assert
        mockMvc.perform(post("/api/automoviles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(autoDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", containsString("Ya existe un auto")));

        verify(autoService, times(1)).createAuto(any(AutoDTO.class));
    }

    @Test
    @DisplayName("Debe actualizar un auto existente")
    void updateAutomoviles_WithValidData_ShouldReturnUpdatedAutomoviles() throws Exception {
        // Arrange
        AutoDTO updatedDTO = new AutoDTO(
                1L,
                "Juan Carlos",
                "Pérez",
                "Ford",
                "Mustang",
                "Negro",
                LocalDate.of(2020, 1, 15),
                "juan.perez@example.com",
                LocalDate.of(2020, 1, 15),
                new BigDecimal("55000.00"),
                "Tecnología",
                1
        );

        when(autoService.updateAuto(eq(1L), any(AutoDTO.class))).thenReturn(updatedDTO);

        // Act & Assert
        mockMvc.perform(put("/api/automoviles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Juan Carlos")))
                .andExpect(jsonPath("$.costo", is(55000.00)));

        verify(autoService, times(1)).updateAuto(eq(1L), any(AutoDTO.class));
    }
    
    @Test
    @DisplayName("Debe retornar 404 al actualizar un auto inexistente")
    void updateAutomoviles_WhenAutomovilesDoesNotExist_ShouldReturn404() throws Exception {
        // Arrange
        when(autoService.updateAuto(eq(99L), any(AutoDTO.class)))
                .thenThrow(new ResourceNotFoundException("Empleado no encontrado con id: 99"));

        // Act & Assert
        mockMvc.perform(put("/api/automoviles/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(autoDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("99")));

        verify(autoService, times(1)).updateAuto(eq(99L), any(AutoDTO.class));
    }
    
    @Test
    @DisplayName("Debe retornar 409 al actualizar con email duplicado")
    void updateAutomoviles_WithDuplicateEmail_ShouldReturn409() throws Exception {
        // Arrange
        AutoDTO updatedDTO = new AutoDTO(
                1L,
                "Juan Carlos",
                "Pérez",
                "Ford",
                "Mustang",
                "Negro",
                LocalDate.of(2020, 1, 15),
                "ana.garcia@example.com",
                LocalDate.of(2020, 1, 15),
                new BigDecimal("55000.00"),
                "Tecnología",
                1
        );

        when(autoService.updateAuto(eq(1L), any(AutoDTO.class)))
                .thenThrow(new DataIntegrityViolationException("Ya existe un auto con el email: ana.garcia@example.com"));

        // Act & Assert
        mockMvc.perform(put("/api/automoviles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", containsString("Ya existe un auto")));

        verify(autoService, times(1)).updateAuto(eq(1L), any(AutoDTO.class));
    }

    @Test
    @DisplayName("Debe eliminar un auto")
    void deleteAutomoviles_WhenAutomovilesExists_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(autoService).deleteAuto(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/automoviles/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(autoService, times(1)).deleteAuto(1L);
    }
    
    @Test
    @DisplayName("Debe retornar 404 al eliminar un auto inexistente")
    void deleteAutomoviles_WhenAutomovilesDoesNotExist_ShouldReturn404() throws Exception {
        // Arrange
        doThrow(new ResourceNotFoundException("Empleado no encontrado con id: 99"))
                .when(autoService).deleteAuto(99L);

        // Act & Assert
        mockMvc.perform(delete("/api/automoviles/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("99")));

        verify(autoService, times(1)).deleteAuto(99L);
    }

    @Test
    @DisplayName("Debe retornar automoviles por departamento")
    void getAutomovilesByDepartamento_ShouldReturnListOfEmpleados() throws Exception {
        // Arrange
        List<AutoDTO> tecnologiaEmpleados = TestDataBuilder.createAutomovilesDTOByDepartamento("Tecnología");
        
        when(autoService.getAutomovilesByDepartamento("Tecnología")).thenReturn(tecnologiaEmpleados);

        // Act & Assert
        mockMvc.perform(get("/api/automoviles/departamento/Tecnología")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].departamento", is("Tecnología")));

        verify(autoService, times(1)).getAutomovilesByDepartamento("Tecnología");
    }
    
    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay automoviles en el departamento")
    void getAutomovilesByDepartamento_WhenNoAutomoviles_ShouldReturnEmptyList() throws Exception {
        // Arrange
        when(autoService.getAutomovilesByDepartamento("Marketing")).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/automoviles/departamento/Marketing")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(autoService, times(1)).getAutomovilesByDepartamento("Marketing");
    }
}