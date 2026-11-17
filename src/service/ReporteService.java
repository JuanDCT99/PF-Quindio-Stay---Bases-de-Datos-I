package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.HabitacionDAO;
import model.Reporte;
import utils.ConexionBD;

/** Servicio de reportes.
 * Ejecuta cada consulta SQL de los 10 reportes del negocio
 * y los devuelve como listas de mapas (clave = nombre de columna).
 */
public class ReporteService {

    private final HabitacionDAO habitacionDAO = new HabitacionDAO();

    /** Devuelve el catálogo de reportes disponible en la base de datos. */
    public List<Reporte> listarReportes() throws SQLException {
        List<Reporte> lista = new ArrayList<>();
        String sql = "SELECT id_reporte, nombre_reporte, nivel, pregunta_negocio "
                   + "FROM Reporte ORDER BY id_reporte";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Reporte reporte = new Reporte();
                reporte.setIdReporte(rs.getLong("id_reporte"));
                reporte.setNombreReporte(rs.getString("nombre_reporte"));
                reporte.setNivel(rs.getString("nivel"));
                reporte.setPreguntaNegocio(rs.getString("pregunta_negocio"));
                lista.add(reporte);
            }
        }
        return lista;
    }

    public Reporte buscarPorId(Long idReporte) throws SQLException {
        if (idReporte == null) {
            return null;
        }
        String sql = "SELECT id_reporte, nombre_reporte, nivel, pregunta_negocio FROM Reporte WHERE id_reporte = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idReporte);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearReporte(rs);
                }
            }
        }
        return null;
    }

    public Long crearReporte(Reporte reporte) throws SQLException {
        if (reporte == null) {
            throw new IllegalArgumentException("El reporte no puede ser nulo.");
        }
        try (Connection conn = ConexionBD.getConexion()) {
            if (reporte.getIdReporte() == null) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT NVL(MAX(id_reporte), 0) + 1 AS siguiente_id FROM Reporte");
                     ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        reporte.setIdReporte(rs.getLong("siguiente_id"));
                    }
                }
            }
            String sql = "INSERT INTO Reporte (id_reporte, nombre_reporte, nivel, pregunta_negocio) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, reporte.getIdReporte());
                ps.setString(2, reporte.getNombreReporte());
                ps.setString(3, reporte.getNivel());
                ps.setString(4, reporte.getPreguntaNegocio());
                ps.executeUpdate();
            }
            return reporte.getIdReporte();
        }
    }

    public boolean actualizarReporte(Reporte reporte) throws SQLException {
        if (reporte == null || reporte.getIdReporte() == null) {
            throw new IllegalArgumentException("Datos del reporte incompletos.");
        }
        String sql = "UPDATE Reporte SET nombre_reporte = ?, nivel = ?, pregunta_negocio = ? WHERE id_reporte = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reporte.getNombreReporte());
            ps.setString(2, reporte.getNivel());
            ps.setString(3, reporte.getPreguntaNegocio());
            ps.setLong(4, reporte.getIdReporte());
            return ps.executeUpdate() > 0;
        }
    }

    private Reporte mapearReporte(ResultSet rs) throws SQLException {
        Reporte reporte = new Reporte();
        reporte.setIdReporte(rs.getLong("id_reporte"));
        reporte.setNombreReporte(rs.getString("nombre_reporte"));
        reporte.setNivel(rs.getString("nivel"));
        reporte.setPreguntaNegocio(rs.getString("pregunta_negocio"));
        return reporte;
    }

    /** Ejecuta un reporte y devuelve los resultados.
     * Cada fila se convierte en un Map<String,Object> usando
     * el nombre de la columna como clave.
     */
    public List<Map<String, Object>> ejecutar(Long idReporte) throws SQLException {
        if (idReporte == null) {
            return new ArrayList<>();
        }
        int id = idReporte.intValue();
        String sql;
        switch (id) {
            case 1: sql = SQL_REPORTE_1; break;
            case 2: sql = SQL_REPORTE_2; break;
            case 3: sql = SQL_REPORTE_3; break;
            case 4: sql = SQL_REPORTE_4; break;
            case 5: sql = SQL_REPORTE_5; break;
            case 6: sql = SQL_REPORTE_6; break;
            case 7: sql = SQL_REPORTE_7; break;
            case 8: sql = SQL_REPORTE_8; break;
            case 9: sql = SQL_REPORTE_9; break;
            case 10: sql = SQL_REPORTE_10; break;
            default: throw new IllegalArgumentException("ID de reporte desconocido: " + idReporte);
        }
        return ejecutarSql(sql);
    }

    private List<Map<String, Object>> ejecutarSql(String sql) throws SQLException {
        List<Map<String, Object>> filas = new ArrayList<>();
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            java.sql.ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columna = meta.getColumnLabel(i);
                    Object valor = rs.getObject(i);
                    fila.put(columna, valor);
                }
                filas.add(fila);
            }
        }
        return filas;
    }

    // ============================================
    // 10 REPORTES
    // ============================================

    private static final String SQL_REPORTE_1 =
        "SELECT " +
        "    a.nombre_alojamiento        AS alojamiento, " +
        "    t.nombre_tipo               AS tipo_habitacion, " +
        "    h.numero_habitacion         AS numero_habitacion, " +
        "    h.capacidad_maxima          AS capacidad_maxima, " +
        "    h.estado_operativo          AS estado_operativo " +
        "FROM Habitacion h " +
        "JOIN Tipo_Habitacion t ON h.id_tipo_habitacion = t.id_tipo_habitacion " +
        "JOIN Alojamiento a ON h.id_alojamiento = a.id_alojamiento " +
        "ORDER BY a.nombre_alojamiento, h.numero_habitacion";

    private static final String SQL_REPORTE_2 =
        "SELECT " +
        "    a.nombre_alojamiento       AS alojamiento, " +
        "    h.numero_habitacion        AS numero_habitacion, " +
        "    t.nombre_tipo              AS tipo_habitacion, " +
        "    h.capacidad_maxima         AS capacidad_maxima " +
        "FROM Habitacion h " +
        "JOIN Tipo_Habitacion t ON h.id_tipo_habitacion = t.id_tipo_habitacion " +
        "JOIN Alojamiento a ON h.id_alojamiento = a.id_alojamiento " +
        "WHERE NOT EXISTS ( " +
        "    SELECT 1 FROM Reserva_Habitacion rh WHERE rh.id_habitacion = h.id_habitacion " +
        ") " +
        "ORDER BY a.nombre_alojamiento, h.numero_habitacion";

    private static final String SQL_REPORTE_3 =
        "SELECT " +
        "    t.nombre_tipo             AS tipo_habitacion, " +
        "    tm.nombre_temporada       AS temporada, " +
        "    tm.anio                   AS anio, " +
        "    tar.precio_noche          AS precio_noche " +
        "FROM Tarifa tar " +
        "JOIN Tipo_Habitacion t ON tar.id_tipo_habitacion = t.id_tipo_habitacion " +
        "JOIN Temporada tm ON tar.id_temporada = tm.id_temporada " +
        "ORDER BY tm.anio, t.nombre_tipo";

    private static final String SQL_REPORTE_4 =
        "SELECT " +
        "    tm.anio                      AS anio, " +
        "    tm.nombre_temporada          AS temporada, " +
        "    t.nombre_tipo                AS tipo_habitacion, " +
        "    COUNT(rh.id_reserva_habitacion) AS noches_vendidas, " +
        "    SUM(tar.precio_noche)        AS ingreso_abstracto " +
        "FROM Reserva_Habitacion rh " +
        "JOIN Reserva r ON rh.id_reserva = r.id_reserva " +
        "JOIN Tipo_Habitacion t ON rh.id_habitacion = t.id_tipo_habitacion " +
        "JOIN Tarifa tar ON t.id_tipo_habitacion = tar.id_tipo_habitacion " +
        "JOIN Temporada tm ON rh.id_habitacion = tm.id_temporada " +
        "WHERE r.estado <> 'CANCELADA' " +
        "GROUP BY tm.anio, tm.nombre_temporada, t.nombre_tipo " +
        "ORDER BY tm.anio, tm.nombre_temporada, t.nombre_tipo";

    private static final String SQL_REPORTE_5 =
        "SELECT " +
        "    i.nombre_intermediario      AS intermediario, " +
        "    i.tipo_canal                 AS tipo_canal, " +
        "    i.porcentaje_comision        AS porcentaje_comision, " +
        "    COUNT(r.id_reserva)          AS total_reservas, " +
        "    SUM(r.precio_total)          AS ingreso_total, " +
        "    SUM(r.precio_total * i.porcentaje_comision / 100) AS comision " +
        "FROM Reserva r " +
        "JOIN Intermediario i ON r.id_intermediario = i.id_intermediario " +
        "GROUP BY i.nombre_intermediario, i.tipo_canal, i.porcentaje_comision " +
        "ORDER BY i.nombre_intermediario";

    private static final String SQL_REPORTE_6 =
        "SELECT " +
        "    r.id_reserva                    AS reserva_id, " +
        "    r.estado                        AS estado_reserva, " +
        "    rh.id_reserva_habitacion        AS reserva_habitacion_id, " +
        "    h.numero_habitacion             AS numero_habitacion, " +
        "    h.capacidad_maxima              AS capacidad_maxima, " +
        "    rh.precio_noche_pactado         AS precio_noche_pactado, " +
        "    rh.fecha_entrada_reservada      AS entrada_reservada, " +
        "    rh.fecha_salida_reservada       AS salida_reservada, " +
        "    r.precio_total                  AS precio_total_reserva, " +
        "    COALESCE(s.total_sanciones, 0)  AS total_sanciones, " +
        "    COALESCE(p.total_pagado, 0)     AS total_pagado, " +
        "    (COALESCE(r.precio_total, 0) + COALESCE(s.total_sanciones, 0) - COALESCE(p.total_pagado, 0)) AS saldo_pendiente " +
        "FROM Reserva r " +
        "JOIN Reserva_Habitacion rh ON r.id_reserva = rh.id_reserva " +
        "JOIN Habitacion h ON rh.id_habitacion = h.id_habitacion " +
        "LEFT JOIN ( " +
        "    SELECT id_reserva, SUM(valor) AS total_sanciones FROM Sancion GROUP BY id_reserva " +
        ") s ON r.id_reserva = s.id_reserva " +
        "LEFT JOIN ( " +
        "    SELECT id_reserva, SUM(monto) AS total_pagado FROM Pago GROUP BY id_reserva " +
        ") p ON r.id_reserva = p.id_reserva " +
        "WHERE r.estado <> 'CANCELADA' " +
        "HAVING (COALESCE(r.precio_total, 0) + COALESCE(s.total_sanciones, 0) - COALESCE(p.total_pagado, 0)) > 0 " +
        "ORDER BY r.id_reserva, h.numero_habitacion";

    private static final String SQL_REPORTE_7 =
        "SELECT " +
        "    s.id_servicio                 AS servicio_id, " +
        "    s.nombre_servicio             AS nombre_servicio, " +
        "    s.tipo_servicio               AS tipo_servicio, " +
        "    a.nombre_alojamiento          AS alojamiento, " +
        "    COUNT(u.id_uso_servicio)      AS total_usos, " +
        "    SUM(CASE WHEN u.solicitud IS NOT NULL AND u.solicitud != '' THEN 1 ELSE 0 END) AS usos_registrados " +
        "FROM Servicio s " +
        "JOIN Alojamiento a ON s.id_alojamiento = a.id_alojamiento " +
        "LEFT JOIN Uso_Servicio u ON u.id_servicio = s.id_servicio " +
        "GROUP BY s.id_servicio, s.nombre_servicio, s.tipo_servicio, a.nombre_alojamiento " +
        "ORDER BY a.nombre_alojamiento, total_usos DESC";

    private static final String SQL_REPORTE_8 =
        "SELECT " +
        "    h.id_alojamiento              AS alojamiento_id, " +
        "    a.nombre_alojamiento          AS alojamiento, " +
        "    EXTRACT(YEAR FROM rh.fecha_entrada_reservada) AS anio, " +
        "    EXTRACT(MONTH FROM rh.fecha_entrada_reservada) AS mes, " +
        "    COUNT(DISTINCT rh.id_reserva) AS total_reservas, " +
        "    SUM(CASE WHEN r.estado = 'CANCELADA' THEN 1 ELSE 0 END) AS reservas_canceladas, " +
        "    SUM(CASE WHEN r.estado = 'NO_SHOW' THEN 1 ELSE 0 END) AS no_show " +
        "FROM Reserva r " +
        "JOIN Reserva_Habitacion rh ON r.id_reserva = rh.id_reserva " +
        "JOIN Habitacion h ON rh.id_habitacion = h.id_habitacion " +
        "JOIN Alojamiento a ON h.id_alojamiento = a.id_alojamiento " +
        "WHERE rh.fecha_entrada_reservada >= TRUNC(SYSDATE, 'MM') " +
        "  AND rh.fecha_salida_reservada >= TRUNC(SYSDATE, 'MM') " +
        "GROUP BY " +
        "    h.id_alojamiento, " +
        "    a.nombre_alojamiento, " +
        "    EXTRACT(YEAR FROM rh.fecha_entrada_reservada), " +
        "    EXTRACT(MONTH FROM rh.fecha_entrada_reservada) " +
        "ORDER BY a.nombre_alojamiento, anio, mes";

    private static final String SQL_REPORTE_9 =
        "SELECT " +
        "    i.tipo_canal                     AS canal, " +
        "    tm.nombre_temporada              AS temporada, " +
        "    tm.anio                          AS anio, " +
        "    COUNT(r.id_reserva)              AS total_reservas, " +
        "    SUM(CASE WHEN r.estado = 'CANCELADA' THEN 1 ELSE 0 END) AS canceladas, " +
        "    SUM(CASE WHEN r.estado = 'NO_SHOW' THEN 1 ELSE 0 END) AS no_show " +
        "FROM Reserva r " +
        "JOIN Reserva_Habitacion rh ON r.id_reserva = rh.id_reserva " +
        "JOIN Intermediario i ON r.id_intermediario = i.id_intermediario " +
        "JOIN Temporada tm ON rh.id_temporada = tm.id_temporada " +
        "WHERE r.estado IN ('CANCELADA', 'NO_SHOW') " +
        "GROUP BY i.tipo_canal, tm.nombre_temporada, tm.anio " +
        "ORDER BY i.tipo_canal, tm.anio";

    private static final String SQL_REPORTE_10 =
        "SELECT " +
        "    h.id_huesped, " +
        "    h.nombre, " +
        "    h.documento, " +
        "    h.telefono, " +
        "    h.correo, " +
        "    MAX(rh.fecha_entrada_reservada) AS fecha_ultima_estancia " +
        "FROM Huesped h " +
        "LEFT JOIN Reserva r ON h.id_huesped = r.id_titular " +
        "LEFT JOIN Reserva_Habitacion rh ON r.id_reserva = rh.id_reserva " +
        "GROUP BY " +
        "    h.id_huesped, " +
        "    h.nombre, " +
        "    h.documento, " +
        "    h.telefono, " +
        "    h.correo " +
        "ORDER BY fecha_ultima_estancia DESC";
}
