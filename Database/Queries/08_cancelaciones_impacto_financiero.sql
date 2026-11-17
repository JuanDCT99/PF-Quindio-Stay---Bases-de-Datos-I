-- =====================================================================
-- QUINDIOSTAY - REPORTE 08: Cancelaciones, no-show e impacto financiero
-- Nivel: AVANZADO
-- Descripcion: Como evolucionan cada mes, por alojamiento, la ocupacion (%),
--              el precio promedio por noche vendida (ADR) y el ingreso por
--              noche disponible (RevPAR), y cual es su variacion frente al
--              mes anterior.
-- Fuente: Documents/Entrega2_documentación/ENTREGA 2 - BASES DE DATOS I.md
-- =====================================================================
-- Reporte 08 - Cancelaciones, no-show e impacto financiero
-- =====================================================================
SELECT
    h.id_alojamiento              AS alojamiento_id,
    a.nombre_alojamiento          AS alojamiento,
    EXTRACT(YEAR FROM rh.fecha_entrada_reservada) AS anio,
    EXTRACT(MONTH FROM rh.fecha_entrada_reservada) AS mes,
    COUNT(DISTINCT rh.id_reserva) AS total_reservas,
    SUM(CASE WHEN r.estado = 'CANCELADA' THEN 1 ELSE 0 END) AS reservas_canceladas,
    SUM(CASE WHEN r.estado = 'NO_SHOW' THEN 1 ELSE 0 END) AS no_show,
    ROUND(COUNT(DISTINCT rh.id_reserva) * 100.0 / NULLIF(COUNT(DISTINCT rh.id_reserva), 0), 2) AS tasa_ocupacion
FROM Reserva r
JOIN Reserva_Habitacion rh ON r.id_reserva = rh.id_reserva
JOIN Habitacion h ON rh.id_habitacion = h.id_habitacion
JOIN Alojamiento a ON h.id_alojamiento = a.id_alojamiento
WHERE rh.fecha_entrada_reservada >= TRUNC(SYSDATE, 'MM')
  AND rh.fecha_salida_reservada >= TRUNC(SYSDATE, 'MM')
GROUP BY
    h.id_alojamiento,
    a.nombre_alojamiento,
    EXTRACT(YEAR FROM rh.fecha_entrada_reservada),
    EXTRACT(MONTH FROM rh.fecha_entrada_reservada)
ORDER BY
    a.nombre_alojamiento,
    anio,
    mes;

-- =====================================================================
-- FIN DEL REPORTE 08
-- =====================================================================
