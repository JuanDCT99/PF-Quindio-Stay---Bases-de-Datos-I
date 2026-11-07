-- =====================================================================
--  DML - INSERT DE DATOS DE PRUEBA
--  Script: QuindíoStay
--  Dominio: Reservas Hoteleras
--  Materia: Bases de Datos I - Universidad del Quindío
--  Equipo: Juan David Cardozo Torres · Camilo Ruiz López · Erwin Harder Garzón
--  Versión: 1.0 (para la Entrega 3)
--
--  Descripción:
--  Este script contiene sentencias DML (Data Manipulation Language)
--  con datos mínimos de prueba para todas las tablas del modelo.
--
--  Características de los datos:
--  - Verosímiles (valores realistas del dominio hotelero)
--  - Asimétricos (no todos los registros son iguales)
--  - Coherentes con las reglas de negocio (PK, FK, CHECK, UNIQUE)
--
--  Nota: Este script debe ejecutarse DESPUÉS de 01_creatables.sql.
-- =====================================================================

-- =====================================================================
-- 1. MUNICIPIO (catálogo de municipios de Quindío)
-- =====================================================================
INSERT INTO Municipio (id_municipio, nombre_municipio, departamento) VALUES (1, 'Armenia', 'Quindío');
INSERT INTO Municipio (id_municipio, nombre_municipio, departamento) VALUES (2, 'Circasia', 'Quindío');
INSERT INTO Municipio (id_municipio, nombre_municipio, departamento) VALUES (3, 'Buenaventura', 'Quindío');
INSERT INTO Municipio (id_municipio, nombre_municipio, departamento) VALUES (4, 'Calarca', 'Quindío');
INSERT INTO Municipio (id_municipio, nombre_municipio, departamento) VALUES (5, 'Filandia', 'Quindío');
INSERT INTO Municipio (id_municipio, nombre_municipio, departamento) VALUES (6, 'Génova', 'Quindío');
INSERT INTO Municipio (id_municipio, nombre_municipio, departamento) VALUES (7, 'La Tebaida', 'Quindío');
INSERT INTO Municipio (id_municipio, nombre_municipio, departamento) VALUES (8, 'Pijao', 'Quindío');
INSERT INTO Municipio (id_municipio, nombre_municipio, departamento) VALUES (9, 'Quimbaya', 'Quindío');
INSERT INTO Municipio (id_municipio, nombre_municipio, departamento) VALUES (10, 'Salento', 'Quindío');

-- =====================================================================
-- 2. ALOJAMIENTO (algunos alojamientos del Quindío)
-- =====================================================================
INSERT INTO Alojamiento (id_alojamiento, nombre_alojamiento, nit, direccion, telefono, acepta_mascotas, hora_salida_limite, plazo_confirmacion_horas)
VALUES (1, 'Finca La Esperanza', '8-80912345', 'Cra 2 #45-67, Armenia, Quindío', '+57 603 1234567', 'S', '14:00:00', 24);

INSERT INTO Alojamiento (id_alojamiento, nombre_alojamiento, nit, direccion, telefono, acepta_mascotas, hora_salida_limite, plazo_confirmacion_horas)
VALUES (2, 'Glamping Andino', '9-12345678', 'Carrera 12 #50-30, Circasia, Quindío', '+57 603 2345678', 'N', '12:00:00', 48);

INSERT INTO Alojamiento (id_alojamiento, nombre_alojamiento, nit, direccion, telefono, acepta_mascotas, hora_salida_limite, plazo_confirmacion_horas)
VALUES (3, 'Hotel Boutique Quindío', '10-56789012', 'Calle 1 #20-10, Armenia, Quindío', '+57 603 3456789', 'S', '13:00:00', 24);

INSERT INTO Alojamiento (id_alojamiento, nombre_alojamiento, nit, direccion, telefono, acepta_mascotas, hora_salida_limite, plazo_confirmacion_horas)
VALUES (4, 'Cabañas del Río', '11-23456789', 'Vía Valle de Lourdes Km 2, Calarca, Quindío', '+57 603 4567890', 'S', '11:00:00', 72);

-- =====================================================================
-- 3. TIPO_HABITACION
-- =====================================================================
INSERT INTO Tipo_Habitacion (id_tipo_habitacion, nombre_tipo, descripcion, capacidad_estandar)
VALUES (1, 'SENCILLA', 'Habitación estándar con 1 cama de matrimonio', 2);

INSERT INTO Tipo_Habitacion (id_tipo_habitacion, nombre_tipo, descripcion, capacidad_estandar)
VALUES (2, 'DOBLE', 'Habitación con 1 cama de doble esposa', 2);

INSERT INTO Tipo_Habitacion (id_tipo_habitacion, nombre_tipo, descripcion, capacidad_estandar)
VALUES (3, 'SUITE', 'Suite con living room y cuna', 3);

INSERT INTO Tipo_Habitacion (id_tipo_habitacion, nombre_tipo, descripcion, capacidad_estandar)
VALUES (4, 'SUITE DELUXE', 'Suite completa con conexión Wi-Fi, minibar y balcón', 4);

INSERT INTO Tipo_Habitacion (id_tipo_habitacion, nombre_tipo, descripcion, capacidad_estandar)
VALUES (5, 'CABAÑA', 'Cabaña básica en la finca', 2);

INSERT INTO Tipo_Habitacion (id_tipo_habitacion, nombre_tipo, descripcion, capacidad_estandar)
VALUES (6, 'DORMITORIO COMPARTIDO', 'Dormitorio para grupos con camas individuales', 6);

