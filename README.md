# Bazar Manager

## Descripción
Este proyecto es una API diseñada para manejar las operaciones de un bazar que ha incrementado significativamente sus ventas. Dado que el manejo manual del stock y registro de ventas se ha vuelto casi imposible, se requiere una solución automatizada. La API permitirá realizar todas las operaciones a través de una aplicación web y una aplicación móvil que utilizarán el mismo backend.

## Tecnologías Utilizadas
- **Java**: Lenguaje de programación.
- **Spring Boot**: Framework principal para el desarrollo de la API.
- **MySQL**: Sistema de gestión de base de datos.
- **JPA + Hibernate**: ORM para el manejo de la base de datos.
- **ModelMapper**: Utilidad para mapear objetos.
- **Mockito y JUnit**: Utilizados para realizar pruebas unitarias y de integración.
- **Swagger**: Para documentar y visualizar todos los endpoints de la API.
- **Lombok**: Biblioteca para reducir el código repetitivo de Java, como getters, setters, etc.
- **Spring Boot Validations**: Para validar los datos de entrada en la API.

## Cómo Iniciar
Para configurar y ejecutar este proyecto localmente, sigue estos pasos:
1. Clona el repositorio en tu máquina local.
2. Crea una base de datos en MySQL.
3. Configura la cadena de conexión, usuario y contraseña de tu base de datos en `application.properties` utilizando variables de entorno para mayor seguridad:
   ```properties
   spring.datasource.url=${DATABASE_URL}
   spring.datasource.username=${DATABASE_USER}
   spring.datasource.password=${DATABASE_PASSWORD}
4. Instala las dependencias del proyecto con:
   mvn install
5. Ejecuta la aplicación utilizando:
   mvn spring-boot:run

La aplicación estará disponible en `http://localhost:8080`.

## Endpoints
Los endpoints de la API están documentados y se pueden consultar a través de Swagger en la siguiente dirección una vez que el proyecto esté en ejecución:
[Swagger UI](http://localhost:8080/swagger-ui/index.html)

Esto proporcionará una interfaz visual para interactuar con la API y explorar sus capacidades.

## Licencia
Este proyecto está disponible bajo la licencia MIT. Puedes utilizar y modificar el código según las condiciones de la licencia.
