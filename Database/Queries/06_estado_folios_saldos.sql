-- =====================================================================
-- QUINDIOSTAY - REPORTE 06: Estado de folios y cuentas con saldos pendientes
-- Nivel: INTERMEDIO
-- Descripcion: Que reservas tienen saldo pendiente de pago, considerando
--              el valor de la estadia, los consumos de servicios y las
--              sanciones.
-- Fuente: Documents/Entrega2_documentación/ENTREGA 2 - BASES DE DATOS I.md
-- =====================================================================
-- Reporte 06 - Estado de folios y cuentas con saldos pendientes
-- =====================================================================
SELECT
    r.id_reserva                    AS reserva_id,
    r.estado                        AS estado_reserva,
    rh.id_reserva_habitacion        AS reserva_habitacion_id,
    h.numero_habitacion             AS numero_habitacion,
    h.capacidad_maxima              AS capacidad_maxima,
    rh.precio_noche_pactado         AS precio_noche_pactado,
    rh.fecha_entrada_reservada      AS entrada_reservada,
    rh.fecha_salida_reservada       AS salida_reservada,
    r.precio_total                  AS precio_total_reserva,
    COALESCE((SELECT SUM(u.solicitud) FROM Uso_Servicio u WHERE u.id_reserva_habitacion = rh.id_reserva_habitacion), 0) AS consumo_servicios,
    COALESCE(s.total_sanciones, 0)  AS total_sanciones,
    COALESCE(p.total_pagado, 0)     AS total_pagado,
    (COALESCE(r.precio_total, 0) + COALESCE(s.total_sanciones, 0) + COALESCE(p.total_pagado, 0)) AS saldo_pendiente
FROM Reserva r
JOIN Reserva_Habitacion rh
  ON r.id_reserva = rh.id_reserva
JOIN Habitacion h
  ON rh.id_habitacion = h.id_habitacion
LEFT JOIN (
    SELECT id_reserva, SUM(valor) AS total_sanciones
    FROM Sancion
    GROUP BY id_reserva
) s ON r.id_reserva = s.id_reserva
LEFT JOIN (
    SELECT id_reserva, SUM(monto) AS total_pagado
    FROM Pago
    GROUP BY id_reserva
) p ON r.id_reserva = p.id_reserva
WHERE r.estado <> 'CANCELADA'
HAVING (COALESCE(r.precio_total, 0) + COALESCE(s.total_sanciones, 0) - COALESCE(p.total_pagado, 0)) > 0
ORDER BY
    r.id_reserva,
    h.numero_habitacion;

-- =====================================================================
-- FIN DEL REPORTE 06
-- =====================================================================
