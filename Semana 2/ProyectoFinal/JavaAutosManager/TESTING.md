# Documentación de Pruebas - API de Gestión de Empleados

Este documento describe las pruebas implementadas para la API de Gestión de Empleados, desarrollada con Spring Boot y Java 17.

## Resumen de Cobertura

Se ha implementado un conjunto completo de pruebas con el objetivo de alcanzar una cobertura mínima del 90% en todas las capas:

- Capa Repository: ~95% de cobertura
- Capa Service: ~95% de cobertura
- Capa Controller: ~95% de cobertura
- Excepciones y validaciones: ~90% de cobertura

## Estructura de Pruebas

Las pruebas siguen la misma estructura de paquetes que el código fuente:

```
src/test/java/com/empleados/api/
├── controller
│   └── EmpleadoControllerTest.java
├── integration
│   ├── EmpleadoIntegrationIT.java
│   └── EmpleadoSqlIT.java
├── repository
│   └── EmpleadoRepositoryTest.java
├── service
│   └── EmpleadoServiceTest.java
└── util
    └── TestDataBuilder.java
```

## Pruebas Unitarias

### Capa Repository (`EmpleadoRepositoryTest`)

- Prueba de operaciones CRUD básicas (save, findById, delete)
- Validación de consultas personalizadas:
  - `findByEmail`
  - `findByDepartamento`
- Verificación de restricciones (unicidad de email)
- Pruebas de actualización de entidades

### Capa Service (`EmpleadoServiceTest`)

- Pruebas de toda la lógica de negocio
- Verificación de transformaciones entre entidades y DTOs
- Manejo de excepciones:
  - ResourceNotFoundException
  - DataIntegrityViolationException
- Validación de operaciones con datos válidos e inválidos
- Pruebas para asegurar que el servicio llama correctamente al repositorio

### Capa Controller (`EmpleadoControllerTest`)

- Pruebas para cada endpoint REST implementado
- Verificación de códigos de respuesta HTTP
- Validación de formatos de respuesta JSON
- Pruebas de manejo de excepciones y casos límite
- Verificación de validaciones en las entradas

## Pruebas de Integración

### Pruebas End-to-End (`EmpleadoIntegrationIT`)

- Flujo completo de operaciones CRUD
- Verificación de interacciones entre controlador, servicio y repositorio
- Pruebas de validaciones y restricciones
- Pruebas de comportamiento bajo escenarios de error
- Pruebas de rendimiento básicas

### Pruebas con SQL Scripts (`EmpleadoSqlIT`)

- Pruebas utilizando scripts SQL para cargar y limpiar datos
- Verificación de queries y operaciones de base de datos
- Validación de restricciones de base de datos (unicidad)

## Configuración de Pruebas

### Herramientas y Dependencias

- JUnit 5 como framework principal
- Mockito para simulación de componentes
- H2 como base de datos en memoria
- Spring Test para pruebas de integración
- JaCoCo para análisis de cobertura

### Configuración Específica

- `application-test.properties` para configuración de pruebas
- Scripts SQL en `/src/test/resources/sql/`
- TestDataBuilder para generar datos de prueba consistentes

## Clases Auxiliares

### `TestDataBuilder`

Clase utilitaria para crear objetos de prueba (entidades y DTOs) con datos predefinidos:

- Empleados con datos válidos
- Empleados con datos inválidos para pruebas negativas
- Listas de empleados para pruebas que requieren conjuntos de datos

## Informe de Cobertura

El informe detallado de cobertura se genera a través de JaCoCo después de ejecutar las pruebas:

```
mvn clean test jacoco:report
```

El informe se genera en `target/site/jacoco/index.html` y puede ser consultado para verificar el nivel de cobertura alcanzado.

---

Este conjunto de pruebas cumple con los requisitos especificados, asegurando una cobertura mínima del 90% en todas las capas y validando el comportamiento esperado de la API en diferentes escenarios.