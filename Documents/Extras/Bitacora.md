# Bitácora del Proyecto QuindíoStay

Registro de avances, decisiones y contexto del proyecto para continuidad entre sesiones.

**Equipo:** 3 ingenieros de sistemas
- Juan David Cardozo Torres — C.C. 1094881210
- Camilo Ruiz López — C.C. 1098307006
- Erwin Harder Garzón — C.C. 1092852986

---

## Información del Proyecto

|| Campo | Valor |
|---|---|---|
| **Nombre** | QuindíoStay |
| **Tipo** | Plataforma turismo/hotelería |
| **Enfoque** | Reservas hoteleras (fincas, glampings, hoteles boutique) |
| **Ubicación** | Quindío, Colombia |
| **Materia** | Bases de Datos I — Universidad del Quindío |
| **Fecha límite** | 20/11/2026 (sustentación) |

---

## Entregas

| # | Fecha | Descripción | Estado |
|---|---|---|---|
| 1 | 25/09/2026 | Análisis: reglas de negocio, diccionario preliminar, alcance, modelo E-R preliminar | **Revisada y APROBADA por el docente** |
| 2 | 16/10/2026 | Modelo E-R extendido + modelo relacional normalizado (3FN/BCNF) con justificación | **Revisada y APROBADA por el docente** |
| 3 | 13/11/2026 | BD implementada en Oracle XE (DDL + datos de prueba), CRUD, 10 reportes PDF | En progreso |
| Sustentación | 20/11/2026 | Defensa oral y demostración en vivo | Pendiente |

**Nota:** Las entregas 1 y 2 sirven como guía para la Entrega 3 y sus correcciones. El esquema relacional del documento Entrega 2 es el referente oficial para la implementación en Oracle XE.

---

## Sesiones

### Sesión 1 — 05/09/2026

**Participantes:** Juanda (y equipo)

**Actividades realizadas:**
1. Exploración del repositorio existente
2. Revisión del documento `quindiostay_proyecto.md` (requisitos del producto)
3. Diseño y creación de estructura de carpetas:

   ```
   Documents/
   ├── 01_Analisis/
   ├── 02_Modelo/
   └── 03_Reportes/
   Database/
   ├── DDL/
   ├── DML/
   └── Queries/
   src/
   ├── models/
   ├── controllers/
   ├── routes/
   ├── views/
   └── utils/
   reports/
   ```

4. Creación de `Guia_Git.md` para el equipo
5. Creación de esta bitácora

**Pendiente para próxima sesión:** iniciar la Entrega 1 (análisis del proyecto).

---

### Sesión 2 — 06/11/2026 (síntesis de trabajo previo)

**Participantes:** Juanda (investigación de documentación)

**Actividades realizadas:**
1. Lectura completa de `Documents/Entrega1_documentación/ENTREGA 1 - BASES DE DATOS I.docx.md` (diccionario preliminar, reglas de negocio RN-01 a RN-12, alcance).
2. Lectura completa de `Documents/Entrega2_documentación/ENTREGA 2 - BASES DE DATOS I.md` (modelo E-R extendido, esquema relacional definitivo, normalización 1FN→2FN→3FN→BCNF, integralidad referencial, 10 reportes, arquitectura).
3. Lectura de `Documents/quindiostay_proyecto.md` (requisitos oficiales).
4. Lectura de `Documents/Extras/Bitacora.md`, `Desarrollo_Teorico.md` y `Guia_Git.md`.
5. Revisión del repositorio: estructura de carpetas, archivos, estado de Git y contenido del script SQL.

**Resultado de la revisión:**

- Las **entregas 1 y 2 están aprobadas** por el docente; se usan como guía para la Entrega 3.
- El esquema relacional final (Entrega 2) consta de **23 entidades fuertes + 1 débil (Folio)**, normalizado a 3FN/BCNF.
- El script SQL `Database/JucameStay - Reservas Hoteleras.sql` está **incompleto y con errores** (columnas faltantes en `Habitacion`, `Ocupacion`, `Servicio`; FK apuntando a tabla inexistente `Titular` en `Reserva`; faltan paréntesis en `CHECK`; mezcla de nomenclaturas camelCase/snake_case; tablas no declaradas como las de la Entrega 2).
- Carpetas `Database/DDL`, `Database/DML`, `Database/Queries`, `src/*` y `reports/` están vacías: faltan el cuerpo de la BD, los datos de prueba y los 10 reportes.
- La aplicación (`src/`) no ha empezado: la arquitectura elegida es Java 21 + JavaFX + Oracle XE (JDBC, patrón DAO).

---

