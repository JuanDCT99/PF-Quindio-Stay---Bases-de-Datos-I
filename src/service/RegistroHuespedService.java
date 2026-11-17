package service;

import java.sql.Connection;
import java.sql.SQLException;

import dao.HuespedDAO;
import dao.RolDAO;
import dao.UsuarioDAO;
import model.Huesped;
import model.Rol;
import model.UsuarioSistema;
import utils.ConexionBD;

/**
 * Registra un huésped y su usuario de acceso dentro de una única transacción.
 * Si falla cualquiera de las dos inserciones se revierte todo, evitando
 * huéspedes huérfanos cuando el nombre de usuario ya existe.
 */
public class RegistroHuespedService {

    private final HuespedDAO huespedDAO = new HuespedDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final RolDAO rolDAO = new RolDAO();

    public Long registrar(Huesped huesped, String nombreUsuario, String contrasena) throws SQLException {
        try (Connection conn = ConexionBD.getConexion()) {
            boolean autoCommitOriginal = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try {
                Long idHuesped = huespedDAO.registrarHuesped(conn, huesped);

                Rol rolHuesped = rolDAO.buscarPorNombre(conn, "HUESPED");
                Long idRol = (rolHuesped != null && rolHuesped.getIdRol() != null)
                        ? rolHuesped.getIdRol()
                        : 3L;

                UsuarioSistema usuario = new UsuarioSistema(
                        null,
                        nombreUsuario,
                        null,
                        "ACTIVO",
                        idRol,
                        null
                );
                usuarioDAO.registrarUsuario(conn, usuario, contrasena);

                conn.commit();
                return idHuesped;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(autoCommitOriginal);
            }
        }
    }
}
