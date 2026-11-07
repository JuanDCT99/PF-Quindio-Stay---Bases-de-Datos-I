-- =====================================================================
--  DDL - CREACIÓN DE TABLAS
--  Script: QuindíoStay
--  Dominio: Reservas Hoteleras
--  Materia: Bases de Datos I - Universidad del Quindío
--  Equipo: Juan David Cardozo Torres · Camilo Ruiz López · Erwin Harder Garzón
--  Versión: 1.0 (para la Entrega 3)
--
--  Descripción:
--  Este script contiene las sentencias DDL (Data Definition Language)
--  para crear todas las tablas del modelo relacional normalizado a
--  3FN/BCNF (según la Entrega 2).
--
--  Notas de diseño:
--  - Nomenclatura en snake_case para coincidir con el esquema del documento.
--  - PK: PRIMARY KEY (o CONSTRAINT).
|--  - FK: FOREIGN KEY. NOTA: Oracle aplica RESTRICT de forma implícita;
|--    solo se usan CASCADE y SET NULL en las cláusulas de eliminación.
--  - CHECK: restricciones de dominio y consistencia.
--  - UNIQUE: restricciones de unicidad.
--  - Tablas observadas: Alojamiento, Tipo_Habitacion, Habitacion,
--    Temporada, Tarifa, Intermediario, Huesped, Reserva, Reserva_Habitacion,
--    Ocupacion, Personal, Rol, Usuario_Sistema, Servicio, Uso_Servicio,
--    Calificacion, Folio, Pago, Sancion, Bloqueo, Conflicto_Canal,
--    Reporte, Reporte_Generado, Notificacion, Municipio, Plan, Perfil_Cliente.
-- =====================================================================

-- ============================
-- 1. ALOJAMIENTO
-- ============================
CREATE TABLE Alojamiento (
    id_alojamiento NUMBER(10) NOT NULL CONSTRAINT pk_alojamiento PRIMARY KEY,
    nombre_alojamiento VARCHAR2(120) NOT NULL,
    nit VARCHAR2(20) NOT NULL,
    direccion VARCHAR2(200) NOT NULL,
    telefono VARCHAR2(25) NOT NULL,
    acepta_mascotas CHAR(1) NOT NULL CHECK (acepta_mascotas IN ('S', 'N')),
    hora_salida_limite TIMESTAMP NOT NULL,
    plazo_confirmacion_horas NUMBER(3) NOT NULL CHECK (plazo_confirmacion_horas > 0)
);

-- ============================
-- 2. TIPO_HABITACION
-- ============================
CREATE TABLE Tipo_Habitacion (
    id_tipo_habitacion NUMBER(10) NOT NULL CONSTRAINT pk_tipo_habitacion PRIMARY KEY,
    nombre_tipo VARCHAR2(60) NOT NULL,
    descripcion VARCHAR2(300) NULL,
    capacidad_estandar NUMBER(3) NOT NULL
);

-- ============================
-- 3. HABITACION
-- ============================
CREATE TABLE Habitacion (
    id_habitacion NUMBER(10) NOT NULL CONSTRAINT pk_habitacion PRIMARY KEY,
    numero_habitacion VARCHAR2(20) NOT NULL UNIQUE,
    capacidad_maxima NUMBER(3) NOT NULL CHECK (capacidad_maxima > 0),
    descripcion VARCHAR2(500) NULL,
    estado_operativo VARCHAR2(20) NOT NULL,
    id_tipo_habitacion NUMBER(10) NOT NULL,
    id_alojamiento NUMBER(10) NOT NULL,
    CONSTRAINT fk_tipo_habitacion FOREIGN KEY (id_tipo_habitacion) REFERENCES Tipo_Habitacion (id_tipo_habitacion),
    CONSTRAINT fk_alojamiento FOREIGN KEY (id_alojamiento) REFERENCES Alojamiento (id_alojamiento)
);

-- ============================
-- 4. TEMPORADA
-- ============================
CREATE TABLE Temporada (
    id_temporada NUMBER(10) NOT NULL CONSTRAINT pk_temporada PRIMARY KEY,
    nombre_temporada VARCHAR2(20) NOT NULL CHECK (nombre_temporada IN ('ALTA', 'MEDIA', 'BAJA')),
    anio NUMBER(4) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    CONSTRAINT chk_fechas_temporada CHECK (fecha_fin >= fecha_inicio)
);

