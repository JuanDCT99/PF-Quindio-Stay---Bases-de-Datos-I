-- =====================================================================
-- QUINDIOSTAY - REPORTE 09: Analisis integral de cancelaciones, no-show
--                         e impacto financiero
-- Nivel: AVANZADO
-- Descripcion: Tasa de cancelacion y de no-show por canal y temporada,
--              cuanto ingreso se pierde y cuanto se retiene en pagos
--              ya recibidos.
-- Fuente: Documents/Entrega2_documentación/ENTREGA 2 - BASES DE DATOS I.md
-- =====================================================================
-- Reporte 09 - Cancelaciones por canal y temporada
-- =====================================================================
WITH reservas_afectadas AS (
    SELECT
        r.id_reserva,
        r.estado,
        r.precio_total,
        i.tipo_canal,
        tm.nombre_temporada,
        tm.anio,
        ROW_NUMBER() OVER (PARTITION BY r.id_reserva ORDER BY rh.fecha_entrada_reservada) AS rn
    FROM Reserva r
    JOIN Reserva_Habitacion rh ON r.id_reserva = rh.id_reserva
    JOIN Intermediario i ON r.id_intermediario = i.id_intermediario
    JOIN Temporada tm ON rh.fecha_entrada_reservada BETWEEN tm.fecha_inicio AND tm.fecha_fin
    WHERE r.estado IN ('CANCELADA', 'NO_SHOW')
)
SELECT
    tipo_canal                       AS canal,
    nombre_temporada                 AS temporada,
    anio                             AS anio,
    COUNT(id_reserva)                AS total_reservas,
    SUM(CASE WHEN estado = 'CANCELADA' THEN 1 ELSE 0 END) AS canceladas,
    SUM(CASE WHEN estado = 'NO_SHOW' THEN 1 ELSE 0 END) AS no_show,
    ROUND(SUM(CASE WHEN estado = 'CANCELADA' THEN 1 ELSE 0 END) * 100.0
          / NULLIF(COUNT(id_reserva), 0), 2) AS tasa_cancelacion_porcentaje,
    ROUND(SUM(precio_total) / NULLIF(COUNT(id_reserva), 0), 2) AS precio_promedio_noche_vendida,
    SUM(precio_total)                AS ingreso_total
FROM reservas_afectadas
WHERE rn = 1
GROUP BY tipo_canal, nombre_temporada, anio
ORDER BY tipo_canal, anio;

-- =====================================================================
-- FIN DEL REPORTE 09
-- =====================================================================
