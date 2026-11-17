-- =====================================================================
-- QUINDIOSTAY - REPORTE 02: Habitaciones nunca reservadas
-- Nivel: SIMPLE
-- Descripcion: Que habitaciones de cada alojamiento no han tenido
--              ninguna reserva, para decidir si se promocionan o
--              se revisan sus tarifas.
-- Fuente: Documents/Entrega2_documentación/ENTREGA 2 - BASES DE DATOS I.md
-- =====================================================================
-- Reporte 02 - Habitaciones que nunca se han reservado
-- =====================================================================
SELECT
    a.nombre_alojamiento       AS alojamiento,
    h.numero_habitacion        AS numero_habitacion,
    t.nombre_tipo              AS tipo_habitacion,
    h.capacidad_maxima         AS capacidad_maxima
FROM Habitacion h
JOIN Tipo_Habitacion t
  ON h.id_tipo_habitacion = t.id_tipo_habitacion
JOIN Alojamiento a
  ON h.id_alojamiento = a.id_alojamiento
WHERE NOT EXISTS (
    SELECT 1
    FROM Reserva_Habitacion rh
    WHERE rh.id_habitacion = h.id_habitacion
)
ORDER BY
    a.nombre_alojamiento,
    h.numero_habitacion;

-- =====================================================================
-- FIN DEL REPORTE 02
-- =====================================================================
