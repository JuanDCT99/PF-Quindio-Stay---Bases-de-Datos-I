package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import model.Reserva;
import utils.ConexionBD;
import utils.ValidacionesBD;

/**
 * Data Access Object (DAO) para la entidad Reserva.
 * Administra el ciclo de vida de reservas hoteleras: creación, check-in, check-out y estados.
 */
public class ReservaDAO {

    /**
     * Crea una reserva transaccional completa:
     * 1. Inserta en Reserva.
     * 2. Inserta en Reserva_Habitacion.
     * 3. Crea el Folio inicial asociado (entidad débil 1:1).
     * 4. Actualiza el estado operativo de la habitación si aplica.
     */
    public Long crearReservaCompleta(Reserva reserva, Long idHabitacion, LocalDate entrada, LocalDate salida, BigDecimal precioNoche) throws SQLException {
        if (reserva == null || idHabitacion == null || entrada == null || salida == null) {
            throw new IllegalArgumentException("Datos de reserva incompletos.");
        }

        long noches = ChronoUnit.DAYS.between(entrada, salida);
        if (noches <= 0) {
            noches = 1;
        }

        BigDecimal precioTotal = precioNoche.multiply(BigDecimal.valueOf(noches));
        reserva.setPrecioTotal(precioTotal);

        Long idReserva = reserva.getIdReserva();
        if (idReserva == null) {
            idReserva = obtenerSiguienteIdReserva();
            reserva.setIdReserva(idReserva);
        }

        LocalDateTime ahora = LocalDateTime.now();
        if (reserva.getFechaCreacion() == null) {
            reserva.setFechaCreacion(ahora);
        }
        if (reserva.getFechaLimiteConfirmacion() == null) {
            reserva.setFechaLimiteConfirmacion(ahora.plusHours(48));
        }
        if (reserva.getEstado() == null || reserva.getEstado().isBlank()) {
            reserva.setEstado("CONFIRMADA");
        }
        if (reserva.getCanalDirecto() == null) {
            reserva.setCanalDirecto("S");
        }

        Connection conn = null;
        try {
            conn = ConexionBD.getConexion();
            conn.setAutoCommit(false);

            ValidacionesBD.validarPlan(conn, reserva.getIdPlan());
            ValidacionesBD.validarHuesped(conn, reserva.getIdTitular());
            ValidacionesBD.validarHabitacion(conn, idHabitacion);
            if (reserva.getIdIntermediario() != null) {
                ValidacionesBD.validarIntermediario(conn, reserva.getIdIntermediario());
            }

            // 1. Insertar Reserva
            String sqlReserva = "INSERT INTO Reserva (id_reserva, estado, fecha_creacion, hora_estimada_llegada, fecha_limite_confirmacion, canal_directo, precio_total, id_plan, id_titular, id_intermediario) "
                              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlReserva)) {
                ps.setLong(1, reserva.getIdReserva());
                ps.setString(2, reserva.getEstado());
                ps.setTimestamp(3, Timestamp.valueOf(reserva.getFechaCreacion()));
                if (reserva.getHoraEstimadaLlegada() != null) {
                    ps.setTimestamp(4, Timestamp.valueOf(reserva.getHoraEstimadaLlegada()));
                } else {
                    ps.setNull(4, Types.TIMESTAMP);
                }
                ps.setTimestamp(5, Timestamp.valueOf(reserva.getFechaLimiteConfirmacion()));
                ps.setString(6, reserva.getCanalDirecto());
                ps.setBigDecimal(7, reserva.getPrecioTotal());
                ps.setLong(8, reserva.getIdPlan());
                ps.setLong(9, reserva.getIdTitular());
                if (reserva.getIdIntermediario() != null) {
                    ps.setLong(10, reserva.getIdIntermediario());
                } else {
                    ps.setNull(10, Types.NUMERIC);
                }
                ps.executeUpdate();
            }