-- =====================================================================
-- 4. HABITACION
-- =====================================================================
INSERT INTO Habitacion (id_habitacion, numero_habitacion, capacidad_maxima, descripcion, estado_operativo, id_tipo_habitacion, id_alojamiento)
VALUES (1, '101', 2, 'Habitación estándar en primer piso', 'DISPONIBLE', 1, 1);

INSERT INTO Habitacion (id_habitacion, numero_habitacion, capacidad_maxima, descripcion, estado_operativo, id_tipo_habitacion, id_alojamiento)
VALUES (2, '102', 2, 'Habitación estándar en primer piso', 'DISPONIBLE', 1, 1);

INSERT INTO Habitacion (id_habitacion, numero_habitacion, capacidad_maxima, descripcion, estado_operativo, id_tipo_habitacion, id_alojamiento)
VALUES (3, '201', 2, 'Habitación estándar en segundo piso', 'OCUPADA', 2, 1);

INSERT INTO Habitacion (id_habitacion, numero_habitacion, capacidad_maxima, descripcion, estado_operativo, id_tipo_habitacion, id_alojamiento)
VALUES (4, '202', 2, 'Habitación estándar en segundo piso', 'PREPARACION', 2, 1);

INSERT INTO Habitacion (id_habitacion, numero_habitacion, capacidad_maxima, descripcion, estado_operativo, id_tipo_habitacion, id_alojamiento)
VALUES (5, '103', 3, 'Suite con living room', 'DISPONIBLE', 3, 1);

INSERT INTO Habitacion (id_habitacion, numero_habitacion, capacidad_maxima, descripcion, estado_operativo, id_tipo_habitacion, id_alojamiento)
VALUES (6, '104', 3, 'Suite con living room', 'DISPONIBLE', 3, 1);

INSERT INTO Habitacion (id_habitacion, numero_habitacion, capacidad_maxima, descripcion, estado_operativo, id_tipo_habitacion, id_alojamiento)
VALUES (7, '01', '4', 'Suite Deluxe con balcón y minibar', 'DISPONIBLE', 4, 1);

INSERT INTO Habitacion (id_habitacion, numero_habitacion, capacidad_maxima, descripcion, estado_operativo, id_tipo_habitacion, id_alojamiento)
VALUES (8, '02', '4', 'Suite Deluxe con balcón y minibar', 'OCUPADA', 4, 1);

INSERT INTO Habitacion (id_habitacion, numero_habitacion, capacidad_maxima, descripcion, estado_operativo, id_tipo_habitacion, id_alojamiento)
VALUES (9, '501', 2, 'Cabaña básica en la finca', 'DISPONIBLE', 5, 2);

INSERT INTO Habitacion (id_habitacion, numero_habitacion, capacidad_maxima, descripcion, estado_operativo, id_tipo_habitacion, id_alojamiento)
VALUES (10, '502', 2, 'Cabaña básica en la finca', 'DISPONIBLE', 5, 2);

INSERT INTO Habitacion (id_habitacion, numero_habitacion, capacidad_maxima, descripcion, estado_operativo, id_tipo_habitacion, id_alojamiento)
VALUES (11, '101', 6, 'Dormitorio compartido para grupos', 'DISPONIBLE', 6, 2);

INSERT INTO Habitacion (id_habitacion, numero_habitacion, capacidad_maxima, descripcion, estado_operativo, id_tipo_habitacion, id_alojamiento)
VALUES (12, '102', 6, 'Dormitorio compartido para grupos', 'DISPONIBLE', 6, 2);

INSERT INTO Habitacion (id_habitacion, numero_habitacion, capacidad_maxima, descripcion, estado_operativo, id_tipo_habitacion, id_alojamiento)
VALUES (13, '100', 2, 'Habitación estándar con vista al valle', 'DISPONIBLE', 1, 3);

INSERT INTO Habitacion (id_habitacion, numero_habitacion, capacidad_maxima, descripcion, estado_operativo, id_tipo_habitacion, id_alojamiento)
VALUES (14, '101', 2, 'Habitación estándar con vista al río', 'OCUPADA', 1, 4);

INSERT INTO Habitacion (id_habitacion, numero_habitacion, capacidad_maxima, descripcion, estado_operativo, id_tipo_habitacion, id_alojamiento)
VALUES (15, '102', 2, 'Habitación estándar con vista al río', 'PREPARACION', 1, 4);

-- =====================================================================
-- 5. TEMPORADA
-- =====================================================================
INSERT INTO Temporada (id_temporada, nombre_temporada, anio, fecha_inicio, fecha_fin)
VALUES (1, 'ALTA', 2025, TO_DATE('2025-12-01', 'YYYY-MM-DD'), TO_DATE('2026-03-31', 'YYYY-MM-DD'));

INSERT INTO Temporada (id_temporada, nombre_temporada, anio, fecha_inicio, fecha_fin)
VALUES (2, 'MEDIA', 2025, TO_DATE('2025-04-01', 'YYYY-MM-DD'), TO_DATE('2025-11-30', 'YYYY-MM-DD'));

INSERT INTO Temporada (id_temporada, nombre_temporada, anio, fecha_inicio, fecha_fin)
VALUES (3, 'BAJA', 2025, TO_DATE('2026-04-01', 'YYYY-MM-DD'), TO_DATE('2026-12-31', 'YYYY-MM-DD'));

-- =====================================================================
-- 6. TARIFA
-- =====================================================================
INSERT INTO Tarifa (id_tarifa, precio_noche, id_tipo_habitacion, id_temporada)
VALUES (1, 250000, 1, 1);

INSERT INTO Tarifa (id_tarifa, precio_noche, id_tipo_habitacion, id_temporada)
VALUES (2, 320000, 2, 1);

