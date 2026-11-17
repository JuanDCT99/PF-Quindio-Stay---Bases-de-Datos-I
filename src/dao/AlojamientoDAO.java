package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Alojamiento;
import utils.ConexionBD;

/**
 * Data Access Object (DAO) para la entidad Alojamiento.
 */
public class AlojamientoDAO {

    public Alojamiento buscarPorId(Long idAlojamiento) throws SQLException {
        if (idAlojamiento == null) {
            return null;
        }
        String sql = "SELECT id_alojamiento, nombre_alojamiento, nit, direccion, telefono, acepta_mascotas, hora_salida_limite, plazo_confirmacion_horas, id_municipio "
                   + "FROM Alojamiento WHERE id_alojamiento = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idAlojamiento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearAlojamiento(rs);
                }
            }
        }
        return null;
    }

    public List<Alojamiento> listarTodos() throws SQLException {
        List<Alojamiento> lista = new ArrayList<>();
        String sql = "SELECT id_alojamiento, nombre_alojamiento, nit, direccion, telefono, acepta_mascotas, hora_salida_limite, plazo_confirmacion_horas, id_municipio "
                   + "FROM Alojamiento ORDER BY nombre_alojamiento";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearAlojamiento(rs));
            }
        }
        return lista;
    }

    private Alojamiento mapearAlojamiento(ResultSet rs) throws SQLException {
        Alojamiento a = new Alojamiento();
        a.setIdAlojamiento(rs.getLong("id_alojamiento"));
        a.setNombreAlojamiento(rs.getString("nombre_alojamiento"));
        a.setNit(rs.getString("nit"));
        a.setDireccion(rs.getString("direccion"));
        a.setTelefono(rs.getString("telefono"));
        a.setAceptaMascotas(rs.getString("acepta_mascotas"));
        java.sql.Timestamp ts = rs.getTimestamp("hora_salida_limite");
        if (ts != null) {
            a.setHoraSalidaLimite(ts.toLocalDateTime());
        }
        a.setPlazoConfirmacionHoras(rs.getInt("plazo_confirmacion_horas"));
        long idMun = rs.getLong("id_municipio");
        if (!rs.wasNull()) {
            a.setIdMunicipio(idMun);
        }
        return a;
    }
}
