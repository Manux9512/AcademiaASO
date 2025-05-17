-- Script para limpiar datos de prueba de la base de datos

-- Eliminar todos los registros de la tabla empleados
DELETE FROM empleados;

-- Reiniciar secuencias (para H2)
ALTER TABLE empleados ALTER COLUMN id RESTART WITH 1;