            // 2. Insertar Reserva_Habitacion
            Long idReservaHab = obtenerSiguienteIdReservaHabitacion(conn);
            String sqlHab = "INSERT INTO Reserva_Habitacion (id_reserva_habitacion, fecha_entrada_reservada, fecha_salida_reservada, precio_noche_pactado, checkin_real, checkout_real, noches_adicionales, id_reserva, id_habitacion) "
                          + "VALUES (?, ?, ?, ?, NULL, NULL, 0, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlHab)) {
                ps.setLong(1, idReservaHab);
                ps.setDate(2, Date.valueOf(entrada));
                ps.setDate(3, Date.valueOf(salida));
                ps.setBigDecimal(4, precioNoche);
                ps.setLong(5, idReserva);
                ps.setLong(6, idHabitacion);
                ps.executeUpdate();
            }

            // 3. Crear Folio
            String sqlFolio = "INSERT INTO Folio (id_reserva, fecha_apertura, fecha_cierre, estado) "
                            + "VALUES (?, ?, NULL, 'ABIERTO')";
            try (PreparedStatement ps = conn.prepareStatement(sqlFolio)) {
                ps.setLong(1, idReserva);
                ps.setTimestamp(2, Timestamp.valueOf(ahora));
                ps.executeUpdate();
            }

            conn.commit();
            return idReserva;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    public boolean actualizarEstado(Long idReserva, String nuevoEstado) throws SQLException {
        if (idReserva == null || nuevoEstado == null) {
            return false;
        }
        String sql = "UPDATE Reserva SET estado = ? WHERE id_reserva = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setLong(2, idReserva);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean registrarCheckIn(Long idReserva) throws SQLException {
        if (idReserva == null) {
            return false;
        }
        String sql = "UPDATE Reserva_Habitacion SET checkin_real = CURRENT_TIMESTAMP WHERE id_reserva = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idReserva);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                actualizarEstado(idReserva, "CONFIRMADA");
                // Marcar habitación como OCUPADA
                String sqlHab = "UPDATE Habitacion SET estado_operativo = 'OCUPADA' "
                              + "WHERE id_habitacion IN (SELECT id_habitacion FROM Reserva_Habitacion WHERE id_reserva = ?)";
                try (PreparedStatement psHab = conn.prepareStatement(sqlHab)) {
                    psHab.setLong(1, idReserva);
                    psHab.executeUpdate();
                }
                return true;
            }
        }
        return false;
    }

    public boolean registrarCheckOut(Long idReserva) throws SQLException {
        if (idReserva == null) {
            return false;
        }
        String sql = "UPDATE Reserva_Habitacion SET checkout_real = CURRENT_TIMESTAMP WHERE id_reserva = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idReserva);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                // Marcar habitación como EN_PREPARACION (limpieza)
                String sqlHab = "UPDATE Habitacion SET estado_operativo = 'EN_PREPARACION' "
                              + "WHERE id_habitacion IN (SELECT id_habitacion FROM Reserva_Habitacion WHERE id_reserva = ?)";
                try (PreparedStatement psHab = conn.prepareStatement(sqlHab)) {
                    psHab.setLong(1, idReserva);
                    psHab.executeUpdate();
                }

                // Cerrar folio
                String sqlFolio = "UPDATE Folio SET fecha_cierre = CURRENT_TIMESTAMP, estado = 'CERRADO' WHERE id_reserva = ?";
                try (PreparedStatement psFolio = conn.prepareStatement(sqlFolio)) {
                    psFolio.setLong(1, idReserva);
                    psFolio.executeUpdate();
                }
                return true;
            }
        }
        return false;
    }

    public Reserva buscarPorId(Long idReserva) throws SQLException {
        if (idReserva == null) {
            return null;
        }
        String sql = "SELECT id_reserva, estado, fecha_creacion, hora_estimada_llegada, fecha_limite_confirmacion, canal_directo, precio_total, id_plan, id_titular, id_intermediario "
                   + "FROM Reserva WHERE id_reserva = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idReserva);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearReserva(rs);
                }
            }
        }
        return null;
    }

    public List<Reserva> listarTodas() throws SQLException {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT id_reserva, estado, fecha_creacion, hora_estimada_llegada, fecha_limite_confirmacion, canal_directo, precio_total, id_plan, id_titular, id_intermediario "
                   + "FROM Reserva ORDER BY id_reserva DESC";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearReserva(rs));
            }
        }
        return lista;
    }

    public List<Reserva> listarPorEstado(String estado) throws SQLException {
        List<Reserva> lista = new ArrayList<>();
        if (estado == null) {
            return lista;
        }
        String sql = "SELECT id_reserva, estado, fecha_creacion, hora_estimada_llegada, fecha_limite_confirmacion, canal_directo, precio_total, id_plan, id_titular, id_intermediario "
                   + "FROM Reserva WHERE UPPER(estado) = UPPER(?) ORDER BY id_reserva DESC";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado.trim());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearReserva(rs));
                }
            }
        }
        return lista;
    }

    public Long obtenerSiguienteIdReserva() throws SQLException {
        String sql = "SELECT NVL(MAX(id_reserva), 1000) + 1 AS siguiente_id FROM Reserva";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong("siguiente_id");
            }
        }
        return 1001L;
    }

    private Long obtenerSiguienteIdReservaHabitacion(Connection conn) throws SQLException {
        String sql = "SELECT NVL(MAX(id_reserva_habitacion), 0) + 1 AS siguiente_id FROM Reserva_Habitacion";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong("siguiente_id");
            }
        }
        return 1L;
    }

    private Reserva mapearReserva(ResultSet rs) throws SQLException {
        Reserva r = new Reserva();
        r.setIdReserva(rs.getLong("id_reserva"));
        r.setEstado(rs.getString("estado"));
        Timestamp fc = rs.getTimestamp("fecha_creacion");
        if (fc != null) {
            r.setFechaCreacion(fc.toLocalDateTime());
        }
        Timestamp hl = rs.getTimestamp("hora_estimada_llegada");
        if (hl != null) {
            r.setHoraEstimadaLlegada(hl.toLocalDateTime());
        }
        Timestamp fl = rs.getTimestamp("fecha_limite_confirmacion");
        if (fl != null) {
            r.setFechaLimiteConfirmacion(fl.toLocalDateTime());
        }
        r.setCanalDirecto(rs.getString("canal_directo"));
        r.setPrecioTotal(rs.getBigDecimal("precio_total"));
        r.setIdPlan(rs.getLong("id_plan"));
        r.setIdTitular(rs.getLong("id_titular"));
        long idInter = rs.getLong("id_intermediario");
        if (!rs.wasNull()) {
            r.setIdIntermediario(idInter);
        }
        return r;
    }
}
