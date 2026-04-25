# bazar-spring-boot-api

API REST de un bazar ficticio. Sandbox personal para aprender Claude Code CLI.

## Contexto

- Proyecto de práctica. No hay usuarios reales ni datos sensibles.
- Rama de trabajo actual: `claude-code-lab`. `master` es intocable, es el código original.
- DB: MySQL local. Si no está levantada, los tests de integración fallan; está bien.

## Convenciones del repo (no cambiar sin consultar)

- El código existente mezcla idiomas: dominio en español (`Cliente`, `Producto`, `Venta`) e infraestructura en inglés (controllers, services, repositories). Respetá esa convención al agregar código nuevo.
- DTOs separados en `dtos/requestDTOs/<entidad>DTOs/` y `dtos/responseDTOs/<entidad>DTOs/`.
- Fixtures de test en `test/java/.../datos/`.
- Autenticación: JWT con filtro en `security/filters/`.

## Comandos frecuentes

- Levantar app: `./mvnw spring-boot:run`
- Tests: `./mvnw test`
- Empaquetar: `./mvnw clean package`

## Objetivos de aprendizaje en este repo

Estoy usando este proyecto para practicar Claude Code CLI. Cuando te pida algo, además de hacerlo, mencioná brevemente qué primitiva del CLI estás usando (bash, edit, read, etc.) cuando sea didácticamente útil. No hace falta que lo hagas siempre, solo cuando agregue valor.
