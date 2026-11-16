package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Rol;
import utils.ConexionBD;

/**
 * Data Access Object (DAO) para la entidad Rol.
 */
public class RolDAO {

    public Rol buscarPorId(Long idRol) throws SQLException {
        if (idRol == null) {
            return null;
        }
        String sql = "SELECT id_rol, nombre_rol, descripcion FROM Rol WHERE id_rol = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idRol);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearRol(rs);
                }
            }
        }
        return null;
    }

    public Rol buscarPorNombre(String nombreRol) throws SQLException {
        if (nombreRol == null || nombreRol.isBlank()) {
            return null;
        }
        String sql = "SELECT id_rol, nombre_rol, descripcion FROM Rol WHERE UPPER(nombre_rol) = UPPER(?)";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombreRol.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearRol(rs);
                }
            }
        }
        return null;
    }

    public List<Rol> listarTodos() throws SQLException {
        List<Rol> lista = new ArrayList<>();
        String sql = "SELECT id_rol, nombre_rol, descripcion FROM Rol ORDER BY id_rol";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearRol(rs));
            }
        }
        return lista;
    }

    private Rol mapearRol(ResultSet rs) throws SQLException {
        Rol rol = new Rol();
        rol.setIdRol(rs.getLong("id_rol"));
        rol.setNombreRol(rs.getString("nombre_rol"));
        rol.setDescripcion(rs.getString("descripcion"));
        return rol;
    }
}
