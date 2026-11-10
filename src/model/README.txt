# Modelo Java - QuindíoStay

Contenido: 27 clases POJO correspondientes a las 27 tablas del DDL actual.

## Instalación
Copia todos los archivos `.java` dentro del paquete `model` que creaste en `src` de IntelliJ.

Cada clase incluye:
- Atributos.
- Constructor vacío.
- Constructor con todos los atributos.
- Getters y setters.

## Tipos usados
- NUMBER(10): Long
- NUMBER(3), NUMBER(4), NUMBER(2): Integer
- NUMBER(p,2): BigDecimal
- VARCHAR2 / CHAR: String
- DATE: LocalDate
- TIMESTAMP: LocalDateTime

Las claves foráneas se representan como identificadores Long. Ocupacion contiene sus dos columnas de clave primaria compuesta.
