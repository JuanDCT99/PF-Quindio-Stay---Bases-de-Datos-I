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
    SUM(rh.fecha_salida_reservada - rh.fecha_entrada_reservada + rh.noches_adicionales) AS noches_vendidas,
    SUM(tar.precio_noche * (rh.fecha_salida_reservada - rh.fecha_entrada_reservada + rh.noches_adicionales)) AS ingreso_total
FROM Reserva_Habitacion rh
JOIN Reserva r
  ON rh.id_reserva = r.id_reserva
JOIN Habitacion h
  ON rh.id_habitacion = h.id_habitacion
JOIN Tipo_Habitacion t
  ON h.id_tipo_habitacion = t.id_tipo_habitacion
JOIN Tarifa tar
  ON tar.id_tipo_habitacion = t.id_tipo_habitacion
JOIN Temporada tm
  ON tar.id_temporada = tm.id_temporada
WHERE r.estado <> 'CANCELADA'
  AND rh.fecha_entrada_reservada BETWEEN tm.fecha_inicio AND tm.fecha_fin
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
