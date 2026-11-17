package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import model.Folio;
import utils.ConexionBD;

/**
 * Data Access Object (DAO) para la entidad débil Folio (1:1 con Reserva).
 * Gestiona el balance de cobro, pagos y cierre de cuenta de una reserva.
 */
public class FolioDAO {

    public Folio buscarPorIdReserva(Long idReserva) throws SQLException {
        if (idReserva == null) {
            return null;
        }
        String sql = "SELECT id_reserva, fecha_apertura, fecha_cierre, estado FROM Folio WHERE id_reserva = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idReserva);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearFolio(rs);
                }
            }
        }
        return null;
    }

    public boolean cerrarFolio(Long idReserva) throws SQLException {
        if (idReserva == null) {
            return false;
        }
        String sql = "UPDATE Folio SET fecha_cierre = CURRENT_TIMESTAMP, estado = 'CERRADO' WHERE id_reserva = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idReserva);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Calcula el balance financiero exacto de la reserva:
     * Saldo = (Precio Total Reserva + Total Sanciones) - Total Pagos.
     *
     * @param idReserva Identificador de la reserva.
     * @return Saldo pendiente de pago (BigDecimal).
     */
    public BigDecimal calcularSaldoPendiente(Long idReserva) throws SQLException {
        if (idReserva == null) {
            return BigDecimal.ZERO;
        }

        String sql = "WITH TotReserva AS ("
                   + "    SELECT NVL(precio_total, 0) AS total_hospedaje FROM Reserva WHERE id_reserva = ?"
                   + "), TotPagos AS ("
                   + "    SELECT NVL(SUM(monto), 0) AS total_pagado FROM Pago WHERE id_reserva = ?"
                   + "), TotSanciones AS ("
                   + "    SELECT NVL(SUM(valor), 0) AS total_sanciones FROM Sancion WHERE id_reserva = ?"
                   + ") "
                   + "SELECT (r.total_hospedaje + san.total_sanciones - p.total_pagado) AS saldo_pendiente "
                   + "FROM TotReserva r, TotPagos p, TotSanciones san";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idReserva);
            ps.setLong(2, idReserva);
            ps.setLong(3, idReserva);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BigDecimal saldo = rs.getBigDecimal("saldo_pendiente");
                    return (saldo != null) ? saldo : BigDecimal.ZERO;
                }
            }
        }
        return BigDecimal.ZERO;
    }

    private Folio mapearFolio(ResultSet rs) throws SQLException {
        Folio f = new Folio();
        f.setIdReserva(rs.getLong("id_reserva"));
        Timestamp fa = rs.getTimestamp("fecha_apertura");
        if (fa != null) {
            f.setFechaApertura(fa.toLocalDateTime());
        }
        Timestamp fc = rs.getTimestamp("fecha_cierre");
        if (fc != null) {
            f.setFechaCierre(fc.toLocalDateTime());
        }
        f.setEstado(rs.getString("estado"));
        return f;
    }
}
