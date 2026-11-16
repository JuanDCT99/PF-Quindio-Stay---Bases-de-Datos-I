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
SELECT
    i.tipo_canal                     AS canal,
    tm.nombre_temporada              AS temporada,
    tm.anio                          AS anio,
    COUNT(r.id_reserva)              AS total_reservas,
    SUM(CASE WHEN r.estado = 'CANCELADA' THEN 1 ELSE 0 END) AS canceladas,
    SUM(CASE WHEN r.estado = 'NO_SHOW' THEN 1 ELSE 0 END) AS no_show,
    ROUND(COUNT(r.id_reserva) * 100.0 / NULLIF(COUNT(r.id_reserva), 0), 2) AS tasa_cancelacion_porcentaje,
    ROUND(SUM(r.precio_total) * 100.0 / NULLIF(SUM(r.precio_total), 0), 2) AS precio_promedio_noche_vendida,
    SUM(r.precio_total)              AS ingreso_total
FROM Reserva r
JOIN Reserva_Habitacion rh ON r.id_reserva = rh.id_reserva
JOIN Intermediario i ON r.id_intermediario = i.id_intermediario
JOIN Temporada tm ON rh.id_temporada = tm.id_temporada
WHERE r.estado IN ('CANCELADA', 'NO_SHOW')
GROUP BY i.tipo_canal, tm.nombre_temporada, tm.anio
ORDER BY i.tipo_canal, tm.anio;

-- =====================================================================
-- FIN DEL REPORTE 09
-- =====================================================================
