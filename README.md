# Bazar Manager

## Descripción
Este proyecto es una API diseñada para manejar las operaciones de un bazar que ha incrementado significativamente sus ventas. Dado que el manejo manual del stock y registro de ventas se ha vuelto casi imposible, se requiere una solución automatizada. La API permitirá realizar todas las operaciones a través de una aplicación web y una aplicación móvil que utilizarán el mismo backend.

## Tecnologías Utilizadas
- **Java 17**: Lenguaje de programación.
- **Spring Boot v3.2**: Framework principal para el desarrollo de la API.
- **Spring Security**: Para la autenticación y autorización de los usuarios.
- **JWT 0.12**: Para la generación de tokens de autenticación.
- **Spring Data JPA**: Para la persistencia de datos.
- **Hibernate**: ORM para mapear objetos a la base de datos.
- **MySQL**: Sistema de gestión de base de datos.
- **Spring Boot Validations**: Para validar los datos de entrada en la API.
- **Mockito y JUnit**: Utilizados para realizar pruebas unitarias y de integración.
- **Docker**: Para empaquetar la app y sus dependencias, asegurando que se pueda ejecutar de manera consistente en diferentes entornos.
- **Swagger**: Para documentar y visualizar todos los endpoints de la API.
- **ModelMapper**: Utilidad para mapear objetos.
- **Lombok**: Biblioteca para reducir el código repetitivo de Java, como getters, setters, etc.


## Cómo Iniciar
Para configurar y ejecutar este proyecto localmente, sigue estos pasos:
1. Clona el repositorio en tu máquina local.
2. Crea una base de datos en MySQL.
3. Configura la cadena de conexión, usuario y contraseña de tu base de datos en `application.properties` utilizando variables de entorno para mayor seguridad:
   ```properties
   spring.datasource.url=${DB_URL}
   spring.datasource.username=${DB_USERNAME}
   spring.datasource.password=${DB_PASSWORD}
4. Instala las dependencias del proyecto con:
   mvn install
5. Ejecuta la aplicación utilizando:
   mvn spring-boot:run

La aplicación estará disponible en `http://localhost:8080`.

## Endpoints
Los endpoints de la API están documentados y se pueden consultar a través de Swagger en la siguiente dirección una vez que el proyecto esté en ejecución:
[Swagger UI](http://localhost:8080/swagger-ui/index.html)

Esto proporcionará una interfaz visual para interactuar con la API y explorar sus capacidades.

## Restricciones de acceso:
- **Sin autenticación**: puede registrarse y loguearse.
- **Con autenticación y rol USER**: sólo puede ver Clientes y Productos.
- **Con autenticación y rol ADMIN**
  * Ver, agregar, editar y eliminar Clientes, Productos y Ventas.
  * Consultar productos con falta de stock.
  * Consultar productos de una venta determinada.
  * Consultar monto total y cantidad de ventas de un día determinado.
  * Consultar la mayor venta realizada.


## Licencia
Este proyecto está disponible bajo la licencia MIT. Puedes utilizar y modificar el código según las condiciones de la licencia.
