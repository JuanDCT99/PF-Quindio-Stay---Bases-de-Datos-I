-- =====================================================================
-- QUINDIOSTAY - REPORTE 04: Ingresos por temporada y tipo de habitacion
-- Nivel: INTERMEDIO
-- Descripcion: Cuantas noches se vendieron y cuanto ingreso por alojamiento
--              de habitaciones genero cada temporada, desglosado por
--              tipo de habitacion.
-- Fuente: Documents/Entrega2_documentación/ENTREGA 2 - BASES DE DATOS I.md
-- =====================================================================
-- Reporte 04 - Ingresos por temporada y tipo de habitacion
-- =====================================================================
SELECT
    tm.anio                      AS anio,
    tm.nombre_temporada          AS temporada,
    t.nombre_tipo                AS tipo_habitacion,
    COUNT(rh.id_reserva_habitacion) AS noches_vendidas,
    SUM(tar.precio_noche)        AS ingreso_abstracto
FROM Reserva_Habitacion rh
JOIN Reserva r
  ON rh.id_reserva = r.id_reserva
JOIN Tipo_Habitacion t
  ON rh.id_habitacion = t.id_tipo_habitacion
JOIN Tarifa tar
  ON rh.id_habitacion = tar.id_tipo_habitacion
JOIN Temporada tm
  ON rh.id_habitacion = tm.id_temporada
WHERE r.estado <> 'CANCELADA'
GROUP BY
    tm.anio,
    tm.nombre_temporada,
    t.nombre_tipo
ORDER BY
    tm.anio,
    tm.nombre_temporada,
    t.nombre_tipo;

-- =====================================================================
-- FIN DEL REPORTE 04
-- =====================================================================
