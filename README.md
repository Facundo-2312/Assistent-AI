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

1. En la pantalla inicial abre **Crear equipo**.
2. Registra el nombre del equipo y la cuenta administradora. La aplicacion genera un codigo unico.
3. Comparte ese codigo con los demas miembros para que usen **Unirse con codigo**.
4. Cada miembro inicia sesion con sus credenciales y queda visible en la misma base compartida.

## Control de administracion

- La cuenta que crea el equipo inicia con rol Administrador.
- En la seccion Usuarios, el Administrador puede crear nuevos usuarios y editar nombre, correo, rol y contrasena de usuarios existentes.
- Los cambios se guardan en la base de datos y permanecen despues de reiniciar la aplicacion.

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

Para el desarrollo local, las cuentas, equipos y activos se guardan en H2 dentro de `backend/data/`. Esa carpeta esta excluida de Git porque contiene datos de usuarios.

La configuracion acepta `DATABASE_URL`, `DATABASE_USERNAME` y `DATABASE_PASSWORD`. Al apuntar todas las instancias a una misma base PostgreSQL y exponer esta app como API central, los usuarios, equipos y activos quedan compartidos para el equipo. El flujo de acceso ahora usa un codigo de equipo para unir nuevas instalaciones al mismo workspace.

## Modo servidor principal (esta computadora)

Para que los cambios del equipo se vean reflejados en esta PC como servidor principal:

1. En esta computadora, levanta PostgreSQL con Docker:

```powershell
docker compose up -d postgres
```

2. Inicia el backend en esta computadora usando PostgreSQL local:

```powershell
cd backend
$env:DATABASE_URL="jdbc:postgresql://localhost:5432/assistentia"
$env:DATABASE_USERNAME="postgres"
$env:DATABASE_PASSWORD="postgres"
.\mvnw spring-boot:run
```

3. En cada computadora del equipo, configurar esas mismas variables pero reemplazando `localhost` por la IP de esta computadora, por ejemplo `192.168.1.50`:

```powershell
$env:DATABASE_URL="jdbc:postgresql://192.168.1.50:5432/assistentia"
$env:DATABASE_USERNAME="postgres"
$env:DATABASE_PASSWORD="postgres"
```

4. Asegura firewall/red para permitir conexiones TCP al puerto `5432` desde la LAN.

Con esta configuracion, todos operan sobre la misma base central y cualquier alta/edicion se refleja en el servidor principal.

### Scripts rapidos (PowerShell)

- Servidor principal (esta PC):

```powershell
.\scripts\start-server-main.ps1
```

- Cliente apuntando al servidor principal:

```powershell
.\scripts\start-client-node.ps1 -ServerIp 192.168.1.41
```

- Abrir firewall para PostgreSQL (ejecutar como administrador):

```powershell
.\scripts\open-firewall-postgres.ps1
```

- Generar ZIP para compartir:

```powershell
.\scripts\build-share-zip.ps1
```

## Verificacion

```powershell
cd backend
.\mvnw test
```

## Siguiente prioridad

1. Publicar los endpoints de lectura/escritura desde el cliente de escritorio remoto.
2. Implementar JWT y refresh token para autenticacion remota.
3. Persistir prospectos, reuniones y tareas con los mismos permisos de equipo.
4. Agregar gestion de roles de administrador, lider y miembro desde la aplicacion.
