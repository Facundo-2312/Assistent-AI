# Arquitectura Inicial

## Estrategia

La plataforma arranca como un monolito modular. Es la opción correcta para la primera etapa porque reduce complejidad operativa y permite separar dominios sin pagar el costo de microservicios antes de tiempo.

## Estilo arquitectónico

- Clean Architecture
- Modular monolith
- API REST para la primera etapa
- Seguridad stateless con JWT en la fase 2

## Capas backend

### `application`

Casos de uso, orquestación y DTOs de salida/entrada.

### `domain`

Entidades, reglas de negocio, contratos de repositorio y objetos de valor.

### `infrastructure`

Persistencia, integración externa, proveedores y configuración técnica acoplada al framework.

### `interfaces`

Controllers REST, mappers de entrada/salida y contratos expuestos hacia clientes.

### `shared`

Configuración transversal, errores comunes, utilidades y convenciones globales.

## Módulos de negocio previstos

- Auth & Access
- Teams & Sponsorship
- CRM de Prospectos
- Meetings
- Tasks
- Assets
- Team Needs
- Library
- Calendar
- Internal Chat
- Analytics
- AI Assistant

## Estructura objetivo del backend

```text
backend/src/main/java/com/facundo/assistentia/
  application/
  domain/
  infrastructure/
  interfaces/
  shared/
```

## Seguridad

Fase actual:

- Seguridad base de Spring Security
- Endpoint técnico público para validar despliegue

Fase siguiente:

- Login con JWT access token
- Refresh token persistido
- BCrypt
- Recuperación de contraseña
- Verificación por correo
- RBAC con Admin, Leader y Member

## Persistencia

- PostgreSQL como base principal
- Flyway para migraciones versionadas
- JPA para persistencia de la primera etapa

## Observabilidad

- Actuator habilitado para health/info
- Logs estructurados en fase posterior
- Métricas y tracing cuando el uso lo justifique