INSERT INTO Tarifa (id_tarifa, precio_noche, id_tipo_habitacion, id_temporada)
VALUES (3, 450000, 3, 1);

INSERT INTO Tarifa (id_tarifa, precio_noche, id_tipo_habitacion, id_temporada)
VALUES (4, 550000, 4, 1);

INSERT INTO Tarifa (id_tarifa, precio_noche, id_tipo_habitacion, id_temporada)
VALUES (5, 180000, 1, 2);

INSERT INTO Tarifa (id_tarifa, precio_noche, id_tipo_habitacion, id_temporada)
VALUES (6, 240000, 2, 2);

INSERT INTO Tarifa (id_tarifa, precio_noche, id_tipo_habitacion, id_temporada)
VALUES (7, 350000, 3, 2);

INSERT INTO Tarifa (id_tarifa, precio_noche, id_tipo_habitacion, id_temporada)
VALUES (8, 420000, 4, 2);

INSERT INTO Tarifa (id_tarifa, precio_noche, id_tipo_habitacion, id_temporada)
VALUES (9, 150000, 1, 3);

INSERT INTO Tarifa (id_tarifa, precio_noche, id_tipo_habitacion, id_temporada)
VALUES (10, 200000, 2, 3);

INSERT INTO Tarifa (id_tarifa, precio_noche, id_tipo_habitacion, id_temporada)
VALUES (11, 300000, 3, 3);

INSERT INTO Tarifa (id_tarifa, precio_noche, id_tipo_habitacion, id_temporada)
VALUES (12, 380000, 4, 3);

-- =====================================================================
-- 7. INTERMEDIARIO
-- =====================================================================
INSERT INTO Intermediario (id_intermediario, nombre_intermediario, porcentaje_comision, tipo_canal, email_contacto)
VALUES (1, 'Agencia Viajes Quindío', 10.00, 'AGENCIA', 'contacto@viajesquindio.com');

INSERT INTO Intermediario (id_intermediario, nombre_intermediario, porcentaje_comision, tipo_canal, email_contacto)
VALUES (2, 'Portal de Reservas Expreso', 15.00, 'PLATAFORMA_EN_LINEA', 'ventas@reservasexpreso.com');

INSERT INTO Intermediario (id_intermediario, nombre_intermediario, porcentaje_comision, tipo_canal, email_contacto)
VALUES (3, 'Operador Turístico Andino', 8.00, 'OPERADOR_TURISTICO', 'info@andino.com');

INSERT INTO Intermediario (id_intermediario, nombre_intermediario, porcentaje_comision, tipo_canal, email_contacto)
VALUES (4, 'Empresa Corporativa S.A.', 5.00, 'CORPORATIVO', 'reservas@corporativa.com');

-- =====================================================================
-- 8. HUESPED
-- =====================================================================
INSERT INTO Huesped (id_huesped, nombre, documento, fecha_nacimiento, direccion, correo, telefono)
VALUES (1, 'Juan David Cardozo Torres', '1094881210', TO_DATE('1998-05-15', 'YYYY-MM-DD'), 'Cra 3 #10-20, Armenia, Quindío', 'juan.cardozo@email.com', '3001234567');

INSERT INTO Huesped (id_huesped, nombre, documento, fecha_nacimiento, direccion, correo, telefono)
VALUES (2, 'Camilo Ruiz López', '1098307006', TO_DATE('1997-11-20', 'YYYY-MM-DD'), 'Carrera 5 #20-15, Circasia, Quindío', 'camilo.ruiz@email.com', '3006543210');

INSERT INTO Huesped (id_huesped, nombre, documento, fecha_nacimiento, direccion, correo, telefono)
VALUES (3, 'Erwin Harder Garzón', '1092852986', TO_DATE('1996-03-10', 'YYYY-MM-DD'), 'Calle 10 #50-30, Armenia, Quindío', 'erwin.harder@email.com', '3007891234');

INSERT INTO Huesped (id_huesped, nombre, documento, fecha_nacimiento, direccion, correo, telefono)
VALUES (4, 'María Fernández García', '1087654321', TO_DATE('1990-08-25', 'YYYY-MM-DD'), 'Cra 2 #30-40, Armenia, Quindío', 'maria.fernandez@email.com', '3002234567');

INSERT INTO Huesped (id_huesped, nombre, documento, fecha_nacimiento, direccion, correo, telefono)
VALUES (5, 'Carlos Mendoza Rojas', '1076543210', TO_DATE('1988-01-30', 'YYYY-MM-DD'), 'Avenida 1 #60-10, Calarca, Quindío', 'carlos.mendoza@email.com', '3008912345');

-- =====================================================================
-- 9. PLAN
-- =====================================================================
INSERT INTO Plan (id_plan, nombre_plan, descripcion, incluye_desayuno, valor_adicional_noche, id_alojamiento)
VALUES (1, 'Plan Estándar', 'Habitación básica sin desayuno', 'N', 0, 1);

INSERT INTO Plan (id_plan, nombre_plan, descripcion, incluye_desayuno, valor_adicional_noche, id_alojamiento)
VALUES (2, 'Plan Familiar', 'Habitación + desayuno familiar', 'S', 50000, 1);

INSERT INTO Plan (id_plan, nombre_plan, descripcion, incluye_desayuno, valor_adicional_noche, id_alojamiento)
VALUES (3, 'Plan De Epi', 'Suite completa con desayuno', 'S', 80000, 1);

INSERT INTO Plan (id_plan, nombre_plan, descripcion, incluye_desayuno, valor_adicional_noche, id_alojamiento)
VALUES (4, 'Plan Cabaña', 'Cabaña con alojamiento básico', 'N', 0, 2);

