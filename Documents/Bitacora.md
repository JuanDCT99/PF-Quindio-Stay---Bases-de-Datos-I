# Bitácora del Proyecto QuindíoStay

Registro de avances y contexto del proyecto para continuidad entre sesiones.

---

## Información del Proyecto

| Campo | Valor |
|-------|-------|
| **Nombre** | QuindíoStay |
| **Tipo** | Plataforma turismo/hotelería |
| **Enfoque** | Reservas hoteleras (fincas, glampings, hoteles boutique) |
| **Ubicación** | Quindío, Colombia |
| **Equipo** | 3 ingenieros de sistemas |
| **Materia** | Bases de Datos I |
| **Fecha límite** | 20/11/2026 (sustentación) |

---

## Entregas

| # | Fecha | Descripción | Estado |
|---|-------|-------------|--------|
| 1 | 25/09/2026 | Análisis: reglas de negocio, diccionario preliminar, alcance, modelo E-R preliminar | Pendiente |
| 2 | 16/10/2026 | Modelo E-R extendido + modelo relacional normalizado (3FN/BCNF) con justificación | Pendiente |
| 3 | 13/11/2026 | BD implementada en Oracle XE (DDL + datos de prueba), CRUD, 10 reportes PDF | Pendiente |
| Sustentación | 20/11/2026 | Defensa oral y demostración en vivo | Pendiente |

---

## Sesiones

### Sesión 1 - 05/09/2026

**Participantes:** Juanda (y equipo)

**Actividades realizadas:**
1. Exploración del repositorio existente
2. Revisión del documento `quindiostay_proyecto.md` (requisitos)
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

**Pendiente para próxima sesión:**
- Iniciar **Entrega 1**: Análisis del proyecto
  - Reglas de negocio
  - Diccionario preliminar de datos
  - Alcance del sistema
  - Modelo E-R preliminar

**Notas:**
- Aún no se define la tecnología (web vs local)
- Aún no se define lenguaje de programación
- Oracle XE es la BD obligatoria para el proyecto

---

## Decisiones Pendientes

| Decisión | Opciones | Notas |
|----------|----------|-------|
| Tipo de aplicación | Web / Escritorio | Definir antes de Entrega 3 |
| Lenguaje | Python / Java / JavaScript / Otro | Verificar conocimiento del equipo |
| Framework | Flask / Spring / Express / Otro | Depende del lenguaje |

---

## Archivos Importantes

| Archivo | Ubicación | Propósito |
|---------|-----------|-----------|
| Requisitos del proyecto | `Documents/quindiostay_proyecto.md` | Especificaciones oficiales |
| Guía de Git | `Documents/Guia_Git.md` | Referencia para el equipo |
| Desarrollo teórico | `Documents/Desarrollo_Teorico.md` | Registro de avances (ya existía) |
