# Sistema de Gestión de Empleados

Un sistema para la gestión de empleados desarrollado con Java Spring Boot y Thymeleaf para la interfaz de usuario web.

## Descripción

Esta aplicación proporciona una interfaz web para gestionar información de empleados, con un backend RESTful desarrollado en Spring Boot y una interfaz de usuario construida con Thymeleaf.

## Características

### Backend (Spring Boot)

- API REST completa para la gestión de empleados
- Arquitectura en capas (Controller, Service, Repository)
- Validación de datos y manejo de excepciones
- Base de datos H2 en memoria (fácilmente configurable para bases de datos persistentes)
- Documentación API con SpringDoc/OpenAPI (Swagger UI)
- Pruebas unitarias y de integración con cobertura superior al 90%

### Frontend (Thymeleaf)

- Interfaz web para la visualización de empleados
- Gestión completa CRUD (Crear, Leer, Actualizar, Borrar) de empleados a través de formularios web
- Interfaz de usuario renderizada en el servidor con plantillas Thymeleaf

## Capturas de Pantalla

### Dashboard
![Dashboard](screenshot-dashboard.png)

### Gestión de Empleados
![Empleados](screenshot-empleados.png)

*(Nota: Las capturas de pantalla podrían necesitar actualización si la interfaz de usuario ha cambiado significativamente).*

## Requisitos

- Java 17 o superior
- Maven 3.6 o superior

## Instalación y Ejecución

1. Clonar el repositorio:
   ```bash
   git clone [https://github.com/tuusuario/empleados-app.git](https://github.com/tuusuario/empleados-app.git)
   cd empleados-app

2. Compilar y ejecutar con Maven:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

3. Acceder a la aplicación:
   - **Frontend Thymeleaf**: http://localhost:8000/
   - **Swagger API Docs**: http://localhost:8000/swagger-ui.html
   - **API REST**: http://localhost:8000/api/empleados

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── empleados/
│   │           └── api/
│   │               ├── config/                  # Configuración Spring y SpringDoc/OpenAPI
│   │               ├── controller/              # Controladores REST
│   │               ├── dto/                     # Objetos de transferencia de datos
│   │               ├── exception/               # Excepciones personalizadas
│   │               ├── model/                   # Entidades JPA
│   │               ├── repository/              # Repositorios Spring Data
│   │               ├── service/                 # Servicios de negocio
│   │               └── ui/                      # Lógica de interfaz de usuario
│   │                   └── controller/          # Controladores para la interfaz web (Thymeleaf)
│   └── resources/
│       ├── application.properties              # Configuración de la aplicación
│       ├── templates/                          # Plantillas HTML (Thymeleaf)
│       └── META-INF/resources/                 # Recursos web estáticos (CSS, JS)
│           ├── themes/                         # Temas (CSS)
│           └── frontend/                       # Otros recursos frontend (CSS, JS)
└── test/
    └── java/                                  # Pruebas unitarias e integración
```

## Tecnologías Utilizadas

- **Backend**: Spring Boot, Spring Data JPA, Spring Web, H2 Database, Lombok, Swagger/OpenAPI
- **Frontend**: Thymeleaf
- **Testing**: JUnit, Mockito, Spring Test, JaCoCo

## Documentación

- [Documentación de la API (Swagger)](http://localhost:8000/swagger-ui.html)
- [Documentación de pruebas](TESTING.md)
- [Validación de pruebas](TEST_VALIDATION.md)

## Contribuir

1. Hacer un fork del repositorio
2. Crear una rama para tu característica: `git checkout -b feature/nueva-caracteristica`
3. Hacer commit de tus cambios: `git commit -m 'Añade nueva característica'`
4. Hacer push a la rama: `git push origin feature/nueva-caracteristica`
5. Enviar un pull request

## Licencia

Este proyecto está licenciado bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## Autor

Desarrollado como parte de un proyecto de demostración de arquitectura de aplicaciones Java modernas.