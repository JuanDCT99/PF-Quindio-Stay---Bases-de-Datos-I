package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Validaciones de integridad referencial antes de ejecutar INSERT/UPDATE.
 * Los nombres de tabla y columna son constantes internas, nunca datos del usuario.
 */
public final class ValidacionesBD {

    private ValidacionesBD() {
    }

    public static void validarReserva(Connection conn, Long idReserva) throws SQLException {
        validarExistencia(conn, "Reserva", "id_reserva", idReserva, "la reserva");
    }

    public static void validarServicio(Connection conn, Long idServicio) throws SQLException {
        validarExistencia(conn, "Servicio", "id_servicio", idServicio, "el servicio");
    }

    public static void validarHuesped(Connection conn, Long idHuesped) throws SQLException {
        validarExistencia(conn, "Huesped", "id_huesped", idHuesped, "el huésped");
    }

    public static void validarHabitacion(Connection conn, Long idHabitacion) throws SQLException {
        validarExistencia(conn, "Habitacion", "id_habitacion", idHabitacion, "la habitación");
    }

    public static void validarTipoHabitacion(Connection conn, Long idTipo) throws SQLException {
        validarExistencia(conn, "Tipo_Habitacion", "id_tipo_habitacion", idTipo, "el tipo de habitación");
    }

    public static void validarAlojamiento(Connection conn, Long idAlojamiento) throws SQLException {
        validarExistencia(conn, "Alojamiento", "id_alojamiento", idAlojamiento, "el alojamiento");
    }

    public static void validarPlan(Connection conn, Long idPlan) throws SQLException {
        validarExistencia(conn, "Plan", "id_plan", idPlan, "el plan");
    }

    public static void validarRol(Connection conn, Long idRol) throws SQLException {
        validarExistencia(conn, "Rol", "id_rol", idRol, "el rol");
    }

    public static void validarPersonal(Connection conn, Long idPersonal) throws SQLException {
        validarExistencia(conn, "Personal", "id_personal", idPersonal, "el personal");
    }

    public static void validarIntermediario(Connection conn, Long idIntermediario) throws SQLException {
        validarExistencia(conn, "Intermediario", "id_intermediario", idIntermediario, "el intermediario");
    }

    public static void validarReservaHabitacion(Connection conn, Long idReservaHabitacion) throws SQLException {
        validarExistencia(conn, "Reserva_Habitacion", "id_reserva_habitacion", idReservaHabitacion,
                "la reserva de habitación");
    }

    public static void validarExistencia(Connection conn, String tabla, String columna, Long id, String etiqueta)
            throws SQLException {
        if (id == null) {
            throw new SQLException("Falta el identificador de " + etiqueta + ".");
        }
        String sql = "SELECT 1 FROM " + tabla + " WHERE " + columna + " = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("No existe " + etiqueta + " con ID " + id
                            + " en la base de datos. Revise el valor e intente de nuevo.");
                }
            }
        }
    }
}