-- ============================
-- 5. TARIFA
-- ============================
CREATE TABLE Tarifa (
    id_tarifa NUMBER(10) NOT NULL CONSTRAINT pk_tarifa PRIMARY KEY,
    precio_noche NUMBER(12,2) NOT NULL CHECK (precio_noche > 0),
    id_tipo_habitacion NUMBER(10) NOT NULL,
    id_temporada NUMBER(10) NOT NULL,
    CONSTRAINT fk_tipo_habitacion FOREIGN KEY (id_tipo_habitacion) REFERENCES Tipo_Habitacion (id_tipo_habitacion),
    CONSTRAINT fk_temporada FOREIGN KEY (id_temporada) REFERENCES Temporada (id_temporada) ON DELETE CASCADE,
    CONSTRAINT uq_tipo_temporada UNIQUE (id_tipo_habitacion, id_temporada)
);

-- ============================
-- 6. INTERMEDIARIO
-- ============================
CREATE TABLE Intermediario (
    id_intermediario NUMBER(10) NOT NULL CONSTRAINT pk_intermediario PRIMARY KEY,
    nombre_intermediario VARCHAR2(120) NOT NULL,
    porcentaje_comision NUMBER(5,2) NOT NULL CHECK (porcentaje_comision BETWEEN 0 AND 30),
    tipo_canal VARCHAR2(30) NOT NULL,
    email_contacto VARCHAR2(120) NULL
);

-- ============================
-- 7. HUESPED
-- ============================
CREATE TABLE Huesped (
    id_huesped NUMBER(10) NOT NULL CONSTRAINT pk_huesped PRIMARY KEY,
    nombre VARCHAR2(80) NOT NULL,
    documento VARCHAR2(25) NOT NULL UNIQUE,
    fecha_nacimiento DATE NOT NULL,
    direccion VARCHAR2(200) NULL,
    correo VARCHAR2(120) NULL,
    telefono VARCHAR2(25) NULL
);

-- ============================
-- 8. RESERVA
-- ============================
CREATE TABLE Reserva (
    id_reserva NUMBER(10) NOT NULL CONSTRAINT pk_reserva PRIMARY KEY,
    estado VARCHAR2(20) NOT NULL CHECK (estado IN ('PENDIENTE', 'CONFIRMADA', 'CANCELADA', 'NO_SHOW')),
    fecha_creacion TIMESTAMP NOT NULL,
    hora_estimada_llegada TIMESTAMP NULL,
    fecha_limite_confirmacion TIMESTAMP NOT NULL,
    canal_directo CHAR(1) NOT NULL CHECK (canal_directo IN ('S','N')),
    precio_total NUMBER(12,2) NULL,
    id_plan NUMBER(10) NOT NULL,
    id_titular NUMBER(10) NOT NULL,
    id_intermediario NUMBER(10) NULL,
    CONSTRAINT fk_titular FOREIGN KEY (id_titular) REFERENCES Huesped (id_huesped),
    CONSTRAINT fk_intermediario FOREIGN KEY (id_intermediario) REFERENCES Intermediario (id_intermediario),
    CONSTRAINT fk_plan FOREIGN KEY (id_plan) REFERENCES Plan (id_plan)
);

-- ============================
-- 9. RESERVA_HABITACION
-- ============================
CREATE TABLE Reserva_Habitacion (
    id_reserva_habitacion NUMBER(10) NOT NULL CONSTRAINT pk_reserva_habitacion PRIMARY KEY,
    fecha_entrada_reservada DATE NOT NULL,
    fecha_salida_reservada DATE NOT NULL,
    precio_noche_pactado NUMBER(12,2) NOT NULL CHECK (precio_noche_pactado > 0),
    checkin_real TIMESTAMP NULL,
    checkout_real TIMESTAMP NULL,
    noches_adicionales NUMBER(3) NOT NULL CHECK (noches_adicionales >= 0),
    id_reserva NUMBER(10) NOT NULL,
    id_habitacion NUMBER(10) NOT NULL,
    CONSTRAINT fk_reserva FOREIGN KEY (id_reserva) REFERENCES Reserva (id_reserva) ON DELETE CASCADE,
    CONSTRAINT fk_habitacion FOREIGN KEY (id_habitacion) REFERENCES Habitacion (id_habitacion),
    CONSTRAINT chk_fechas_reserva CHECK (fecha_salida_reservada >= fecha_entrada_reservada)
);

