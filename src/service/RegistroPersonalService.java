package service;

import java.sql.Connection;
import java.sql.SQLException;

import dao.PersonalDAO;
import dao.UsuarioDAO;
import model.Personal;
import model.UsuarioSistema;
import utils.ConexionBD;

/**
 * Registra una persona de personal y su usuario de acceso en una sola transacción.
 * Si falla el usuario, la inserción de personal se revierte para no dejar registros huérfanos.
 */
public class RegistroPersonalService {

    private final PersonalDAO personalDAO = new PersonalDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Long registrar(Personal personal, Long idRol, String nombreUsuario, String contrasena) throws SQLException {
        try (Connection conn = ConexionBD.getConexion()) {
            boolean autoCommitOriginal = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try {
                Long idPersonal = personalDAO.registrarPersonal(conn, personal);

                UsuarioSistema usuario = new UsuarioSistema(
                        null,
                        nombreUsuario,
                        null,
                        "ACTIVO",
                        idRol,
                        idPersonal
                );
                usuarioDAO.registrarUsuario(conn, usuario, contrasena);

                conn.commit();
                return idPersonal;
            } catch (SQLException | RuntimeException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(autoCommitOriginal);
            }
        }
    }
}
