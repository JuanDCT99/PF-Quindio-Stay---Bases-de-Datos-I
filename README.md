# QuindíoStay

Proyecto final de la asignatura **Bases de Datos I** (Universidad del Quindío, 2026). Es una aplicación de escritorio para la gestión de alojamientos turísticos del Quindío: propiedades, habitaciones, tarifas por temporada, huéspedes, reservas, servicios, pagos, folios, sanciones, calificaciones y reportes.

## Tecnologías

- **Java 21** + **JavaFX 21.0.8** (interfaz FXML)
- **Oracle Database** (XE 21c / Free 23ai) con driver **ojdbc11**
- **JDBC + DAO** para el acceso a datos y **OpenPDF** para exportar reportes
- **Maven** como gestor de dependencias y ejecución

## Estructura

```
00_crear_base.sql            Script maestro: crea el usuario QUINDIOSTAY
Database/DDL/                DDL de las 27 tablas (01_creatables.sql)
Database/DML/                Datos de prueba y usuario demo
Database/Queries/            10 consultas de reportes SQL
Documents/                   Documentación de las tres entregas
src/Main.java                Punto de entrada JavaFX
src/dao/                     Acceso a datos (DAO)
src/model/                   27 POJOs
src/service/                 Lógica de negocio y reportes PDF
src/utils/                   Conexión, sesión, seguridad y validaciones
src/ViewController/          Controladores de la interfaz
src/Resources/               FXML, CSS, imágenes y db.properties
```

## Requisitos previos

- JDK 21 y Maven instalados y en el `PATH`.
- Oracle Database accesible en `localhost:1521` (por defecto el PDB `XEPDB1`; si usa Oracle Free, cambie a `FREEPDB1`).
- Docker, si desea levantar Oracle XE 23ai en el contenedor `oracle23ai`.

La conexión se configura en `src/Resources/db.properties` (`db.url`, `db.user`, `db.password`). También puede sobrescribirse con las variables de entorno `DB_URL`, `DB_USER` y `DB_PASSWORD`, que tienen prioridad.

## Preparar la base de datos

Ejecute los scripts en este orden, conectado como usuario con privilegios (p. ej. `SYSTEM`):

```bash
# 1. Crear el usuario del esquema (QUINDIOSTAY / Quindio123)
@00_crear_base.sql

# 2. Crear las 27 tablas
@Database/DDL/01_creatables.sql

# 3. Cargar los datos de prueba
@Database/DML/01_seed_data.sql

# 4. Usuario demo para iniciar sesión (opcional)
@Database/DML/02_usuario_demo.sql
```

Si usa Oracle Free, confirme que el listener registra `FREEPDB1` (`lsnrctl status` y, si hace falta, `lsnrctl reload`).

## Ejecutar la aplicación

```bash
# Compilar
mvn -f pom.xml compile

# Ejecutar la interfaz JavaFX
mvn -f pom.xml javafx:run

# Probar la conexión a la base de datos
javac -cp "$(cat target/cp.txt)" -d target/classes src/TestConexion.java
java -cp "target/classes;$(cat target/cp.txt)" TestConexion
```

En Windows, reemplace `$(cat target/cp.txt)` por el contenido de `target/cp.txt` o use PowerShell: `Get-Content target/cp.txt`.

## Acceso de prueba

- Usuario: `admin_demo`
- Contraseña: `Admin123`

El sistema ofrece tres roles (`ADMINISTRADOR`, `RECEPCIONISTA` y `HUESPED`) con menús de reservas, reportes y generación de documentos PDF. Las 10 consultas SQL de reportes están en `Database/Queries/`.