-- ============================
-- 10. OCUPACION
-- ============================
CREATE TABLE Ocupacion (
    id_reserva_habitacion NUMBER(10) NOT NULL,
    id_huesped NUMBER(10) NOT NULL,
    rol VARCHAR2(20) NOT NULL CHECK (rol IN ('TITULAR','ACOMPANANTE')),
    CONSTRAINT pk_ocupacion PRIMARY KEY (id_reserva_habitacion, id_huesped),
    CONSTRAINT fk_reserva_habitacion FOREIGN KEY (id_reserva_habitacion) REFERENCES Reserva_Habitacion (id_reserva_habitacion) ON DELETE CASCADE,
    CONSTRAINT fk_huesped FOREIGN KEY (id_huesped) REFERENCES Huesped (id_huesped)
);

-- ============================
-- 11. PERSONAL
-- ============================
CREATE TABLE Personal (
    id_personal NUMBER(10) NOT NULL CONSTRAINT pk_personal PRIMARY KEY,
    nombre VARCHAR2(120) NOT NULL,
    funcion VARCHAR2(100) NOT NULL,
    cargo VARCHAR2(60) NOT NULL,
    salario NUMBER(12,2) NOT NULL CHECK (salario >= 0),
    horario_trabajo VARCHAR2(100) NOT NULL,
    id_alojamiento NUMBER(10) NOT NULL,
    CONSTRAINT fk_alojamiento FOREIGN KEY (id_alojamiento) REFERENCES Alojamiento (id_alojamiento)
);

-- ============================
-- 12. ROL
-- ============================
CREATE TABLE Rol (
    id_rol NUMBER(10) NOT NULL CONSTRAINT pk_rol PRIMARY KEY,
    nombre_rol VARCHAR2(30) NOT NULL UNIQUE,
    descripcion VARCHAR2(200) NULL
);

-- ============================
-- 13. USUARIO_SISTEMA
-- ============================
CREATE TABLE Usuario_Sistema (
    id_usuario NUMBER(10) NOT NULL CONSTRAINT pk_usuario_sistema PRIMARY KEY,
    nombre_usuario VARCHAR2(50) NOT NULL UNIQUE,
    contrasena_hash VARCHAR2(255) NOT NULL,
    estado VARCHAR2(15) NOT NULL CHECK (estado IN ('ACTIVO','INACTIVO','BLOQUEADO')),
    id_rol NUMBER(10) NOT NULL,
    id_personal NUMBER(10) NULL,
    CONSTRAINT fk_rol FOREIGN KEY (id_rol) REFERENCES Rol (id_rol),
    CONSTRAINT fk_personal FOREIGN KEY (id_personal) REFERENCES Personal (id_personal)
);

-- ============================
-- 14. SERVICIO
-- ============================
CREATE TABLE Servicio (
    id_servicio NUMBER(10) NOT NULL CONSTRAINT pk_servicio PRIMARY KEY,
    nombre_servicio VARCHAR2(100) NOT NULL,
    tipo_servicio VARCHAR2(30) NOT NULL,
    hora_apertura TIMESTAMP NULL,
    hora_cierre TIMESTAMP NULL,
    uso_exclusivo CHAR(1) NOT NULL CHECK (uso_exclusivo IN ('S','N')),
    id_alojamiento NUMBER(10) NOT NULL,
    CONSTRAINT fk_alojamiento FOREIGN KEY (id_alojamiento) REFERENCES Alojamiento (id_alojamiento)
);

-- ============================
-- 15. USO_SERVICIO
-- ============================
CREATE TABLE Uso_Servicio (
    id_uso_servicio NUMBER(10) NOT NULL CONSTRAINT pk_uso_servicio PRIMARY KEY,
    fecha_hora_inicio TIMESTAMP NOT NULL,
    fecha_hora_fin TIMESTAMP NOT NULL,
    solicitud VARCHAR2(500) NULL,
    id_reserva_habitacion NUMBER(10) NOT NULL,
    id_servicio NUMBER(10) NOT NULL,
    id_personal NUMBER(10) NOT NULL,
    CONSTRAINT fk_reserva_habitacion FOREIGN KEY (id_reserva_habitacion) REFERENCES Reserva_Habitacion (id_reserva_habitacion) ON DELETE CASCADE,
    CONSTRAINT fk_servicio FOREIGN KEY (id_servicio) REFERENCES Servicio (id_servicio),
    CONSTRAINT fk_personal FOREIGN KEY (id_personal) REFERENCES Personal (id_personal),
    CONSTRAINT chk_fechas_uso_servicio CHECK (fecha_hora_fin >= fecha_hora_inicio)
);

