package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Personal;
import utils.ConexionBD;

/**
 * Data Access Object (DAO) para la entidad Personal.
 */
public class PersonalDAO {

    public Long registrarPersonal(Personal personal) throws SQLException {
        if (personal == null) {
            throw new IllegalArgumentException("Personal no puede ser nulo.");
        }

        Long id = personal.getIdPersonal();
        if (id == null) {
            id = obtenerSiguienteId();
            personal.setIdPersonal(id);
        }

        if (personal.getSalario() == null) {
            personal.setSalario(new BigDecimal("1300000.00"));
        }
        if (personal.getIdAlojamiento() == null) {
            personal.setIdAlojamiento(1L); // Alojamiento predeterminado
        }

        String sql = "INSERT INTO Personal (id_personal, nombre, funcion, cargo, salario, horario_trabajo, id_alojamiento) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, personal.getIdPersonal());
            ps.setString(2, personal.getNombre());
            ps.setString(3, personal.getFuncion());
            ps.setString(4, personal.getCargo());
            ps.setBigDecimal(5, personal.getSalario());
            ps.setString(6, personal.getHorarioTrabajo());
            ps.setLong(7, personal.getIdAlojamiento());
            ps.executeUpdate();
            return id;
        }
    }

    public Personal buscarPorId(Long idPersonal) throws SQLException {
        if (idPersonal == null) {
            return null;
        }
        String sql = "SELECT id_personal, nombre, funcion, cargo, salario, horario_trabajo, id_alojamiento "
                   + "FROM Personal WHERE id_personal = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idPersonal);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPersonal(rs);
                }
            }
        }
        return null;
    }

    public List<Personal> listarTodos() throws SQLException {
        List<Personal> lista = new ArrayList<>();
        String sql = "SELECT id_personal, nombre, funcion, cargo, salario, horario_trabajo, id_alojamiento "
                   + "FROM Personal ORDER BY nombre";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearPersonal(rs));
            }
        }
        return lista;
    }

    public Long obtenerSiguienteId() throws SQLException {
        String sql = "SELECT NVL(MAX(id_personal), 0) + 1 AS siguiente_id FROM Personal";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong("siguiente_id");
            }
        }
        return 1L;
    }

    private Personal mapearPersonal(ResultSet rs) throws SQLException {
        Personal p = new Personal();
        p.setIdPersonal(rs.getLong("id_personal"));
        p.setNombre(rs.getString("nombre"));
        p.setFuncion(rs.getString("funcion"));
        p.setCargo(rs.getString("cargo"));
        p.setSalario(rs.getBigDecimal("salario"));
        p.setHorarioTrabajo(rs.getString("horario_trabajo"));
        p.setIdAlojamiento(rs.getLong("id_alojamiento"));
        return p;
    }
}