## Decisiones de diseño (confirmadas hasta Entrega 2)

| Decisión | Opción elegida | Motivo |
|---|---|---|
| Tipo de aplicación | Escritorio (JavaFX) | Defirió de la web; arquitectura JavaFX + capas |
| Lenguaje | Java 21 | Elegido en la Entrega 2 |
| Motor de BD | Oracle XE | Obligatorio para el proyecto |
| Conexión | JDBC (driver ojdbc) | Para el acceso a la base de datos |
| Patrón de acceso a datos | DAO por entidad | Capa de persistencia separada |
| Seguridad | Login por rol; contraseñas con hash (`contrasena_hash`) | Administrador, recepcionista, huésped |

**Reglas de negocio retenidas (de la Entrega 1, aprobada):** RN-01 a RN-12 (sobre solapamiento de reservas, noches, bloqueos, tarifas, intermediarios, check-in, estados de reserva, capacidad de ocupantes, etc.).

**Reglas que requieren trigger o capa de servicio (de la Entrega 2, aprobada):**
- RN-01: no más de una reserva activa solapada por habitación
- RN-03: habitación bloqueada no disponible
- RN-04: precio noche pactado según tipo + temporada
- RN-07: check-in solo en reserva CONFIRMADA, no antes de entrada
- RN-08: PENDIENTE vencido → CANCELADA automáticamente
- RN-09: check-out pasa a EN_PREPARACION; retraso → noche adicional
- RN-10: no reservas hacia el pasado
- RN-11: estados válidos y transiciones
- RN-12: ocupantes no superan capacidad_maxima
- RN-13: servicio solo dentro de su horario
- RN-14: uso exclusivo en turno sin solapes
- RN-17: solo se califica una reserva finalizada, por titular/acompañante
- RN-18: reserva externa colisionando → Conflicto_Canal
- RN-19: NO_SHOW solo si CONFIRMADA y fecha de entrada vencida
- RN-20: cierre de folio con saldo pendiente solo por administrador
- RN-21: folio cerrado no acepta pagos/sanciones
- RN-22: solo titular puede ser TITULAR en Ocupacion
- RN-23: todas las habitaciones de una reserva pertenecen al mismo alojamiento
- RN-24: al menos una habitación para confirmar; al menos un ocupante para check-in
- RN-25: temporadas no se solapan
- RN-26: precio_total derivado (suma de noches × precio_noche_pactado)
- RN-28: solo administrador configura; recepcionista gestiona reservas, check-in/out, folio

---

## Modelo de datos retenido para la Entrega 3

**Fuente:** `Documents/Entrega2_documentación/ENTREGA 2 - BASES DE DATOS I.md` (clave para la implementación).

**Entidades fuertes (23):** Alojamiento, Tipo_Habitacion, Habitacion, Temporada, Tarifa, Intermediario, Huesped, Reserva, Reserva_Habitacion, Ocupacion, Personal, Perfil, Usuario, Servicio, Uso_Servicio, Calificacion, Folio, Pago, Sancion, Bloqueo, Conflicto_Canal, Reporte, Reporte_Generado, Notificacion, Municipio, Plan, Perfil_Cliente.

**Entidad débil (1):** Folio (1:1 con Reserva; su clave es `id_Reserva`).

**Esquema relacional definido en la Entrega 2 (comparten nombre y estructura con la implementación):** ALOJAMIENTO, TIPO_HABITACION, HABITACION, TEMPORADA, TARIFA, INTERMEDIARIO, HUESPED, RESERVA, RESERVA_HABITACION, OCUPACION, PERSONAL, PERFIL, USUARIO, SERVICIO, USO_SERVICIO, CALIFICACION, FOLIO, PAGO, SANCION, BLOQUEO, CONFLICTO_CANAL, REPORTE, REPORTE_GENERADO, NOTIFICACION, MUNICIPIO, PLAN, PERFIL_CLIENTE.

**Parte de las reglas de integridad referencial (Entrega 2):**
- `ON DELETE RESTRICT` para padres que no pueden eliminarse mientras tengan hijos (tipo habitación clasificando, alojamiento con habitaciones, reserva con titular, etc.)
- `ON DELETE CASCADE` para hijos que pierden sentido sin el padre (habitación de una reserva, ocupación de una reserva_habitacion, etc.)
- Se mantiene el estado de las entidades (ej. `estado` en HUESPED, USUARIO; `estado_operativo` en HABITACION) para el historial; no se eliminan row históricas.

---

## Situación actual del repositorio