-- ============================
-- 16. CALIFICACION
-- ============================
CREATE TABLE Calificacion (
    id_calificacion NUMBER(10) NOT NULL CONSTRAINT pk_calificacion PRIMARY KEY,
    puntuacion NUMBER(2) NOT NULL,
    comentario VARCHAR2(1000) NULL,
    fecha_calificacion TIMESTAMP NOT NULL,
    id_huesped NUMBER(10) NOT NULL,
    id_reserva NUMBER(10) NOT NULL,
    CONSTRAINT fk_huesped FOREIGN KEY (id_huesped) REFERENCES Huesped (id_huesped),
    CONSTRAINT fk_reserva FOREIGN KEY (id_reserva) REFERENCES Reserva (id_reserva) ON DELETE CASCADE
);

-- ============================
-- 17. FOLIO
-- ============================
CREATE TABLE Folio (
    id_reserva NUMBER(10) NOT NULL CONSTRAINT pk_folio PRIMARY KEY,
    fecha_apertura TIMESTAMP NOT NULL,
    fecha_cierre TIMESTAMP NULL,
    estado VARCHAR2(10) NOT NULL CHECK (estado IN ('ABIERTO','CERRADO')),
    CONSTRAINT chk_fechas_folio CHECK (fecha_cierre >= fecha_apertura)
);

-- ============================
-- 18. PAGO
-- ============================
CREATE TABLE Pago (
    id_pago NUMBER(10) NOT NULL CONSTRAINT pk_pago PRIMARY KEY,
    monto NUMBER(12,2) NOT NULL CHECK (monto > 0),
    medio_pago VARCHAR2(25) NOT NULL,
    fecha_pago TIMESTAMP NOT NULL,
    id_reserva NUMBER(10) NOT NULL,
    CONSTRAINT fk_reserva FOREIGN KEY (id_reserva) REFERENCES Reserva (id_reserva) ON DELETE CASCADE
);

-- ============================
-- 19. SANCION
-- ============================
CREATE TABLE Sancion (
    id_sancion NUMBER(10) NOT NULL CONSTRAINT pk_sancion PRIMARY KEY,
    tipo_sancion VARCHAR2(30) NOT NULL,
    descripcion VARCHAR2(500) NOT NULL,
    fecha_sancion TIMESTAMP NOT NULL,
    valor NUMBER(12,2) NOT NULL CHECK (valor > 0),
    id_reserva NUMBER(10) NOT NULL,
    id_servicio NUMBER(10) NULL,
    CONSTRAINT fk_reserva FOREIGN KEY (id_reserva) REFERENCES Reserva (id_reserva) ON DELETE CASCADE,
    CONSTRAINT fk_servicio FOREIGN KEY (id_servicio) REFERENCES Servicio (id_servicio)
);

-- ============================
-- 20. BLOQUEO
-- ============================
CREATE TABLE Bloqueo (
    id_bloqueo NUMBER(10) NOT NULL CONSTRAINT pk_bloqueo PRIMARY KEY,
    fecha_inicio TIMESTAMP NOT NULL,
    fecha_fin TIMESTAMP NOT NULL,
    motivo VARCHAR2(300) NOT NULL,
    id_habitacion NUMBER(10) NOT NULL,
    id_usuario NUMBER(10) NOT NULL,
    CONSTRAINT fk_habitacion FOREIGN KEY (id_habitacion) REFERENCES Habitacion (id_habitacion) ON DELETE CASCADE,
    CONSTRAINT fk_usuario FOREIGN KEY (id_usuario) REFERENCES Usuario_Sistema (id_usuario)
);

-- ============================
-- 21. CONFLICTO_CANAL
-- ============================
CREATE TABLE Conflicto_Canal (
    id_conflicto NUMBER(10) NOT NULL CONSTRAINT pk_conflicto PRIMARY KEY,
    fecha_registro TIMESTAMP NOT NULL,
    fecha_entrada_solicitada DATE NOT NULL,
    fecha_salida_solicitada DATE NOT NULL,
    estado_revision VARCHAR2(20) NOT NULL CHECK (estado_revision IN ('PENDIENTE', 'EN_REVISION', 'RESUELTO', 'DESCARTADO')),
    observacion VARCHAR2(1000) NULL,
    id_intermediario NUMBER(10) NOT NULL,
    id_habitacion NUMBER(10) NOT NULL,
    id_reserva NUMBER(10) NOT NULL,
    id_usuario NUMBER(10) NULL,
    CONSTRAINT fk_intermediario FOREIGN KEY (id_intermediario) REFERENCES Intermediario (id_intermediario),
    CONSTRAINT fk_habitacion FOREIGN KEY (id_habitacion) REFERENCES Habitacion (id_habitacion),
    CONSTRAINT fk_reserva FOREIGN KEY (id_reserva) REFERENCES Reserva (id_reserva),
    CONSTRAINT fk_usuario FOREIGN KEY (id_usuario) REFERENCES Usuario_Sistema (id_usuario)
);

