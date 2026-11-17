-- =====================================================================
-- QUINDIOSTAY - REPORTE 05: Reservas y comisiones por intermediario
-- Nivel: INTERMEDIO
-- Descripcion: Cuantas reservas aporta cada agencia o portal, cuanto
--              ingreso generan y cuanta comision se les debe, frente
--              a las reservas directas.
-- Fuente: Documents/Entrega2_documentación/ENTREGA 2 - BASES DE DATOS I.md
-- =====================================================================
-- Reporte 05 - Reservas y comisiones por intermediario
-- =====================================================================
SELECT
    i.nombre_intermediario      AS intermediario,
    i.tipo_canal                 AS tipo_canal,
    i.porcentaje_comision        AS porcentaje_comision,
    COUNT(r.id_reserva)          AS total_reservas,
    SUM(r.precio_total)          AS ingreso_total,
    SUM(r.precio_total * i.porcentaje_comision / 100) AS comision
FROM Reserva r
JOIN Intermediario i
  ON r.id_intermediario = i.id_intermediario
GROUP BY
    i.nombre_intermediario,
    i.tipo_canal,
    i.porcentaje_comision
ORDER BY
    i.nombre_intermediario;

-- =====================================================================
-- FIN DEL REPORTE 05
-- =====================================================================
