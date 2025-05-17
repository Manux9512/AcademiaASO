package com.empleados.api.service;

import com.empleados.api.dto.AutoDTO;
import com.empleados.api.exception.ResourceNotFoundException;
import com.empleados.api.model.Auto;
import com.empleados.api.repository.AutoRepository;
import com.empleados.api.service.impl.AutoServiceImpl;
import com.empleados.api.util.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutoServiceTest {

    @Mock
    private AutoRepository autoRepository;

    @InjectMocks
    private AutoServiceImpl autoService;

    private Auto auto;
    private AutoDTO autoDTO;
    private Auto auto2;
    private AutoDTO autoDTO2;

    @BeforeEach
    void setUp() {
        // Setup test employee entities and DTOs
        auto = TestDataBuilder.createAuto();
        autoDTO = TestDataBuilder.createAutoDTO();
        auto2 = TestDataBuilder.createAuto2();
        autoDTO2 = TestDataBuilder.createAutoDTO2();
    }

    @Test
    @DisplayName("Debe retornar todos los automoviles")
    void getAllAutomoviles_ShouldReturnAllAutomoviles() {
        // Arrange
        List<Auto> automoviles = Arrays.asList(auto, auto2);
        when(autoRepository.findAll()).thenReturn(automoviles);

        // Act
        List<AutoDTO> result = autoService.getAllAutomoviles();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Juan", result.get(0).getNombre());
        assertEquals("Ana", result.get(1).getNombre());
        
        verify(autoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar un auto por ID")
    void getAutoById_WhenEmpleadoExists_ShouldReturnAuto() {
        // Arrange
        when(autoRepository.findById(1L)).thenReturn(Optional.of(auto));

        // Act
        AutoDTO result = autoService.getAutoById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Juan", result.getNombre());
        assertEquals("juan.perez@example.com", result.getEmail());
        
        verify(autoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el auto no existe")
    void getAutoById_WhenAutoDoesNotExist_ShouldThrowException() {
        // Arrange
        when(autoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
                () -> autoService.getAutoById(99L));
        
        assertTrue(exception.getMessage().contains("99"));
        verify(autoRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Debe crear un nuevo auto")
    void createAuto_WithValidData_ShouldReturnCreatedAuto() {
        // Arrange
        AutoDTO newAutoDTO = TestDataBuilder.createNewAutoDTO();
        Auto newAuto = new Auto(
                null,
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
        
        Auto savedAuto = new Auto(
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
        
        when(autoRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(autoRepository.save(any(Auto.class))).thenReturn(savedAuto);

        // Act
        AutoDTO result = autoService.createAuto(newAutoDTO);

        // Assert
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("Carlos", result.getNombre());
        assertEquals("Rodríguez", result.getApellido());
        assertEquals("carlos.rodriguez@example.com", result.getEmail());
        
        verify(autoRepository, times(1)).findByEmail(anyString());
        verify(autoRepository, times(1)).save(any(Auto.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al crear un auto con email duplicado")
    void createAuto_WithDuplicateEmail_ShouldThrowException() {
        // Arrange
        when(autoRepository.findByEmail("juan.perez@example.com")).thenReturn(Optional.of(auto));

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, 
                () -> autoService.createAuto(autoDTO));
        
        verify(autoRepository, times(1)).findByEmail("juan.perez@example.com");
        verify(autoRepository, never()).save(any(Auto.class));
    }

    @Test
    @DisplayName("Debe actualizar un auto existente")
    void updateAuto_WhenAutoExists_ShouldReturnUpdatedAuto() {
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

        Auto updatedAuto = new Auto(
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

        when(autoRepository.findById(1L)).thenReturn(Optional.of(auto));
        when(autoRepository.save(any(Auto.class))).thenReturn(updatedAuto);

        // Act
        AutoDTO result = autoService.updateAuto(1L, updatedDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Juan Carlos", result.getNombre());
        assertEquals(new BigDecimal("55000.00"), result.getCosto());
        
        verify(autoRepository, times(1)).findById(1L);
        verify(autoRepository, times(1)).save(any(Auto.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar un auto inexistente")
    void updateAuto_WhenAutoDoesNotExist_ShouldThrowException() {
        // Arrange
        when(autoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
                () -> autoService.updateAuto(99L, autoDTO));
        
        assertTrue(exception.getMessage().contains("99"));
        verify(autoRepository, times(1)).findById(99L);
        verify(autoRepository, never()).save(any(Auto.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar con email que ya pertenece a otro auto")
    void updateAuto_WithDuplicateEmail_ShouldThrowException() {
        // Arrange
        AutoDTO updatedDTO = new AutoDTO(
                1L,
                "Juan Carlos",
                "Pérez",
                "Ford",
                "Mustang",
                "Negro",
                LocalDate.of(2020, 1, 15),
                "ana.garcia@example.com", // Email de otro auto existente
                LocalDate.of(2020, 1, 15),
                new BigDecimal("55000.00"),
                "Tecnología",
                1
        );

        when(autoRepository.findById(1L)).thenReturn(Optional.of(auto));
        when(autoRepository.findByEmail("ana.garcia@example.com")).thenReturn(Optional.of(auto2));

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, 
                () -> autoService.updateAuto(1L, updatedDTO));
        
        verify(autoRepository, times(1)).findById(1L);
        verify(autoRepository, times(1)).findByEmail("ana.garcia@example.com");
        verify(autoRepository, never()).save(any(Auto.class));
    }

    @Test
    @DisplayName("Debe eliminar un auto existente")
    void deleteAuto_WhenEmpleadoExists_ShouldDeleteAuto() {
        // Arrange
        when(autoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(autoRepository).deleteById(1L);

        // Act
        autoService.deleteAuto(1L);

        // Assert
        verify(autoRepository, times(1)).existsById(1L);
        verify(autoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar un auto inexistente")
    void deleteAuto_WhenAutoDoesNotExist_ShouldThrowException() {
        // Arrange
        when(autoRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
                () -> autoService.deleteAuto(99L));
        
        assertTrue(exception.getMessage().contains("99"));
        verify(autoRepository, times(1)).existsById(99L);
        verify(autoRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Debe retornar automoviles por departamento")
    void getAutomovilesByDepartamento_ShouldReturnAutomovilesInDepartamento() {
        // Arrange
        List<Auto> tecnologiaAutomoviles = Arrays.asList(auto);
        
        when(autoRepository.findByDepartamento("Tecnología")).thenReturn(tecnologiaAutomoviles);

        // Act
        List<AutoDTO> result = autoService.getAutomovilesByDepartamento("Tecnología");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Tecnología", result.get(0).getDepartamento());
        assertEquals("Juan", result.get(0).getNombre());
        
        verify(autoRepository, times(1)).findByDepartamento("Tecnología");
    }
    
    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay automoviles en el departamento")
    void getAutomovilesByDepartamento_WhenNoAutomoviles_ShouldReturnEmptyList() {
        // Arrange
        when(autoRepository.findByDepartamento("Marketing")).thenReturn(Arrays.asList());

        // Act
        List<AutoDTO> result = autoService.getAutomovilesByDepartamento("Marketing");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(autoRepository, times(1)).findByDepartamento("Marketing");
    }
    
    @Test
    @DisplayName("Debe convertir correctamente entidad a DTO")
    void convertToDTO_ShouldMapAllProperties() {
        // Arrange
        Auto testAuto = new Auto(
                5L,
                "Test",
                "User",
                "Brand",
                "Model",
                "Color",
                LocalDate.of(2020, 1, 15),
                "test.user@example.com",
                LocalDate.of(2022, 5, 15),
                new BigDecimal("60000.00"),
                "IT",
                1
        );
        
        // Primero configuramos el mock
        when(autoRepository.findById(5L)).thenReturn(Optional.of(testAuto));
        
        // Act - Llamar al método privado convertToDTO a través de un método público
        AutoDTO dto = autoService.getAutoById(5L);
        
        // Assert
        assertEquals(5L, dto.getId());
        assertEquals("Test", dto.getNombre());
        assertEquals("User", dto.getApellido());
        assertEquals("test.user@example.com", dto.getEmail());
        assertEquals(LocalDate.of(2022, 5, 15), dto.getFechaContratacion());
        assertEquals(new BigDecimal("60000.00"), dto.getCosto());
        assertEquals("IT", dto.getDepartamento());
    }
}