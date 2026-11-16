-- =====================================================================
-- QUINDIOSTAY - REPORTE 01: Catalogo de habitaciones
-- Nivel: SIMPLE
-- Descripcion: Que habitaciones tiene cada alojamiento, en que estado
--              operativo estan y su tipo y capacidad maxima.
-- Fuente: Documents/Entrega2_documentación/ENTREGA 2 - BASES DE DATOS I.md
-- =====================================================================
-- Reporte 01 - Catalogo de habitaciones y estado operativo actual
-- =====================================================================
SELECT
    a.nombre_alojamiento        AS alojamiento,
    t.nombre_tipo               AS tipo_habitacion,
    h.numero_habitacion         AS numero_habitacion,
    h.capacidad_maxima          AS capacidad_maxima,
    h.estado_operativo          AS estado_operativo
FROM Habitacion h
JOIN Tipo_Habitacion t
  ON h.id_tipo_habitacion = t.id_tipo_habitacion
JOIN Alojamiento a
  ON h.id_alojamiento = a.id_alojamiento
ORDER BY
    a.nombre_alojamiento,
    h.numero_habitacion;

-- =====================================================================
-- FIN DEL REPORTE 01
-- =====================================================================
