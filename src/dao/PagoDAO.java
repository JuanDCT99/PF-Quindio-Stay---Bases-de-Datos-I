package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.Pago;
import utils.ConexionBD;
import utils.ValidacionesBD;

/**
 * Data Access Object (DAO) para la entidad Pago.
 */
public class PagoDAO {

    public Long registrarPago(Pago pago) throws SQLException {
        if (pago == null || pago.getMonto() == null || pago.getIdReserva() == null) {
            throw new IllegalArgumentException("Datos del pago incompletos.");
        }

        Long id = pago.getIdPago();
        if (id == null) {
            id = obtenerSiguienteId();
            pago.setIdPago(id);
        }

        if (pago.getFechaPago() == null) {
            pago.setFechaPago(LocalDateTime.now());
        }
        if (pago.getMedioPago() == null || pago.getMedioPago().isBlank()) {
            pago.setMedioPago("EFECTIVO");
        }

        String sql = "INSERT INTO Pago (id_pago, monto, medio_pago, fecha_pago, id_reserva) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ValidacionesBD.validarReserva(conn, pago.getIdReserva());
            ps.setLong(1, pago.getIdPago());
            ps.setBigDecimal(2, pago.getMonto());
            ps.setString(3, pago.getMedioPago());
            ps.setTimestamp(4, Timestamp.valueOf(pago.getFechaPago()));
            ps.setLong(5, pago.getIdReserva());
            ps.executeUpdate();
            return id;
        }
    }

    public List<Pago> listarTodos() throws SQLException {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT id_pago, monto, medio_pago, fecha_pago, id_reserva FROM Pago ORDER BY fecha_pago DESC, id_pago DESC";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearPago(rs));
            }
        }
        return lista;
    }

    public List<Pago> listarPorReserva(Long idReserva) throws SQLException {
        List<Pago> lista = new ArrayList<>();
        if (idReserva == null) {
            return lista;
        }
        String sql = "SELECT id_pago, monto, medio_pago, fecha_pago, id_reserva FROM Pago WHERE id_reserva = ? ORDER BY fecha_pago DESC";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idReserva);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearPago(rs));
                }
            }
        }
        return lista;
    }

    public BigDecimal totalPagadoPorReserva(Long idReserva) throws SQLException {
        if (idReserva == null) {
            return BigDecimal.ZERO;
        }
        String sql = "SELECT NVL(SUM(monto), 0) AS total_pagado FROM Pago WHERE id_reserva = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idReserva);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total_pagado");
                }
            }
        }
        return BigDecimal.ZERO;
    }

    public Long obtenerSiguienteId() throws SQLException {
        String sql = "SELECT NVL(MAX(id_pago), 0) + 1 AS siguiente_id FROM Pago";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong("siguiente_id");
            }
        }
        return 1L;
    }

    private Pago mapearPago(ResultSet rs) throws SQLException {
        Pago p = new Pago();
        p.setIdPago(rs.getLong("id_pago"));
        p.setMonto(rs.getBigDecimal("monto"));
        p.setMedioPago(rs.getString("medio_pago"));
        Timestamp fp = rs.getTimestamp("fecha_pago");
        if (fp != null) {
            p.setFechaPago(fp.toLocalDateTime());
        }
        p.setIdReserva(rs.getLong("id_reserva"));
        return p;
    }
}
