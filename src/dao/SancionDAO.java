package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.Sancion;
import utils.ConexionBD;

/**
 * Data Access Object (DAO) para la entidad Sancion.
 */
public class SancionDAO {

    public Long registrarSancion(Sancion sancion) throws SQLException {
        if (sancion == null || sancion.getValor() == null || sancion.getIdReserva() == null) {
            throw new IllegalArgumentException("Datos de la sanción incompletos.");
        }

        Long id = sancion.getIdSancion();
        if (id == null) {
            id = obtenerSiguienteId();
            sancion.setIdSancion(id);
        }

        if (sancion.getFechaSancion() == null) {
            sancion.setFechaSancion(LocalDateTime.now());
        }
        if (sancion.getTipoSancion() == null || sancion.getTipoSancion().isBlank()) {
            sancion.setTipoSancion("DANOS");
        }
        if (sancion.getDescripcion() == null || sancion.getDescripcion().isBlank()) {
            sancion.setDescripcion("Cargo por daño o incumplimiento de normas");
        }

        String sql = "INSERT INTO Sancion (id_sancion, tipo_sancion, descripcion, fecha_sancion, valor, id_reserva, id_servicio) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, sancion.getIdSancion());
            ps.setString(2, sancion.getTipoSancion());
            ps.setString(3, sancion.getDescripcion());
            ps.setTimestamp(4, Timestamp.valueOf(sancion.getFechaSancion()));
            ps.setBigDecimal(5, sancion.getValor());
            ps.setLong(6, sancion.getIdReserva());
            if (sancion.getIdServicio() != null) {
                ps.setLong(7, sancion.getIdServicio());
            } else {
                ps.setNull(7, Types.NUMERIC);
            }
            ps.executeUpdate();
            return id;
        }
    }

    public Sancion buscarPorId(Long idSancion) throws SQLException {
        if (idSancion == null) {
            return null;
        }
        String sql = "SELECT id_sancion, tipo_sancion, descripcion, fecha_sancion, valor, id_reserva, id_servicio "
                   + "FROM Sancion WHERE id_sancion = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idSancion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearSancion(rs);
                }
            }
        }
        return null;
    }

    public boolean actualizarSancion(Sancion sancion) throws SQLException {
        if (sancion == null || sancion.getIdSancion() == null || sancion.getValor() == null
                || sancion.getIdReserva() == null) {
            throw new IllegalArgumentException("Datos de la sanción incompletos.");
        }
        String sql = "UPDATE Sancion SET tipo_sancion = ?, descripcion = ?, fecha_sancion = ?, "
                   + "valor = ?, id_reserva = ?, id_servicio = ? WHERE id_sancion = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sancion.getTipoSancion());
            ps.setString(2, sancion.getDescripcion());
            ps.setTimestamp(3, Timestamp.valueOf(sancion.getFechaSancion() == null
                    ? LocalDateTime.now() : sancion.getFechaSancion()));
            ps.setBigDecimal(4, sancion.getValor());
            ps.setLong(5, sancion.getIdReserva());
            if (sancion.getIdServicio() != null) {
                ps.setLong(6, sancion.getIdServicio());
            } else {
                ps.setNull(6, Types.NUMERIC);
            }
            ps.setLong(7, sancion.getIdSancion());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminarSancion(Long idSancion) throws SQLException {
        if (idSancion == null) {
            return false;
        }
        String sql = "DELETE FROM Sancion WHERE id_sancion = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idSancion);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Sancion> listarTodos() throws SQLException {
        List<Sancion> lista = new ArrayList<>();
        String sql = "SELECT id_sancion, tipo_sancion, descripcion, fecha_sancion, valor, id_reserva, id_servicio "
                   + "FROM Sancion ORDER BY fecha_sancion DESC, id_sancion DESC";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearSancion(rs));
            }
        }
        return lista;
    }

    public List<Sancion> listarPorReserva(Long idReserva) throws SQLException {
        List<Sancion> lista = new ArrayList<>();
        if (idReserva == null) {
            return lista;
        }
        String sql = "SELECT id_sancion, tipo_sancion, descripcion, fecha_sancion, valor, id_reserva, id_servicio "
                   + "FROM Sancion WHERE id_reserva = ? ORDER BY fecha_sancion DESC";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idReserva);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearSancion(rs));
                }
            }
        }
        return lista;
    }

    public BigDecimal totalSancionesPorReserva(Long idReserva) throws SQLException {
        if (idReserva == null) {
            return BigDecimal.ZERO;
        }
        String sql = "SELECT NVL(SUM(valor), 0) AS total_sanciones FROM Sancion WHERE id_reserva = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idReserva);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total_sanciones");
                }
            }
        }
        return BigDecimal.ZERO;
    }

    public Long obtenerSiguienteId() throws SQLException {
        String sql = "SELECT NVL(MAX(id_sancion), 0) + 1 AS siguiente_id FROM Sancion";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong("siguiente_id");
            }
        }
        return 1L;
    }

    private Sancion mapearSancion(ResultSet rs) throws SQLException {
        Sancion s = new Sancion();
        s.setIdSancion(rs.getLong("id_sancion"));
        s.setTipoSancion(rs.getString("tipo_sancion"));
        s.setDescripcion(rs.getString("descripcion"));
        Timestamp fs = rs.getTimestamp("fecha_sancion");
        if (fs != null) {
            s.setFechaSancion(fs.toLocalDateTime());
        }
        s.setValor(rs.getBigDecimal("valor"));
        s.setIdReserva(rs.getLong("id_reserva"));
        long idServ = rs.getLong("id_servicio");
        if (!rs.wasNull()) {
            s.setIdServicio(idServ);
        }
        return s;
    }
}
