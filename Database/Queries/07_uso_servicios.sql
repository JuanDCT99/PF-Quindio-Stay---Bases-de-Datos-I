-- =====================================================================
-- QUINDIOSTAY - REPORTE 07: Uso e ingresos de los servicios adicionales
-- Nivel: INTERMEDIO
-- Descripcion: Que servicios del alojamiento (piscina, spa, sauna,
--              jacuzzi, cancha, centro de eventos, servicio a la
--              habitacion) tienen mas uso y cuanto ingreso aportan.
-- Fuente: Documents/Entrega2_documentación/ENTREGA 2 - BASES DE DATOS I.md
-- =====================================================================
-- Reporte 07 - Uso e ingresos de los servicios adicionales
-- =====================================================================
SELECT
    s.id_servicio                 AS servicio_id,
    s.nombre_servicio             AS nombre_servicio,
    s.tipo_servicio               AS tipo_servicio,
    a.nombre_alojamiento          AS alojamiento,
    COUNT(u.id_uso_servicio)      AS total_usos,
    SUM(
        CASE
            WHEN u.solicitud IS NOT NULL AND u.solicitud != ''
            THEN 1
            ELSE 0
        END
    ) AS usos_registrados,
    ROUND(AVG(u.fecha_hora_fin - u.fecha_hora_inicio), 2) AS tiempo_promedio_uso
FROM Servicio s
JOIN Alojamiento a
  ON s.id_alojamiento = a.id_alojamiento
LEFT JOIN Uso_Servicio u
  ON u.id_servicio = s.id_servicio
GROUP BY
    s.id_servicio,
    s.nombre_servicio,
    s.tipo_servicio,
    a.nombre_alojamiento
ORDER BY
    a.nombre_alojamiento,
    total_usos DESC;

-- =====================================================================
-- FIN DEL REPORTE 07
-- =====================================================================
