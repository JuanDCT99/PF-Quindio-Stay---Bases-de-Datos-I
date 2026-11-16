package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Huesped;
import utils.ConexionBD;

/**
 * Data Access Object (DAO) para la entidad Huesped.
 */
public class HuespedDAO {

    public Long registrarHuesped(Huesped huesped) throws SQLException {
        if (huesped == null) {
            throw new IllegalArgumentException("Huésped no puede ser nulo.");
        }

        Long id = huesped.getIdHuesped();
        if (id == null) {
            id = obtenerSiguienteId();
            huesped.setIdHuesped(id);
        }

        String sql = "INSERT INTO Huesped (id_huesped, nombre, documento, fecha_nacimiento, direccion, correo, telefono) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, huesped.getIdHuesped());
            ps.setString(2, huesped.getNombre());
            ps.setString(3, huesped.getDocumento());
            ps.setDate(4, Date.valueOf(huesped.getFechaNacimiento()));
            ps.setString(5, huesped.getDireccion());
            ps.setString(6, huesped.getCorreo());
            ps.setString(7, huesped.getTelefono());
            ps.executeUpdate();
            return id;
        }
    }

    public Huesped buscarPorId(Long idHuesped) throws SQLException {
        if (idHuesped == null) {
            return null;
        }
        String sql = "SELECT id_huesped, nombre, documento, fecha_nacimiento, direccion, correo, telefono "
                   + "FROM Huesped WHERE id_huesped = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idHuesped);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearHuesped(rs);
                }
            }
        }
        return null;
    }

    public Huesped buscarPorDocumento(String documento) throws SQLException {
        if (documento == null || documento.isBlank()) {
            return null;
        }
        String sql = "SELECT id_huesped, nombre, documento, fecha_nacimiento, direccion, correo, telefono "
                   + "FROM Huesped WHERE documento = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, documento.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearHuesped(rs);
                }
            }
        }
        return null;
    }

    public List<Huesped> listarTodos() throws SQLException {
        List<Huesped> lista = new ArrayList<>();
        String sql = "SELECT id_huesped, nombre, documento, fecha_nacimiento, direccion, correo, telefono "
                   + "FROM Huesped ORDER BY nombre";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearHuesped(rs));
            }
        }
        return lista;
    }

    public Long obtenerSiguienteId() throws SQLException {
        String sql = "SELECT NVL(MAX(id_huesped), 0) + 1 AS siguiente_id FROM Huesped";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong("siguiente_id");
            }
        }
        return 1L;
    }

    private Huesped mapearHuesped(ResultSet rs) throws SQLException {
        Huesped h = new Huesped();
        h.setIdHuesped(rs.getLong("id_huesped"));
        h.setNombre(rs.getString("nombre"));
        h.setDocumento(rs.getString("documento"));
        Date fecha = rs.getDate("fecha_nacimiento");
        if (fecha != null) {
            h.setFechaNacimiento(fecha.toLocalDate());
        }
        h.setDireccion(rs.getString("direccion"));
        h.setCorreo(rs.getString("correo"));
        h.setTelefono(rs.getString("telefono"));
        return h;
    }
}
