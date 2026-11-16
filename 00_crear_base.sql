-- =====================================================================
-- SCRIPT ORACLE XE - CREACIÓN DE BASE DE DATOS QuindíoStay
-- Materia: Bases de Datos I - Universidad del Quindío
-- Equipo: Juan David Cardozo Torres · Camilo Ruiz López · Erwin Harder Garzón
-- =====================================================================
--
-- 1. Comprobar si la base ya existe
-- 2. Si no existe, crearla
-- 3. Aplicar el DDL de creación de tablas
-- 4. Insertar los datos de prueba
-- =====================================================================

DEFINE DB_NAME = 'QUINDIOSTAY'
DEFINE DB_USER = 'QUINDIOSTAY'
DEFINE DB_PASSWORD = 'Quindio123'

-- ============================================================
-- PASO 0: Comprobar si la base existe
-- ============================================================
COLUMN base_exists NEW_VALUE base_exists_var
SELECT CASE WHEN COUNT(*) > 0 THEN 'S' ELSE 'N' END AS base_exists
FROM V$DATABASE;

SELECT CASE
  WHEN EXISTS (SELECT 1 FROM V$DATABASE WHERE ROWNUM = 1) THEN 'S'
  ELSE 'N'
END AS base_activa
FROM DUAL;

-- ============================================================
-- PASO 1: Crear la base de datos (solo si no existe)
-- ============================================================
-- En Oracle XE se trabaja con una base PDB. Los datos de este script
-- se crean sobre la base actual (FREEPDB1). La tabla
-- V$DATABASE indica la base de datos activa.

-- ============================================================
-- PASO 2: Crear usuario y roles
-- ============================================================
-- El script DDL (01_creatables.sql) crea las tablas en el esquema
-- actual. Si quisieras aislar el usuario, creas el usuario y
-- ejecutas el script con "CONNECT &DB_USER.@" al inicio.

-- Opcional: crear usuario si no existe
BEGIN
  BEGIN
    EXECUTE IMMEDIATE 'CREATE USER &DB_USER. IDENTIFIED BY &DB_PASSWORD.';
    DBMS_OUTPUT.PUT_LINE('Usuario &DB_USER. creado.');
  EXCEPTION
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('Usuario &DB_USER. ya existe: ' || SQLERRM);
  END;

  BEGIN
    EXECUTE IMMEDIATE 'GRANT CONNECT, RESOURCE, DBA TO &DB_USER.';
    DBMS_OUTPUT.PUT_LINE('Privilegios concedidos a &DB_USER.');
  EXCEPTION
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('Error de privilegios: ' || SQLERRM);
  END;
END;
/

-- ============================================================
-- PASO 3: Aplicar el DDL
-- ============================================================
-- El script 01_creatables.sql contiene las 27 tablas.
-- Se ejecuta en el esquema del usuario actual (o de &DB_USER. si
-- se hace con CONNECT).

-- ============================================================
-- PASO 4: Cargar los datos (DML)
-- ============================================================
-- El script 01_seed_data.sql contiene los 157 INSERT.

-- ============================================================
-- FIN DEL SCRIPT
-- =====================================================================
