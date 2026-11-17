package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Habitacion;
import utils.ConexionBD;

/**
 * Data Access Object (DAO) para la entidad Habitacion.
 */
public class HabitacionDAO {

    public Long crearHabitacion(Habitacion h) throws SQLException {
        if (h == null || !h.esValido()) {
            throw new IllegalArgumentException("Datos de habitación inválidos.");
        }

        Long id = h.getIdHabitacion();
        if (id == null) {
            id = obtenerSiguienteId();
            h.setIdHabitacion(id);
        }

        String sql = "INSERT INTO Habitacion (id_habitacion, numero_habitacion, capacidad_maxima, descripcion, estado_operativo, id_tipo_habitacion, id_alojamiento) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, h.getIdHabitacion());
            ps.setString(2, h.getNumeroHabitacion());
            ps.setInt(3, h.getCapacidadMaxima());
            ps.setString(4, h.getDescripcion());
            ps.setString(5, h.getEstadoOperativo());
            ps.setLong(6, h.getIdTipoHabitacion());
            ps.setLong(7, h.getIdAlojamiento());
            ps.executeUpdate();
            return id;
        }
    }

    public boolean actualizarHabitacion(Habitacion h) throws SQLException {
        if (h == null || h.getIdHabitacion() == null) {
            return false;
        }

        String sql = "UPDATE Habitacion SET numero_habitacion = ?, capacidad_maxima = ?, descripcion = ?, estado_operativo = ?, id_tipo_habitacion = ?, id_alojamiento = ? "
                   + "WHERE id_habitacion = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, h.getNumeroHabitacion());
            ps.setInt(2, h.getCapacidadMaxima());
            ps.setString(3, h.getDescripcion());
            ps.setString(4, h.getEstadoOperativo());
            ps.setLong(5, h.getIdTipoHabitacion());
            ps.setLong(6, h.getIdAlojamiento());
            ps.setLong(7, h.getIdHabitacion());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean cambiarEstadoOperativo(Long idHabitacion, String nuevoEstado) throws SQLException {
        if (idHabitacion == null || nuevoEstado == null) {
            return false;
        }

        String sql = "UPDATE Habitacion SET estado_operativo = ? WHERE id_habitacion = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setLong(2, idHabitacion);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminarHabitacion(Long idHabitacion) throws SQLException {
        if (idHabitacion == null) {
            return false;
        }
        String sql = "DELETE FROM Habitacion WHERE id_habitacion = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idHabitacion);
            return ps.executeUpdate() > 0;
        }
    }

    public Habitacion buscarPorId(Long idHabitacion) throws SQLException {
        if (idHabitacion == null) {
            return null;
        }
        String sql = "SELECT id_habitacion, numero_habitacion, capacidad_maxima, descripcion, estado_operativo, id_tipo_habitacion, id_alojamiento "
                   + "FROM Habitacion WHERE id_habitacion = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idHabitacion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearHabitacion(rs);
                }
            }
        }
        return null;
    }

    public List<Habitacion> listarTodas() throws SQLException {
        List<Habitacion> lista = new ArrayList<>();
        String sql = "SELECT id_habitacion, numero_habitacion, capacidad_maxima, descripcion, estado_operativo, id_tipo_habitacion, id_alojamiento "
                   + "FROM Habitacion ORDER BY id_alojamiento, numero_habitacion";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearHabitacion(rs));
            }
        }
        return lista;
    }

    public List<Habitacion> listarPorAlojamiento(Long idAlojamiento) throws SQLException {
        List<Habitacion> lista = new ArrayList<>();
        if (idAlojamiento == null) {
            return lista;
        }
        String sql = "SELECT id_habitacion, numero_habitacion, capacidad_maxima, descripcion, estado_operativo, id_tipo_habitacion, id_alojamiento "
                   + "FROM Habitacion WHERE id_alojamiento = ? ORDER BY numero_habitacion";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idAlojamiento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearHabitacion(rs));
                }
            }
        }
        return lista;
    }

    public Long obtenerSiguienteId() throws SQLException {
        String sql = "SELECT NVL(MAX(id_habitacion), 0) + 1 AS siguiente_id FROM Habitacion";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong("siguiente_id");
            }
        }
        return 1L;
    }

    private Habitacion mapearHabitacion(ResultSet rs) throws SQLException {
        Habitacion h = new Habitacion();
        h.setIdHabitacion(rs.getLong("id_habitacion"));
        h.setNumeroHabitacion(rs.getString("numero_habitacion"));
        h.setCapacidadMaxima(rs.getInt("capacidad_maxima"));
        h.setDescripcion(rs.getString("descripcion"));
        h.setEstadoOperativo(rs.getString("estado_operativo"));
        h.setIdTipoHabitacion(rs.getLong("id_tipo_habitacion"));
        h.setIdAlojamiento(rs.getLong("id_alojamiento"));
        return h;
    }
}
