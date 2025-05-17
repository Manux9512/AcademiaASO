package com.empleados.api.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;



import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AutoDTOTest {

    private AutoDTO empleadoDTO1;
    private AutoDTO empleadoDTO2;
    private AutoDTO empleadoDTO3; // copia idéntica a empleadoDTO1
    private AutoDTO empleadoDTONull;

    @BeforeEach
    void setUp() {
        // Primer objeto
        empleadoDTO1 = new AutoDTO(
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

        // Segundo objeto con valores diferentes
        empleadoDTO2 = new AutoDTO(
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

        // Tercer objeto idéntico al primero para pruebas de equals y hashCode
        empleadoDTO3 = new AutoDTO(
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

        // Objeto nulo para pruebas de equals
        empleadoDTONull = null;
    }

    @Test
    @DisplayName("Test de equals con mismo objeto")
    void testEqualsSameObject() {
        // El objeto debe ser igual a sí mismo
        assertTrue(empleadoDTO1.equals(empleadoDTO1));
    }

    @Test
    @DisplayName("Test de equals con objetos iguales")
    void testEqualsEqualObjects() {
        // Dos objetos con los mismos valores deben ser considerados iguales
        assertTrue(empleadoDTO1.equals(empleadoDTO3));
        assertTrue(empleadoDTO3.equals(empleadoDTO1));
    }

    @Test
    @DisplayName("Test de equals con objetos diferentes")
    void testEqualsDifferentObjects() {
        // Dos objetos con valores diferentes no deben ser iguales
        assertFalse(empleadoDTO1.equals(empleadoDTO2));
        assertFalse(empleadoDTO2.equals(empleadoDTO1));
    }

    @Test
    @DisplayName("Test de equals con null")
    void testEqualsWithNull() {
        // La comparación con null debe retornar false
        assertFalse(empleadoDTO1.equals(empleadoDTONull));
    }

    @Test
    @DisplayName("Test de equals con otra clase")
    void testEqualsWithDifferentClass() {
        // La comparación con un objeto de otra clase debe retornar false
        assertFalse(empleadoDTO1.equals(new Object()));
    }

    @Test
    @DisplayName("Test de hashCode para objetos iguales")
    void testHashCodeEqualObjects() {
        // Dos objetos iguales deben tener el mismo hashCode
        assertEquals(empleadoDTO1.hashCode(), empleadoDTO3.hashCode());
    }

    @Test
    @DisplayName("Test de hashCode para objetos diferentes")
    void testHashCodeDifferentObjects() {
        // Dos objetos diferentes probablemente tendrán hashCodes diferentes
        // (aunque no es garantizado por el contrato de hashCode, es una buena práctica)
        assertNotEquals(empleadoDTO1.hashCode(), empleadoDTO2.hashCode());
    }

    @Test
    @DisplayName("Test del método toString")
    void testToString() {
        // Verificar que toString contiene la información relevante
        String toStringResult = empleadoDTO1.toString();
        assertTrue(toStringResult.contains("id=1"));
        assertTrue(toStringResult.contains("nombre=Juan"));
        assertTrue(toStringResult.contains("apellido=Pérez"));
        assertTrue(toStringResult.contains("email=juan.perez@example.com"));
        assertTrue(toStringResult.contains("2020-01-15")); // Fecha de contratación
        assertTrue(toStringResult.contains("50000")); // Parte del salario
        assertTrue(toStringResult.contains("departamento=Tecnología"));
    }

    @Test
    @DisplayName("Test del método canEqual")
    void testCanEqual() {
        // El método canEqual debe retornar true para objetos de la misma clase
        assertTrue(empleadoDTO1.canEqual(empleadoDTO2));
        
        // y false para objetos de diferente clase
        assertFalse(empleadoDTO1.canEqual(new Object()));
    }

    @Test
    @DisplayName("Test de equals con campos específicos modificados")
    void testEqualsWithModifiedFields() {
        // Crear una copia y modificar campos para ver qué afecta a equals
        AutoDTO modified = new AutoDTO(
                1L, // mismo ID
                "Juan Modificado", // nombre distinto
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
        
        // Debería ser distinto porque el nombre cambió
        assertFalse(empleadoDTO1.equals(modified));
        
        // Ahora modificamos solo el salario
        modified = new AutoDTO(
                1L,
                "Juan",
                "Pérez",
                "Nissan",
                "Sentra",
                "Negro",
                LocalDate.of(2020, 1, 15),
                "juan.perez@example.com",
                LocalDate.of(2020, 1, 15),
                new BigDecimal("60000.00"), // salario distinto
                "Tecnología",
                1
        );
        
        // Debería ser distinto porque el salario cambió
        assertFalse(empleadoDTO1.equals(modified));
    }
}