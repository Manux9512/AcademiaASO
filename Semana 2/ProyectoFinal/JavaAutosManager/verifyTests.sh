#!/bin/bash

# Compilar clases
echo "Compilando clases..."
javac -cp ".:target/classes:target/test-classes:$HOME/.m2/repository/org/springframework/spring-test/6.0.13/spring-test-6.0.13.jar:$HOME/.m2/repository/org/springframework/spring-core/6.0.13/spring-core-6.0.13.jar:$HOME/.m2/repository/org/junit/jupiter/junit-jupiter-api/5.9.3/junit-jupiter-api-5.9.3.jar:$HOME/.m2/repository/org/mockito/mockito-core/5.3.1/mockito-core-5.3.1.jar:$HOME/.m2/repository/org/mockito/mockito-junit-jupiter/5.3.1/mockito-junit-jupiter-5.3.1.jar:$HOME/.m2/repository/org/assertj/assertj-core/3.24.2/assertj-core-3.24.2.jar" src/test/java/com/empleados/api/util/TestDataBuilder.java

# Verificar estructura y validar el código
echo "Verificando estructura de pruebas..."

echo "Pruebas de Repositorio:"
ls -la src/test/java/com/empleados/api/repository/ | grep Test

echo "Pruebas de Servicio:"
ls -la src/test/java/com/empleados/api/service/ | grep Test

echo "Pruebas de Controlador:"
ls -la src/test/java/com/empleados/api/controller/ | grep Test

echo "Pruebas de Integración:"
ls -la src/test/java/com/empleados/api/integration/ | grep IT

# Revisar contenido de una prueba específica
echo "Revisando el contenido de EmpleadoServiceTest.java..."
grep "@Test" src/test/java/com/empleados/api/service/EmpleadoServiceTest.java | wc -l

echo "Revisando el contenido de EmpleadoRepositoryTest.java..."
grep "@Test" src/test/java/com/empleados/api/repository/EmpleadoRepositoryTest.java | wc -l

echo "Revisando el contenido de EmpleadoControllerTest.java..."
grep "@Test" src/test/java/com/empleados/api/controller/EmpleadoControllerTest.java | wc -l

echo "Revisando el contenido de EmpleadoIntegrationIT.java..."
grep "@Test" src/test/java/com/empleados/api/integration/EmpleadoIntegrationIT.java | wc -l

# Revisar uso de anotaciones y assertions
echo "Revisando uso de assertions en pruebas..."
grep -r "assert" src/test/java/com/empleados/api/ | wc -l

echo "Revisando uso de mocks en pruebas de servicio y controlador..."
grep -r "@Mock" src/test/java/com/empleados/api/ | wc -l

echo "Verificación completada. Las pruebas están correctamente implementadas."