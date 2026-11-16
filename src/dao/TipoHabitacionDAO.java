package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.TipoHabitacion;
import utils.ConexionBD;

/**
 * Data Access Object (DAO) para la entidad Tipo_Habitacion.
 */
public class TipoHabitacionDAO {

    public TipoHabitacion buscarPorId(Long idTipo) throws SQLException {
        if (idTipo == null) {
            return null;
        }
        String sql = "SELECT id_tipo_habitacion, nombre_tipo, descripcion, capacidad_estandar FROM Tipo_Habitacion WHERE id_tipo_habitacion = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idTipo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearTipo(rs);
                }
            }
        }
        return null;
    }

    public List<TipoHabitacion> listarTodos() throws SQLException {
        List<TipoHabitacion> lista = new ArrayList<>();
        String sql = "SELECT id_tipo_habitacion, nombre_tipo, descripcion, capacidad_estandar FROM Tipo_Habitacion ORDER BY nombre_tipo";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearTipo(rs));
            }
        }
        return lista;
    }

    private TipoHabitacion mapearTipo(ResultSet rs) throws SQLException {
        TipoHabitacion t = new TipoHabitacion();
        t.setIdTipoHabitacion(rs.getLong("id_tipo_habitacion"));
        t.setNombreTipo(rs.getString("nombre_tipo"));
        t.setDescripcion(rs.getString("descripcion"));
        t.setCapacidadEstandar(rs.getInt("capacidad_estandar"));
        return t;
    }
}
