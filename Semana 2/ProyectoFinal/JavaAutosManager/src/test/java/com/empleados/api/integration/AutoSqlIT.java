package com.empleados.api.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.empleados.api.model.Auto;
import com.empleados.api.repository.AutoRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración con scripts SQL
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class AutoSqlIT {

    @Autowired
    private AutoRepository empleadoRepository;

    @Test
    @DisplayName("Debe cargar los datos desde scripts SQL")
    @Sql(scripts = {"/sql/init-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/sql/clean-test-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void loadDataFromSqlScript_ShouldPopulateRepository() {
        // Verificar que se cargaron los datos
        List<Auto> empleados = empleadoRepository.findAll();
        assertEquals(5, empleados.size());
        
        // Verificar empleados específicos
        Optional<Auto> juan = empleadoRepository.findByEmail("juan.perez@example.com");
        assertTrue(juan.isPresent());
        assertEquals("Juan", juan.get().getNombre());
        assertEquals("Tecnología", juan.get().getDepartamento());
        
        Optional<Auto> ana = empleadoRepository.findByEmail("ana.garcia@example.com");
        assertTrue(ana.isPresent());
        assertEquals("Ana", ana.get().getNombre());
        assertEquals("Recursos Humanos", ana.get().getDepartamento());
    }

    @Test
    @DisplayName("Debe filtrar empleados por departamento desde datos cargados por SQL")
    @Sql(scripts = {"/sql/init-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/sql/clean-test-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByDepartamento_WithSqlData_ShouldFilterCorrectly() {
        // Verificar filtro por departamento Tecnología
        List<Auto> tecnologia = empleadoRepository.findByDepartamento("Tecnología");
        assertEquals(2, tecnologia.size());
        
        // Verificar filtro por departamento Recursos Humanos
        List<Auto> rrhh = empleadoRepository.findByDepartamento("Recursos Humanos");
        assertEquals(1, rrhh.size());
        
        // Verificar filtro por departamento Finanzas
        List<Auto> finanzas = empleadoRepository.findByDepartamento("Finanzas");
        assertEquals(1, finanzas.size());
        
        // Verificar filtro por departamento Marketing
        List<Auto> marketing = empleadoRepository.findByDepartamento("Marketing");
        assertEquals(1, marketing.size());
        
        // Verificar filtro por departamento que no existe
        List<Auto> otherDepartment = empleadoRepository.findByDepartamento("Logística");
        assertTrue(otherDepartment.isEmpty());
    }

    @Test
    @DisplayName("Debe verificar restricciones de unicidad en email con datos SQL")
    @Sql(scripts = {"/sql/init-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/sql/clean-test-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void emailUniqueness_WithSqlData_ShouldBeEnforced() {
        // Crear empleado con email duplicado
        Auto duplicateEmployee = new Auto();
        duplicateEmployee.setNombre("Otro");
        duplicateEmployee.setApellido("Usuario");
        duplicateEmployee.setEmail("juan.perez@example.com"); // Email ya existente
        
        // Verificar que se lanza excepción al intentar guardar
        assertThrows(Exception.class, () -> {
            empleadoRepository.save(duplicateEmployee);
            empleadoRepository.flush(); // Forzar la ejecución para que se lance la excepción
        });
    }
}