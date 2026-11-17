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
WITH RFM_Recencia AS (
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
RFM_Frecuencia AS (
    SELECT
        h.id_huesped,
        COUNT(DISTINCT r.id_reserva) AS total_reservas
    FROM Huesped h
    JOIN Reserva r
        ON h.id_huesped = r.id_titular
    GROUP BY h.id_huesped
),
RFM_Gasto AS (
    SELECT
        h.id_huesped,
        SUM(r.precio_total) AS total_gastado
    FROM Huesped h
    JOIN Reserva r
        ON h.id_huesped = r.id_titular
    GROUP BY h.id_huesped
),
RFM_Puntuacion AS (
    SELECT
        h.id_huesped,
        AVG(c.puntuacion) AS puntuacion_promedio
    FROM Huesped h
    JOIN Calificacion c
        ON h.id_huesped = c.id_huesped
    GROUP BY h.id_huesped
),
RFM_Scores AS (
    SELECT
        id_huesped,
        NTILE(5) OVER (ORDER BY fecha_ultima_estancia DESC NULLS LAST) AS puntuacion_recencia,
        NTILE(5) OVER (ORDER BY total_reservas DESC) AS puntuacion_frecuencia,
        NTILE(5) OVER (ORDER BY total_gastado DESC) AS puntuacion_gasto
    FROM (
        SELECT
            h.id_huesped,
            rec.fecha_ultima_estancia,
            COALESCE(fre.total_reservas, 0) AS total_reservas,
            COALESCE(gas.total_gastado, 0) AS total_gastado
        FROM Huesped h
        LEFT JOIN RFM_Recencia rec ON h.id_huesped = rec.id_huesped
        LEFT JOIN RFM_Frecuencia fre ON h.id_huesped = fre.id_huesped
        LEFT JOIN RFM_Gasto gas ON h.id_huesped = gas.id_huesped
    )
)
SELECT
    h.id_huesped,
    h.nombre,
    h.documento,
    h.telefono,
    h.correo,
    rec.fecha_ultima_estancia,
    COALESCE(fre.total_reservas, 0) AS frecuencia,
    COALESCE(gas.total_gastado, 0) AS gasto_total,
    pun.puntuacion_promedio AS satisfaccion,
    sc.puntuacion_recencia,
    sc.puntuacion_frecuencia,
    sc.puntuacion_gasto,
    ROUND(
        (sc.puntuacion_recencia + sc.puntuacion_frecuencia + sc.puntuacion_gasto)
        / 3.0, 2
    ) AS puntuacion_rf_total
FROM Huesped h
LEFT JOIN RFM_Recencia rec ON h.id_huesped = rec.id_huesped
LEFT JOIN RFM_Frecuencia fre ON h.id_huesped = fre.id_huesped
LEFT JOIN RFM_Gasto gas ON h.id_huesped = gas.id_huesped
LEFT JOIN RFM_Puntuacion pun ON h.id_huesped = pun.id_huesped
LEFT JOIN RFM_Scores sc ON h.id_huesped = sc.id_huesped
ORDER BY puntuacion_rf_total DESC;

-- =====================================================================
-- FIN DEL REPORTE 10
-- =====================================================================