INSERT INTO Plan (id_plan, nombre_plan, descripcion, incluye_desayuno, valor_adicional_noche, id_alojamiento)
VALUES (5, 'Plan Resort', 'Suite con todos los servicios', 'S', 120000, 3);

-- =====================================================================
-- 10. ROL
-- =====================================================================
INSERT INTO Rol (id_rol, nombre_rol, descripcion)
VALUES (1, 'ADMINISTRADOR', 'Acceso total al sistema: configuración, reportes, usuarios');

INSERT INTO Rol (id_rol, nombre_rol, descripcion)
VALUES (2, 'RECEPCIONISTA', 'Gestión de reservas, check-in, check-out, folios');

INSERT INTO Rol (id_rol, nombre_rol, descripcion)
VALUES (3, 'HUESPED', 'Acceso al historial de reservas y calificaciones');

-- =====================================================================
-- 11. USUARIO_SISTEMA
-- =====================================================================
INSERT INTO Usuario_Sistema (id_usuario, nombre_usuario, contrasena_hash, estado, id_rol, id_personal)
VALUES (1, 'admin_quindio', 'hash_admin_4df695db48563e6b8a908d6a9d9c4b6e', 'ACTIVO', 1, 1);

INSERT INTO Usuario_Sistema (id_usuario, nombre_usuario, contrasena_hash, estado, id_rol, id_personal)
VALUES (2, 'recepcion_quindio', 'hash_recepcion_a1b2c3d4e5f6', 'ACTIVO', 2, 2);

INSERT INTO Usuario_Sistema (id_usuario, nombre_usuario, contrasena_hash, estado, id_rol, id_personal)
VALUES (3, 'huésped_juanda', 'hash_huesped_78901234567890', 'ACTIVO', 3, NULL);

-- =====================================================================
-- 12. PERSONAL
-- =====================================================================
INSERT INTO Personal (id_personal, nombre, funcion, cargo, salario, horario_trabajo, id_alojamiento)
VALUES (1, 'Ana López Restrepo', 'Recepcionista principal', 'RECEPCIONISTA', 800000, 'Lunes a Viernes 08:00-16:00', 1);

INSERT INTO Personal (id_personal, nombre, funcion, cargo, salario, horario_trabajo, id_alojamiento)
VALUES (2, 'Carlos Gómez Pérez', 'Cajero', 'CAJERO', 650000, 'Martes a Sábado 10:00-18:00', 1);

INSERT INTO Personal (id_personal, nombre, funcion, cargo, salario, horario_trabajo, id_alojamiento)
VALUES (3, 'Laura Martínez Ruiz', 'Recepcionista de edición', 'RECEPCIONISTA', 750000, 'Lunes a Viernes 14:00-22:00', 2);

INSERT INTO Personal (id_personal, nombre, funcion, cargo, salario, horario_trabajo, id_alojamiento)
VALUES (4, 'José Ramírez Torres', 'Vigilante', 'VIGILANCIA', 700000, '24 horas', 2);

INSERT INTO Personal (id_personal, nombre, funcion, cargo, salario, horario_trabajo, id_alojamiento)
VALUES (5, 'Paula González Hernández', 'Recepcionista', 'RECEPCIONISTA', 700000, 'Jueves a Domingo 09:00-17:00', 3);

INSERT INTO Personal (id_personal, nombre, funcion, cargo, salario, horario_trabajo, id_alojamiento)
VALUES (6, 'Ricardo Díaz Villalobos', 'Mozo', 'MOSO', 600000, 'Lunes a Viernes 10:00-18:00', 3);

-- =====================================================================
-- 13. SERVICIO
-- =====================================================================
INSERT INTO Servicio (id_servicio, nombre_servicio, tipo_servicio, hora_apertura, hora_cierre, uso_exclusivo, id_alojamiento)
VALUES (1, 'Piscina', 'PISCINA', '08:00:00', '20:00:00', 'N', 1);

INSERT INTO Servicio (id_servicio, nombre_servicio, tipo_servicio, hora_apertura, hora_cierre, uso_exclusivo, id_alojamiento)
VALUES (2, 'Spa', 'SPA', '09:00:00', '21:00:00', 'S', 1);

INSERT INTO Servicio (id_servicio, nombre_servicio, tipo_servicio, hora_apertura, hora_cierre, uso_exclusivo, id_alojamiento)
VALUES (3, 'Jacuzzi', 'JACUZZI', '10:00:00', '22:00:00', 'S', 1);

INSERT INTO Servicio (id_servicio, nombre_servicio, tipo_servicio, hora_apertura, hora_cierre, uso_exclusivo, id_alojamiento)
VALUES (4, 'Salón de eventos', 'CENTRO_EVENTOS', '08:00:00', '23:00:00', 'N', 1);

INSERT INTO Servicio (id_servicio, nombre_servicio, tipo_servicio, hora_apertura, hora_cierre, uso_exclusivo, id_alojamiento)
VALUES (5, 'Cancha de croquetas', 'CANCHA', '09:00:00', '18:00:00', 'N', 2);

INSERT INTO Servicio (id_servicio, nombre_servicio, tipo_servicio, hora_apertura, hora_cierre, uso_exclusivo, id_alojamiento)
VALUES (6, 'Servicios a la habitación', 'SERVICIO_HABITACION', '08:00:00', '21:00:00', 'N', 1);

INSERT INTO Servicio (id_servicio, nombre_servicio, tipo_servicio, hora_apertura, hora_cierre, uso_exclusivo, id_alojamiento)
VALUES (7, 'Minibar', 'PISCINA', '00:00:00', '23:59:59', 'N', 3);

