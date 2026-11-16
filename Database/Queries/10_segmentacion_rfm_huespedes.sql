-- =====================================================================
-- QUINDIOSTAY - REPORTE 10: Segmentacion RFM y ranking de huespedes VIP
-- Nivel: AVANZADO
-- Descripcion: Quiénes son los huéspedes titulares mas valiosos segun su
--              ultima estancia, su frecuencia y su gasto, y cual es su
--              satisfaccion promedio.
-- Fuente: Documents/Entrega2_documentación/ENTREGA 2 - BASES DE DATOS I.md
-- =====================================================================
-- Reporte 10 - Segmentacion RFM y ranking de huespedes VIP
-- =====================================================================
WITH RECURSIVE
-- Recencia: ultima estancia de cada huésped
RFM_Recencia AS (
    SELECT
        h.id_huesped,
        MAX(rh.fecha_entrada_reservada) AS fecha_ultima_estancia
    FROM Huesped h
    JOIN Reserva r
        ON h.id_huesped = r.id_titular
    JOIN Reserva_Habitacion rh
        ON r.id_reserva = rh.id_reserva
    GROUP BY h.id_huesped
),
-- Frecuencia: cantidad de reservas por huésped
RFM_Frecuencia AS (
    SELECT
        h.id_huesped,
        COUNT(r.id_reserva) AS total_reservas
    FROM Huesped h
    JOIN Reserva r
        ON h.id_huesped = r.id_titular
    GROUP BY h.id_huesped
),
-- Gasto: total pagado por huésped
RFM_Gasto AS (
    SELECT
        h.id_huesped,
        SUM(p.monto) AS total_gastado
    FROM Huesped h
    JOIN Reserva r
        ON h.id_huesped = r.id_titular
    JOIN Pago p
        ON r.id_reserva = p.id_reserva
    GROUP BY h.id_huesped
),
-- Puntuacion promedio
RFM_Puntuacion AS (
    SELECT
        h.id_huesped,
        AVG(c.puntuacion) AS puntuacion_promedio
    FROM Huesped h
    JOIN Calificacion c
        ON h.id_huesped = c.id_huesped
    GROUP BY h.id_huesped
)
SELECT
    h.id_huesped,
    h.nombre,
    h.documento,
    h.telefono,
    h.correo,
    r.fecha_ultima_estancia,
    f.total_reservas AS frecuencia,
    g.total_gastado AS gasto_total,
    p.puntuacion_promedio AS satisfaccion,
    r.puntuacion_recencia,
    r.puntuacion_frecuencia,
    r.puntuacion_gasto,
    ROUND(
        (r.puntuacion_recencia + r.puntuacion_frecuencia + r.puntuacion_gasto)
        / 3.0, 2
    ) AS puntuacion_rf_total
FROM Huesped h
LEFT JOIN RFM_Recencia r
    ON h.id_huesped = r.id_huesped
LEFT JOIN RFM_Frecuencia f
    ON h.id_huesped = f.id_huesped
LEFT JOIN RFM_Gasto g
    ON h.id_huesped = g.id_huesped
LEFT JOIN RFM_Puntuacion p
    ON h.id_huesped = p.id_huesped
ORDER BY
    puntuacion_rf_total DESC;

-- =====================================================================
-- FIN DEL REPORTE 10
-- =====================================================================
