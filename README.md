# Assistent-AI

Aplicacion de escritorio para equipos de Network Marketing. Centraliza miembros, prospectos, tareas y activos personales con una vista compartida para el equipo.

## Ejecutar la aplicacion

En VS Code abre [Main.java](backend/src/main/java/com/facundo/assistentia/Main.java) y selecciona **Run Java**. Tambien puedes presionar `F5` y elegir **Run AssistentIA Desktop**.

Al iniciar se abre una ventana nativa de AssistentIA. No requiere navegador ni un puerto fijo.

Desde terminal:

```powershell
cd backend
.\mvnw spring-boot:run
```

## Primer uso

1. En la pantalla inicial abre **Crear cuenta**.
2. Registra el primer miembro del equipo. Esa primera cuenta recibe el rol de administrador.
3. Los siguientes integrantes crean su propia cuenta con usuario y contrasena.
4. Cada miembro inicia sesion con sus credenciales.

Las contrasenas se guardan con hash BCrypt, nunca como texto plano.

## Activos compartidos

Desde **Activos**, cada miembro selecciona un nombre del catalogo e ingresa su cantidad personal. La tabla inferior muestra todos los activos registrados por todos los miembros, junto con su propietario y fecha de actualizacion.

Catalogo inicial:

- NETS
- HMAP COIN
- GICO COIN
- GAME GOS COIN
- REEX COIN
- HEXA COIN
- MLC COIN
- 7PT COUPON
- ANTALLAGI COIN
- DOMINION COIN
- NLT COIN
- 9PT COUPON
- INT COIN
- 7PT PRO COUPON
- REEX.MINER
- PREFACTORY DX
- FACTORY.DRONEX
- FACTORY.DRONEX+

## Datos y despliegue

Para el desarrollo local, las cuentas y activos se guardan en H2 dentro de `backend/data/`. Esa carpeta esta excluida de Git porque contiene datos de usuarios.

La configuracion acepta `DATABASE_URL`, `DATABASE_USERNAME` y `DATABASE_PASSWORD`. Al apuntar todas las instancias a una misma base PostgreSQL, los usuarios y activos quedan compartidos para el equipo. Para el despliegue productivo conviene ejecutar una API central en Docker y conectar los clientes de escritorio a esa API, en lugar de repartir credenciales de base de datos.

## Verificacion

```powershell
cd backend
.\mvnw test
```

## Siguiente prioridad

1. Desplegar una API central con PostgreSQL para compartir datos entre computadoras.
2. Implementar JWT y refresh token para autenticacion remota.
3. Persistir prospectos, reuniones y tareas con los mismos permisos de equipo.
4. Agregar gestion de roles de administrador, lider y miembro desde la aplicacion.