-- =====================================================================
-- 14. USUARIO_SISTEMA (continuación, para alojamiento 2 y 3)
-- =====================================================================
INSERT INTO Usuario_Sistema (id_usuario, nombre_usuario, contrasena_hash, estado, id_rol, id_personal)
VALUES (4, 'recepcion_circasia', 'hash_recepcion_b3c4d5e6f7a8', 'ACTIVO', 2, 3);

INSERT INTO Usuario_Sistema (id_usuario, nombre_usuario, contrasena_hash, estado, id_rol, id_personal)
VALUES (5, 'recepcion_calarca', 'hash_recepcion_c5d6e7f8a9b0', 'ACTIVO', 2, 4);

INSERT INTO Usuario_Sistema (id_usuario, nombre_usuario, contrasena_hash, estado, id_rol, id_personal)
VALUES (6, 'admin_hotel_3', 'hash_admin_9a8b7c6d5e4f', 'ACTIVO', 1, 5);

-- =====================================================================
-- 15. RESERVA
-- =====================================================================
INSERT INTO Reserva (id_reserva, estado, fecha_creacion, hora_estimada_llegada, fecha_limite_confirmacion, canal_directo, precio_total, id_plan, id_titular, id_intermediario)
VALUES (1001, 'PENDIENTE', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP + INTERVAL '48' HOUR, 'N', NULL, 1, 4, 1);

INSERT INTO Reserva (id_reserva, estado, fecha_creacion, hora_estimada_llegada, fecha_limite_confirmacion, canal_directo, precio_total, id_plan, id_titular, id_intermediario)
VALUES (1002, 'CONFIRMADA', CURRENT_TIMESTAMP, TO_TIMESTAMP('2026-02-10 15:00:00', 'YYYY-MM-DD HH24:MI:SS'), CURRENT_TIMESTAMP + INTERVAL '72' HOUR, 'S', 750000, 2, 1, NULL);

