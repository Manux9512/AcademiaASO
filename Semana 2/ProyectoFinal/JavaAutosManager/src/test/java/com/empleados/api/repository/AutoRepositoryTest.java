package com.empleados.api.repository;

import com.empleados.api.model.Auto;

import com.empleados.api.util.TestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para AutoRepository
 */
@DataJpaTest
@ActiveProfiles("test")
class AutoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AutoRepository autoRepository;

    @Test
    @DisplayName("Debe encontrar un auto por email cuando existe")
    void findByEmail_WhenEmailExists_ShouldReturnEmpleado() {
        // Arrange
        Auto auto = TestDataBuilder.createAuto();
        auto.setId(null); // El ID se asignará automáticamente
        entityManager.persist(auto);
        entityManager.flush();

        // Act
        Optional<Auto> found = autoRepository.findByEmail("juan.perez@example.com");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("Juan", found.get().getNombre());
        assertEquals("Pérez", found.get().getApellido());
    }

    @Test
    @DisplayName("Debe retornar Optional vacío cuando el email no existe")
    void findByEmail_WhenEmailDoesNotExist_ShouldReturnEmpty() {
        // Act
        Optional<Auto> found = autoRepository.findByEmail("noexiste@example.com");

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Debe encontrar empleados por departamento")
    void findByDepartamento_ShouldReturnEmpleadosInDepartamento() {
        // Arrange
        Auto auto1 = TestDataBuilder.createAuto();
        auto1.setId(null);
        
        Auto auto2 = TestDataBuilder.createAuto2();
        auto2.setId(null);
        
        Auto auto3 = new Auto(null, "Pedro", "Sánchez", "Nissan", "Sentra", "Negro",LocalDate.of(2020, 1, 15),
                "pedro.sanchez@example.com", LocalDate.of(2021, 3, 5), 
                new BigDecimal("52000.00"), "Tecnología",1);
        
        entityManager.persist(auto1);
        entityManager.persist(auto2);
        entityManager.persist(auto3);
        entityManager.flush();

        // Act
        List<Auto> tecnologiaAutomoviles = autoRepository.findByDepartamento("Tecnología");
        List<Auto> rrhhAutomoviles = autoRepository.findByDepartamento("Recursos Humanos");
        List<Auto> marketingAutomoviles = autoRepository.findByDepartamento("Marketing");

        // Assert
        assertEquals(2, tecnologiaAutomoviles.size());
        assertEquals(1, rrhhAutomoviles.size());
        assertEquals(0, marketingAutomoviles.size());
        
        assertTrue(tecnologiaAutomoviles.stream().allMatch(e -> "Tecnología".equals(e.getDepartamento())));
        assertTrue(rrhhAutomoviles.stream().allMatch(e -> "Recursos Humanos".equals(e.getDepartamento())));
    }

    @Test
    @DisplayName("Debe persistir correctamente un auto")
    void save_ShouldPersistEmpleado() {
        // Arrange
        Auto auto = TestDataBuilder.createAuto();
        auto.setId(null);

        // Act
        Auto saved = autoRepository.save(auto);
        
        // Assert
        assertNotNull(saved.getId());
        
        Auto found = entityManager.find(Auto.class, saved.getId());
        assertNotNull(found);
        assertEquals("Juan", found.getNombre());
        assertEquals("Pérez", found.getApellido());
        assertEquals("juan.perez@example.com", found.getEmail());
    }
    
    @Test
    @DisplayName("Debe actualizar correctamente un auto existente")
    void save_ShouldUpdateExistingEmpleado() {
        // Arrange
        Auto auto = TestDataBuilder.createAuto();
        auto.setId(null);
        auto = entityManager.persist(auto);
        entityManager.flush();
        
        // Act
        auto.setNombre("Juan Carlos");
        auto.setCosto(new BigDecimal("55000.00"));
        Auto updated = autoRepository.save(auto);
        entityManager.flush();
        
        // Assert
        Auto found = entityManager.find(Auto.class, updated.getId());
        assertEquals("Juan Carlos", found.getNombre());
        assertEquals(new BigDecimal("55000.00"), found.getCosto());
    }
    
    @Test
    @DisplayName("Debe eliminar correctamente un auto")
    void delete_ShouldRemoveEmpleado() {
        // Arrange
        Auto auto = TestDataBuilder.createAuto();
        auto.setId(null);
        auto = entityManager.persist(auto);
        entityManager.flush();
        Long id = auto.getId();
        
        // Act
        autoRepository.deleteById(id);
        entityManager.flush();
        
        // Assert
        Auto found = entityManager.find(Auto.class, id);
        assertNull(found);
    }
    
    @Test
    @DisplayName("Debe encontrar todos los empleados")
    void findAll_ShouldReturnAllEmpleados() {
        // Arrange
        Auto auto1 = TestDataBuilder.createAuto();
        auto1.setId(null);
        
        Auto auto2 = TestDataBuilder.createAuto2();
        auto2.setId(null);
        
        entityManager.persist(auto1);
        entityManager.persist(auto2);
        entityManager.flush();
        
        // Act
        List<Auto> empleados = autoRepository.findAll();
        
        // Assert
        assertEquals(2, empleados.size());
    }
    
    @Test
    @DisplayName("Debe verificar la existencia de un auto por ID")
    void existsById_ShouldReturnTrueForExistingId() {
        // Arrange
        Auto auto = TestDataBuilder.createAuto();
        auto.setId(null);
        auto = entityManager.persist(auto);
        entityManager.flush();
        
        // Act & Assert
        assertTrue(autoRepository.existsById(auto.getId()));
        assertFalse(autoRepository.existsById(999L));
    }
}