```
Documents/
├── Entrega1_documentación/   • ENTREGA 1 - BASES DE DATOS I.docx (.md)
├── Entrega2_documentación/   • ENTREGA 2 - BASES DE DATOS I.md (.docx)
├── Entrega3_documentación/   • (vacía — por crear)
├── Extras/                   • Bitacora.md, Desarrollo_Teorico.md, Guia_Git.md,
│                              • quindiostay_proyecto.md, imagen_requisitos_entrega3.jpeg
Database/
├── DDL/                      • (vacía — falta el script corregido de Oracle XE)
├── DML/                      • (vacía — faltan INSERT de datos de prueba)
├── Queries/                  • (vacía — faltan las 10 consultas de reportes)
├── JucameStay - Reservas Hoteleras.sql   • DDL incompleto/erróneo (ver notas)
src/                          • (vacía — faltan modelos, controladores, rutas, vistas, utils)
reports/                      • (vacía — faltan los 10 reportes en PDF)
```

**Estado de Git:**
- Rama actual: `dev_juand` (merged sobre `main`)
- Últimos commits: `c937eb3` (Merge), `e747d8e` (Estructura de directorios), `4143f76` (Introduction)
- Archivos no rastreados: `Database/`, `Documents/Entrega1_documentación/`, `Documents/Entrega2_documentación/`, `Documents/Extras/`, `Introducción/`

---

## Notas de calidad para la Entrega 3 (correcciones destacadas)

1. **DL corregir antes de ejecutar:** `Habitacion` tiene FK a columnas `idTipoHabitacion`/`idAlojamiento` que no están declaradas; `Reserva` tiene FK a `Titular` (tabla inexistente) y su columna `idTitular` no está definida; `Ocupacion` y `Servicio` faltan columnas en sus FKs y sus `CHECK` tienen paréntesis mal cerrados.
2. **Nomenclatura:** usar `snake_case` (ej. `id_alojamiento`, `estado_operativo`) para coincidir con el esquema relacional de la Entrega 2; no mezclar con camelCase.
3. **Tablas por agregar/complete:**
   - ALOJAMIENTO (8 atributos)
   - TIPO_HABITACION (4)
   - HABITACION (7)
   - TEMPORADA (6)
   - TARIFA (4)
   - INTERMEDIARIO (5)
   - HUESPED (7)
   - RESERVA (8)
   - RESERVA_HABITACION (9)
   - OCUPACION (3)
   - PERSONAL (7)
   - PERFIL (3)
   - USUARIO (6)
   - SERVICIO (7)
   - USO_SERVICIO (7)
   - CALIFICACION (6)
   - FOLIO (4)
   - PAGO (5)
   - SANCION (6)
   - BLOQUEO (6)
   - CONFLICTO_CANAL (11)
   - REPORTE (4)
   - REPORTE_GENERADO (6)
   - NOTIFICACION (6)
   - MUNICIPIO (3)
   - PLAN (6)
   - PERFIL_CLIENTE (5)
4. **Datos de prueba:** cargar al menos un alojamiento, tipos de habitación, habitaciones, temporadas, tarifas, intermediarios, huéspedes, reservas, ocupaciones y folios para probar el CRUD y los reportes.
5. **Reportes:** los 10 reportes (3 simples, 4 intermedios, 3 avanzados) definidos en la Entrega 2, exportados a PDF.
6. **Aplicación:** una vez la BD esté correcta, comenzar con la capa `src/` (modelos, DAO, controladores, rutas, vistas, utils) y la integración con Oracle XE.

---

## Archivos importantes del proyecto

| Archivo | Ubicación | Propósito |
|---|---|---|
| Requisitos del proyecto | `Documents/quindiostay_proyecto.md` | Especificaciones oficiales del producto |
| Guía de Git | `Documents/Guia_Git.md` | Referencia de trabajo en equipo |
| Desarrollo teórico | `Documents/Desarrollo_Teorico.md` | Entidades y prueba de filtros |
| Bitácora | `Documents/Extras/Bitacora.md` | Registro de avances (esta bitácora) |
| Entrega 1 | `Documents/Entrega1_documentación/ENTREGA 1 - BASES DE DATOS I.md` | Análisis, reglas de negocio, diccionario preliminar, E-R preliminar |
| Entrega 2 | `Documents/Entrega2_documentación/ENTREGA 2 - BASES DE DATOS I.md` | Modelo E-R extendido, esquema relacional definitivo, normalización, reportes |
| Script DDL | `Database/JucameStay - Reservas Hoteleras.sql` | Creación de tablas en Oracle XE (en revisión/corrección) |
| Imagen de requisito | `Documents/Extras/imagen_requisitos_entrega3.jpeg` | Extracto de la guía de la tercera entrega |
