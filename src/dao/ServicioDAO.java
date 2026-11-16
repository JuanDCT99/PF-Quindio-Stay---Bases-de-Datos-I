package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.Servicio;
import model.UsoServicio;
import utils.ConexionBD;

/**
 * Data Access Object (DAO) para las entidades Servicio y Uso_Servicio.
 */
public class ServicioDAO {

    public List<Servicio> listarTodos() throws SQLException {
        List<Servicio> lista = new ArrayList<>();
        String sql = "SELECT id_servicio, nombre_servicio, tipo_servicio, hora_apertura, hora_cierre, uso_exclusivo, id_alojamiento "
                   + "FROM Servicio ORDER BY nombre_servicio";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearServicio(rs));
            }
        }
        return lista;
    }

    public Servicio buscarPorId(Long idServicio) throws SQLException {
        if (idServicio == null) {
            return null;
        }
        String sql = "SELECT id_servicio, nombre_servicio, tipo_servicio, hora_apertura, hora_cierre, uso_exclusivo, id_alojamiento "
                   + "FROM Servicio WHERE id_servicio = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idServicio);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearServicio(rs);
                }
            }
        }
        return null;
    }

    public Long registrarUsoServicio(UsoServicio uso) throws SQLException {
        if (uso == null || uso.getIdServicio() == null || uso.getIdReservaHabitacion() == null) {
            throw new IllegalArgumentException("Datos de uso de servicio incompletos.");
        }

        Long id = uso.getIdUsoServicio();
        if (id == null) {
            id = obtenerSiguienteIdUso();
            uso.setIdUsoServicio(id);
        }

        if (uso.getFechaHoraInicio() == null) {
            uso.setFechaHoraInicio(LocalDateTime.now());
        }
        if (uso.getFechaHoraFin() == null) {
            uso.setFechaHoraFin(uso.getFechaHoraInicio().plusHours(1));
        }
        if (uso.getIdPersonal() == null) {
            uso.setIdPersonal(1L); // personal por defecto
        }

        String sql = "INSERT INTO Uso_Servicio (id_uso_servicio, fecha_hora_inicio, fecha_hora_fin, solicitud, id_reserva_habitacion, id_servicio, id_personal) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, uso.getIdUsoServicio());
            ps.setTimestamp(2, Timestamp.valueOf(uso.getFechaHoraInicio()));
            ps.setTimestamp(3, Timestamp.valueOf(uso.getFechaHoraFin()));
            ps.setString(4, uso.getSolicitud());
            ps.setLong(5, uso.getIdReservaHabitacion());
            ps.setLong(6, uso.getIdServicio());
            ps.setLong(7, uso.getIdPersonal());
            ps.executeUpdate();
            return id;
        }
    }

    public Long obtenerSiguienteIdUso() throws SQLException {
        String sql = "SELECT NVL(MAX(id_uso_servicio), 0) + 1 AS siguiente_id FROM Uso_Servicio";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong("siguiente_id");
            }
        }
        return 1L;
    }

    private Servicio mapearServicio(ResultSet rs) throws SQLException {
        Servicio s = new Servicio();
        s.setIdServicio(rs.getLong("id_servicio"));
        s.setNombreServicio(rs.getString("nombre_servicio"));
        s.setTipoServicio(rs.getString("tipo_servicio"));
        Timestamp ha = rs.getTimestamp("hora_apertura");
        if (ha != null) {
            s.setHoraApertura(ha.toLocalDateTime());
        }
        Timestamp hc = rs.getTimestamp("hora_cierre");
        if (hc != null) {
            s.setHoraCierre(hc.toLocalDateTime());
        }
        s.setUsoExclusivo(rs.getString("uso_exclusivo"));
        s.setIdAlojamiento(rs.getLong("id_alojamiento"));
        return s;
    }
}