INSERT INTO Reserva (id_reserva, estado, fecha_creacion, hora_estimada_llegada, fecha_limite_confirmacion, canal_directo, precio_total, id_plan, id_titular, id_intermediario)
VALUES (1003, 'CANCELADA', CURRENT_TIMESTAMP, TO_TIMESTAMP('2026-03-01 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), CURRENT_TIMESTAMP + INTERVAL '24' HOUR, 'N', 1200000, 3, 2, 2);

INSERT INTO Reserva (id_reserva, estado, fecha_creacion, hora_estimada_llegada, fecha_limite_confirmacion, canal_directo, precio_total, id_plan, id_titular, id_intermediario)
VALUES (1004, 'CONFIRMADA', CURRENT_TIMESTAMP, TO_TIMESTAMP('2026-04-05 16:00:00', 'YYYY-MM-DD HH24:MI:SS'), CURRENT_TIMESTAMP + INTERVAL '48' HOUR, 'S', 900000, 2, 3, NULL);

INSERT INTO Reserva (id_reserva, estado, fecha_creacion, hora_estimada_llegada, fecha_limite_confirmacion, canal_directo, precio_total, id_plan, id_titular, id_intermediario)
VALUES (1005, 'PENDIENTE', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP + INTERVAL '24' HOUR, 'N', NULL, 1, 4, 3);

INSERT INTO Reserva (id_reserva, estado, fecha_creacion, hora_estimada_llegada, fecha_limite_confirmacion, canal_directo, precio_total, id_plan, id_titular, id_intermediario)
VALUES (1006, 'CONFIRMADA', CURRENT_TIMESTAMP, TO_TIMESTAMP('2026-05-15 15:00:00', 'YYYY-MM-DD HH24:MI:SS'), CURRENT_TIMESTAMP + INTERVAL '72' HOUR, 'S', 680000, 2, 5, NULL);

-- =====================================================================
-- 16. RESERVA_HABITACION
-- =====================================================================
INSERT INTO Reserva_Habitacion (id_reserva_habitacion, fecha_entrada_reservada, fecha_salida_reservada, precio_noche_pactado, checkin_real, checkout_real, noches_adicionales, id_reserva, id_habitacion)
VALUES (1, TO_DATE('2026-02-10', 'YYYY-MM-DD'), TO_DATE('2026-02-13', 'YYYY-MM-DD'), 320000, TO_TIMESTAMP('2026-02-10 15:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2026-02-12 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 0, 1002, 2);

INSERT INTO Reserva_Habitacion (id_reserva_habitacion, fecha_entrada_reservada, fecha_salida_reservada, precio_noche_pactado, checkin_real, checkout_real, noches_adicionales, id_reserva, id_habitacion)
VALUES (2, TO_DATE('2026-02-10', 'YYYY-MM-DD'), TO_DATE('2026-02-13', 'YYYY-MM-DD'), 320000, TO_TIMESTAMP('2026-02-10 15:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2026-02-12 13:00:00', 'YYYY-MM-DD HH24:MI:SS'), 0, 1002, 3);

INSERT INTO Reserva_Habitacion (id_reserva_habitacion, fecha_entrada_reservada, fecha_salida_reservada, precio_noche_pactado, checkin_real, checkout_real, noches_adicionales, id_reserva, id_habitacion)
VALUES (3, TO_DATE('2026-03-01', 'YYYY-MM-DD'), TO_DATE('2026-03-05', 'YYYY-MM-DD'), 550000, TO_TIMESTAMP('2026-03-01 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2026-03-04 11:00:00', 'YYYY-MM-DD HH24:MI:SS'), 0, 1003, 7);

INSERT INTO Reserva_Habitacion (id_reserva_habitacion, fecha_entrada_reservada, fecha_salida_reservada, precio_noche_pactado, checkin_real, checkout_real, noches_adicionales, id_reserva, id_habitacion)
VALUES (4, TO_DATE('2026-04-05', 'YYYY-MM-DD'), TO_DATE('2026-04-08', 'YYYY-MM-DD'), 550000, TO_TIMESTAMP('2026-04-05 16:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2026-04-07 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 0, 1004, 8);

INSERT INTO Reserva_Habitacion (id_reserva_habitacion, fecha_entrada_reservada, fecha_salida_reservada, precio_noche_pactado, checkin_real, checkout_real, noches_adicionales, id_reserva, id_habitacion)
VALUES (5, TO_DATE('2026-05-15', 'YYYY-MM-DD'), TO_DATE('2026-05-18', 'YYYY-MM-DD'), 240000, TO_TIMESTAMP('2026-05-15 15:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2026-05-17 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 0, 1006, 9);

-- =====================================================================
-- 17. OCUPACION
-- =====================================================================
INSERT INTO Ocupacion (id_reserva_habitacion, id_huesped, rol)
VALUES (1, 1, 'TITULAR');

INSERT INTO Ocupacion (id_reserva_habitacion, id_huesped, rol)
VALUES (1, 4, 'ACOMPANANTE');

INSERT INTO Ocupacion (id_reserva_habitacion, id_huesped, rol)
VALUES (2, 1, 'TITULAR');

INSERT INTO Ocupacion (id_reserva_habitacion, id_huesped, rol)
VALUES (2, 4, 'ACOMPANANTE');

INSERT INTO Ocupacion (id_reserva_habitacion, id_huesped, rol)
VALUES (3, 2, 'TITULAR');

INSERT INTO Ocupacion (id_reserva_habitacion, id_huesped, rol)
VALUES (3, 5, 'ACOMPANANTE');

INSERT INTO Ocupacion (id_reserva_habitacion, id_huesped, rol)
VALUES (4, 3, 'TITULAR');

INSERT INTO Ocupacion (id_reserva_habitacion, id_huesped, rol)
VALUES (5, 5, 'TITULAR');

-- =====================================================================
-- 18. FOLIO
-- =====================================================================
INSERT INTO Folio (id_reserva, fecha_apertura, fecha_cierre, estado)
VALUES (1001, CURRENT_TIMESTAMP, NULL, 'ABIERTO');

INSERT INTO Folio (id_reserva, fecha_apertura, fecha_cierre, estado)
VALUES (1002, CURRENT_TIMESTAMP, TO_TIMESTAMP('2026-02-13 13:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'ABIERTO');

INSERT INTO Folio (id_reserva, fecha_apertura, fecha_cierre, estado)
VALUES (1003, CURRENT_TIMESTAMP, TO_TIMESTAMP('2026-02-12 22:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'CERRADO');

INSERT INTO Folio (id_reserva, fecha_apertura, fecha_cierre, estado)
VALUES (1004, CURRENT_TIMESTAMP, NULL, 'ABIERTO');

INSERT INTO Folio (id_reserva, fecha_apertura, fecha_cierre, estado)
VALUES (1005, CURRENT_TIMESTAMP, NULL, 'ABIERTO');

INSERT INTO Folio (id_reserva, fecha_apertura, fecha_cierre, estado)
VALUES (1006, CURRENT_TIMESTAMP, NULL, 'ABIERTO');

-- =====================================================================
-- 19. PAGO
-- =====================================================================
INSERT INTO Pago (id_pago, monto, medio_pago, fecha_pago, id_reserva)
VALUES (1, 400000, 'TARJETA DE CREDITO', CURRENT_TIMESTAMP, 1002);

INSERT INTO Pago (id_pago, monto, medio_pago, fecha_pago, id_reserva)
VALUES (2, 350000, 'TARJETA DE DEBITO', CURRENT_TIMESTAMP, 1002);

INSERT INTO Pago (id_pago, monto, medio_pago, fecha_pago, id_reserva)
VALUES (3, 600000, 'TRANSFERENCIA', CURRENT_TIMESTAMP, 1003);

INSERT INTO Pago (id_pago, monto, medio_pago, fecha_pago, id_reserva)
VALUES (4, 300000, 'TARJETA DE CREDITO', CURRENT_TIMESTAMP, 1004);

INSERT INTO Pago (id_pago, monto, medio_pago, fecha_pago, id_reserva)
VALUES (5, 380000, 'EFECTIVO', CURRENT_TIMESTAMP, 1004);

INSERT INTO Pago (id_pago, monto, medio_pago, fecha_pago, id_reserva)
VALUES (6, 200000, 'TARJETA DE DEBITO', CURRENT_TIMESTAMP, 1006);

-- =====================================================================
-- 20. PERSONAL (sección 20 - Uso Servicio)
-- =====================================================================
INSERT INTO Personal (id_personal, nombre, funcion, cargo, salario, horario_trabajo, id_alojamiento)
VALUES (7, 'Diana Gutiérrez Gutiérrez', 'Acompañante de servicio', 'COORDINADORA', 720000, 'Lunes a Viernes 08:00-16:00', 1);

INSERT INTO Personal (id_personal, nombre, funcion, cargo, salario, horario_trabajo, id_alojamiento)
VALUES (8, 'Hector Morales Flórez', 'Maintenance', 'MANTENIMIENTO', 680000, 'Lunes a Sábado 07:00-15:00', 2);

INSERT INTO Personal (id_personal, nombre, funcion, cargo, salario, horario_trabajo, id_alojamiento)
VALUES (9, 'Lucía Ramírez Aliaga', 'Recepcionista', 'RECEPCIONISTA', 710000, 'Lunes a Viernes 09:00-17:00', 1);

-- =====================================================================
-- 21. SERVICIO (sección 21 - Uso Servicio)
-- =====================================================================
INSERT INTO Servicio (id_servicio, nombre_servicio, tipo_servicio, hora_apertura, hora_cierre, uso_exclusivo, id_alojamiento)
VALUES (8, 'Baño de vapor', 'SAUNA', '08:00:00', '21:00:00', 'S', 1);

INSERT INTO Servicio (id_servicio, nombre_servicio, tipo_servicio, hora_apertura, hora_cierre, uso_exclusivo, id_alojamiento)
VALUES (9, 'Waiter service', 'SERVICIO_HABITACION', '00:00:00', '23:59:59', 'N', 3);

-- =====================================================================
-- 22. USO_SERVICIO
-- =====================================================================
INSERT INTO Uso_Servicio (id_uso_servicio, fecha_hora_inicio, fecha_hora_fin, solicitud, id_reserva_habitacion, id_servicio, id_personal)
VALUES (1, TO_TIMESTAMP('2026-02-10 16:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2026-02-10 17:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'Desayuno en la habitación', 1, 6, 2);

INSERT INTO Uso_Servicio (id_uso_servicio, fecha_hora_inicio, fecha_hora_fin, solicitud, id_reserva_habitacion, id_servicio, id_personal)
VALUES (2, TO_TIMESTAMP('2026-04-05 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2026-04-06 15:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'Piscina y spa', 4, 1, 1);

INSERT INTO Uso_Servicio (id_uso_servicio, fecha_hora_inicio, fecha_hora_fin, solicitud, id_reserva_habitacion, id_servicio, id_personal)
VALUES (3, TO_TIMESTAMP('2026-04-05 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2026-04-05 17:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'Spa y jacuzzi', 4, 2, 1);

INSERT INTO Uso_Servicio (id_uso_servicio, fecha_hora_inicio, fecha_hora_fin, solicitud, id_reserva_habitacion, id_servicio, id_personal)
VALUES (4, TO_TIMESTAMP('2026-04-06 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2026-04-06 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'Servicios a la habitación', 4, 6, 8);

INSERT INTO Uso_Servicio (id_uso_servicio, fecha_hora_inicio, fecha_hora_fin, solicitud, id_reserva_habitacion, id_servicio, id_personal)
VALUES (5, TO_TIMESTAMP('2026-02-11 08:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2026-02-11 09:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'Bloqueo de uso', 1, 8, 7);

-- =====================================================================
-- 23. CALIFICACION
-- =====================================================================
INSERT INTO Calificacion (id_calificacion, puntuacion, comentario, fecha_calificacion, id_huesped, id_reserva)
VALUES (1, 5, 'Excelente servicio, la suite fue muy linda y el personal increíble', CURRENT_TIMESTAMP, 1, 1002);

INSERT INTO Calificacion (id_calificacion, puntuacion, comentario, fecha_calificacion, id_huesped, id_reserva)
VALUES (2, 3, 'Un buen hotel, aunque el transporte desde Armenia puede ser incómodo', CURRENT_TIMESTAMP, 2, 1003);

INSERT INTO Calificacion (id_calificacion, puntuacion, comentario, fecha_calificacion, id_huesped, id_reserva)
VALUES (3, 4, 'Muy agradable, la cabaña tenía buenas condiciones para el fin de semana', CURRENT_TIMESTAMP, 3, 1004);

INSERT INTO Calificacion (id_calificacion, puntuacion, comentario, fecha_calificacion, id_huesped, id_reserva)
VALUES (4, 5, 'Perfecto para familia, el servicio fue rápido y eficiente', CURRENT_TIMESTAMP, 5, 1006);

-- =====================================================================
-- 24. CONFLICTO_CANAL
-- =====================================================================
INSERT INTO Conflicto_Canal (id_conflicto, fecha_registro, fecha_entrada_solicitada, fecha_salida_solicitada, estado_revision, observacion, id_intermediario, id_habitacion, id_reserva, id_usuario)
VALUES (1, CURRENT_TIMESTAMP, TO_DATE('2026-06-01', 'YYYY-MM-DD'), TO_DATE('2026-06-04', 'YYYY-MM-DD'), 'PENDIENTE', 'Reserva externa colisionando con habitación 7', 2, 7, 1005, NULL);

-- =====================================================================
-- 25. BLOQUEO
-- =====================================================================
INSERT INTO Bloqueo (id_bloqueo, fecha_inicio, fecha_fin, motivo, id_habitacion, id_usuario)
VALUES (1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '3' DAY, 'Limpieza general', 1, 1);

INSERT INTO Bloqueo (id_bloqueo, fecha_inicio, fecha_fin, motivo, id_habitacion, id_usuario)
VALUES (2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1' DAY, 'Freír en la cocina', 2, 2);

-- =====================================================================
-- 26. NOTIFICACION
-- =====================================================================
INSERT INTO Notificacion (id_notificacion, tipo_notificacion, mensaje, fecha_envio, estado_envio, id_huesped, id_reserva)
VALUES (1, 'RECHAZO', 'Su solicitud de reserva fue rechazada: colisión con habitación reservada', CURRENT_TIMESTAMP, 'FALLIDA', 5, 1005);

INSERT INTO Notificacion (id_notificacion, tipo_notificacion, mensaje, fecha_envio, estado_envio, id_huesped, id_reserva)
VALUES (2, 'CONFIRMACION', 'Su reserva ha sido confirmada exitosamente', CURRENT_TIMESTAMP, 'ENVIADA', 1, 1002);

INSERT INTO Notificacion (id_notificacion, tipo_notificacion, mensaje, fecha_envio, estado_envio, id_huesped, id_reserva)
VALUES (3, 'RECORDATORIO', 'Recuerde confirmar su reserva antes del plazo límite', CURRENT_TIMESTAMP, 'ENVIADA', 1, 1001);

INSERT INTO Notificacion (id_notificacion, tipo_notificacion, mensaje, fecha_envio, estado_envio, id_huesped, id_reserva)
VALUES (4, 'NO_SHOW', 'La reserva no ha sido confirmada hasta la fecha de entrada', CURRENT_TIMESTAMP, 'ENVIADA', 2, 1003);

-- =====================================================================
-- 27. CALIFICACION (sección 27 - cont.)
-- =====================================================================
INSERT INTO Calificacion (id_calificacion, puntuacion, comentario, fecha_calificacion, id_huesped, id_reserva)
VALUES (5, 4, 'Buena estancia, el pago fue sencillo', NULL, 4, 1005);

-- =====================================================================
-- 28. REPORTE
-- =====================================================================
INSERT INTO Reporte (id_reporte, nombre_reporte, nivel, pregunta_negocio)
VALUES (1, 'Catalogo de habitaciones', 'SIMPLE', '¿Cuáles son las habitaciones disponibles en cada alojamiento?');

INSERT INTO Reporte (id_reporte, nombre_reporte, nivel, pregunta_negocio)
VALUES (2, 'Habitaciones nunca reservadas', 'SIMPLE', '¿Qué habitaciones no han tenido reservas?');

INSERT INTO Reporte (id_reporte, nombre_reporte, nivel, pregunta_negocio)
VALUES (3, 'Tarifario vigente', 'SIMPLE', '¿Cuáles son los precios vigentes por temporada?');

INSERT INTO Reporte (id_reporte, nombre_reporte, nivel, pregunta_negocio)
VALUES (4, 'Ingresos por temporada', 'INTERMEDIO', '¿Cuántas noches vendidas hubo por temporada?');

INSERT INTO Reporte (id_reporte, nombre_reporte, nivel, pregunta_negocio)
VALUES (5, 'Reservas y comisiones', 'INTERMEDIO', '¿Cuántas reservas aporta cada intermediario?');

INSERT INTO Reporte (id_reporte, nombre_reporte, nivel, pregunta_negocio)
VALUES (6, 'Estado de folios', 'INTERMEDIO', '¿Qué reservas tienen saldo pendiente?');

INSERT INTO Reporte (id_reporte, nombre_reporte, nivel, pregunta_negocio)
VALUES (7, 'Uso de servicios', 'INTERMEDIO', '¿Qué servicios tienen más uso?');

INSERT INTO Reporte (id_reporte, nombre_reporte, nivel, pregunta_negocio)
VALUES (8, 'Cancelaciones y no-show', 'AVANZADO', '¿Cómo evoluciona la cancelación por mes?');

INSERT INTO Reporte (id_reporte, nombre_reporte, nivel, pregunta_negocio)
VALUES (9, 'Análisis integral cancelaciones', 'AVANZADO', '¿Cuál es la tasa de cancelación por canal?');

INSERT INTO Reporte (id_reporte, nombre_reporte, nivel, pregunta_negocio)
VALUES (10, 'Segmentación RFM', 'AVANZADO', '¿Quiénes son los huéspedes VIP?');

-- =====================================================================
-- 29. REPORTE_GENERADO
-- =====================================================================
INSERT INTO Reporte_Generado (id_reporte_generado, fecha_generacion, formato, id_reporte, id_usuario)
VALUES (1, CURRENT_TIMESTAMP, 'PDF', 1, 1);

INSERT INTO Reporte_Generado (id_reporte_generado, fecha_generacion, formato, id_reporte, id_usuario)
VALUES (2, CURRENT_TIMESTAMP, 'PDF', 4, 1);

INSERT INTO Reporte_Generado (id_reporte_generado, fecha_generacion, formato, id_reporte, id_usuario)
VALUES (3, CURRENT_TIMESTAMP, 'PDF', 7, 1);

INSERT INTO Reporte_Generado (id_reporte_generado, fecha_generacion, formato, id_reporte, id_usuario)
VALUES (4, CURRENT_TIMESTAMP, 'PDF', 10, 1);

-- =====================================================================
-- 30. PERFIL_CLIENTE
-- =====================================================================
INSERT INTO Perfil_Cliente (id_perfil_cliente, viaja_con_mascotas, desea_desayuno, observaciones, id_huesped, id_tipo_habitacion)
VALUES (1, 'S', 'S', 'Prefiere habitación tranquila, lleva perro', 1, 1);

INSERT INTO Perfil_Cliente (id_perfil_cliente, viaja_con_mascotas, desea_desayuno, observaciones, id_huesped, id_tipo_habitacion)
VALUES (2, 'N', 'S', 'Prefiere desayuno incluido', 2, 2);

INSERT INTO Perfil_Cliente (id_perfil_cliente, viaja_con_mascotas, desea_desayuno, observaciones, id_huesped, id_tipo_habitacion)
VALUES (3, 'N', 'N', NULL, 3, 3);

INSERT INTO Perfil_Cliente (id_perfil_cliente, viaja_con_mascotas, desea_desayuno, observaciones, id_huesped, id_tipo_habitacion)
VALUES (4, 'N', 'S', 'Requiere habitación adaptable', 4, 4);

-- =====================================================================
-- FIN DEL SCRIPT DML
-- =====================================================================