-- ============================
-- 22. REPORTE
-- ============================
CREATE TABLE Reporte (
    id_reporte NUMBER(10) NOT NULL CONSTRAINT pk_reporte PRIMARY KEY,
    nombre_reporte VARCHAR2(120) NOT NULL,
    nivel VARCHAR2(15) NOT NULL CHECK (nivel IN ('SIMPLE', 'INTERMEDIO', 'AVANZADO')),
    pregunta_negocio VARCHAR2(500) NOT NULL
);

-- ============================
-- 23. REPORTE_GENERADO
-- ============================
CREATE TABLE Reporte_Generado (
    id_reporte_generado NUMBER(10) NOT NULL CONSTRAINT pk_reporte_generado PRIMARY KEY,
    fecha_generacion TIMESTAMP NOT NULL,
    formato VARCHAR2(10) NOT NULL,
    id_reporte NUMBER(10) NOT NULL,
    id_usuario NUMBER(10) NOT NULL,
    CONSTRAINT fk_reporte FOREIGN KEY (id_reporte) REFERENCES Reporte (id_reporte),
    CONSTRAINT fk_usuario FOREIGN KEY (id_usuario) REFERENCES Usuario_Sistema (id_usuario)
);

-- ============================
-- 24. NOTIFICACION
-- ============================
CREATE TABLE Notificacion (
    id_notificacion NUMBER(10) NOT NULL CONSTRAINT pk_notificacion PRIMARY KEY,
    tipo_notificacion VARCHAR2(30) NOT NULL,
    mensaje VARCHAR2(1000) NOT NULL,
    fecha_envio TIMESTAMP NOT NULL,
    estado_envio VARCHAR2(10) NOT NULL CHECK (estado_envio IN ('ENVIADA', 'FALLIDA', 'PENDIENTE')),
    id_huesped NUMBER(10) NOT NULL,
    id_reserva NUMBER(10) NULL,
    CONSTRAINT fk_huesped FOREIGN KEY (id_huesped) REFERENCES Huesped (id_huesped),
    CONSTRAINT fk_reserva FOREIGN KEY (id_reserva) REFERENCES Reserva (id_reserva)
);

-- ============================
-- 25. MUNICIPIO
-- ============================
CREATE TABLE Municipio (
    id_municipio NUMBER(10) NOT NULL CONSTRAINT pk_municipio PRIMARY KEY,
    nombre_municipio VARCHAR2(60) NOT NULL,
    departamento VARCHAR2(60) NOT NULL
);

-- ============================
-- 26. PLAN
-- ============================
CREATE TABLE Plan (
    id_plan NUMBER(10) NOT NULL CONSTRAINT pk_plan PRIMARY KEY,
    nombre_plan VARCHAR2(120) NOT NULL,
    descripcion VARCHAR2(500) NULL,
    incluye_desayuno CHAR(1) NOT NULL CHECK (incluye_desayuno IN ('S', 'N')),
    valor_adicional_noche NUMBER(12,2) NOT NULL CHECK (valor_adicional_noche >= 0),
    id_alojamiento NUMBER(10) NOT NULL,
    CONSTRAINT fk_alojamiento FOREIGN KEY (id_alojamiento) REFERENCES Alojamiento (id_alojamiento)
);

-- ============================
-- 27. PERFIL_CLIENTE
-- ============================
CREATE TABLE Perfil_Cliente (
    id_perfil_cliente NUMBER(10) NOT NULL CONSTRAINT pk_perfil_cliente PRIMARY KEY,
    viaja_con_mascota CHAR(1) NOT NULL CHECK (viaja_con_mascota IN ('S', 'N')),
    desea_desayuno CHAR(1) NOT NULL CHECK (desea_desayuno IN ('S', 'N')),
    observaciones VARCHAR2(500) NULL,
    id_huesped NUMBER(10) NOT NULL UNIQUE,
    id_tipo_habitacion NUMBER(10) NULL,
    CONSTRAINT fk_huesped FOREIGN KEY (id_huesped) REFERENCES Huesped (id_huesped),
    CONSTRAINT fk_tipo_habitacion FOREIGN KEY (id_tipo_habitacion) REFERENCES Tipo_Habitacion (id_tipo_habitacion)
);

-- =====================================================================
--  FIN DEL SCRIPT DDL
-- =====================================================================
