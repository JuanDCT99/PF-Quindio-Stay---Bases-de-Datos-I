package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Plan;
import utils.ConexionBD;

/**
 * Data Access Object (DAO) para la entidad Plan.
 */
public class PlanDAO {

    public Plan buscarPorId(Long idPlan) throws SQLException {
        if (idPlan == null) {
            return null;
        }
        String sql = "SELECT id_plan, nombre_plan, descripcion, incluye_desayuno, valor_adicional_noche, id_alojamiento "
                   + "FROM Plan WHERE id_plan = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idPlan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPlan(rs);
                }
            }
        }
        return null;
    }

    public List<Plan> listarTodos() throws SQLException {
        List<Plan> lista = new ArrayList<>();
        String sql = "SELECT id_plan, nombre_plan, descripcion, incluye_desayuno, valor_adicional_noche, id_alojamiento "
                   + "FROM Plan ORDER BY nombre_plan";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearPlan(rs));
            }
        }
        return lista;
    }

    private Plan mapearPlan(ResultSet rs) throws SQLException {
        Plan plan = new Plan();
        plan.setIdPlan(rs.getLong("id_plan"));
        plan.setNombrePlan(rs.getString("nombre_plan"));
        plan.setDescripcion(rs.getString("descripcion"));
        plan.setIncluyeDesayuno(rs.getString("incluye_desayuno"));
        plan.setValorAdicionalNoche(rs.getBigDecimal("valor_adicional_noche"));
        plan.setIdAlojamiento(rs.getLong("id_alojamiento"));
        return plan;
    }
}
