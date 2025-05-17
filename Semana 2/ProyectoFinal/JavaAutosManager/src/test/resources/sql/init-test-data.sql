-- Script para inicializar datos de prueba en la base de datos

-- Limpiar tablas existentes
DELETE FROM empleados;

-- Reiniciar secuencias (para H2)
ALTER TABLE empleados ALTER COLUMN id RESTART WITH 1;

-- Insertar datos de prueba para empleados
INSERT INTO empleados (nombre, apellido, email, fecha_contratacion, salario, departamento) 
VALUES ('Juan', 'Pérez', 'juan.perez@example.com', '2020-01-15', 50000.00, 'Tecnología');

INSERT INTO empleados (nombre, apellido, email, fecha_contratacion, salario, departamento) 
VALUES ('Ana', 'García', 'ana.garcia@example.com', '2019-05-10', 48000.00, 'Recursos Humanos');

INSERT INTO empleados (nombre, apellido, email, fecha_contratacion, salario, departamento) 
VALUES ('Carlos', 'Rodríguez', 'carlos.rodriguez@example.com', '2021-03-22', 52000.00, 'Tecnología');

INSERT INTO empleados (nombre, apellido, email, fecha_contratacion, salario, departamento) 
VALUES ('María', 'López', 'maria.lopez@example.com', '2018-11-05', 55000.00, 'Finanzas');

INSERT INTO empleados (nombre, apellido, email, fecha_contratacion, salario, departamento) 
VALUES ('Pedro', 'Martínez', 'pedro.martinez@example.com', '2022-02-14', 45000.00, 'Marketing');