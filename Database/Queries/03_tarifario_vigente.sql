-- =====================================================================
-- QUINDIOSTAY - REPORTE 03: Tarifario vigente por temporada
-- Nivel: SIMPLE
-- Descripcion: Precio por noche de cada tipo de habitacion en cada
--              temporada del ano.
-- Fuente: Documents/Entrega2_documentación/ENTREGA 2 - BASES DE DATOS I.md
-- =====================================================================
-- Reporte 03 - Tarifario vigente por temporada y tipo de habitacion
-- =====================================================================
SELECT
    t.nombre_tipo             AS tipo_habitacion,
    tm.nombre_temporada       AS temporada,
    tm.anio                   AS anio,
    tar.id_temporada          AS temporada_id,
    tar.precio_noche          AS precio_noche
FROM Tarifa tar
JOIN Tipo_Habitacion t
  ON tar.id_tipo_habitacion = t.id_tipo_habitacion
JOIN Temporada tm
  ON tar.id_temporada = tm.id_temporada
ORDER BY
    tm.anio,
    tm.fecha_inicio,
    t.nombre_tipo;

-- =====================================================================
-- FIN DEL REPORTE 03
-- =====================================================================
