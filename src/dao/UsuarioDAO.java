package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import model.UsuarioSistema;
import utils.ConexionBD;
import utils.SeguridadUtil;

/**
 * Data Access Object (DAO) para la entidad Usuario_Sistema.
 * Gestiona autenticación, búsqueda y registro de usuarios en Oracle XE.
 */
public class UsuarioDAO {

    /**
     * Autentica un usuario contra la base de datos comparando el hash de la contraseña.
     *
     * @param nombreUsuario Nombre de usuario.
     * @param contrasenaPlana Contraseña en texto plano.
     * @return UsuarioSistema autenticado o null si las credenciales son inválidas o está inactivo.
     * @throws SQLException si ocurre un error de base de datos.
     */
    public UsuarioSistema autenticar(String nombreUsuario, String contrasenaPlana) throws SQLException {
        if (nombreUsuario == null || contrasenaPlana == null || nombreUsuario.isBlank()) {
            return null;
        }

        String sql = "SELECT id_usuario, nombre_usuario, contrasena_hash, estado, id_rol, id_personal "
                   + "FROM Usuario_Sistema WHERE UPPER(nombre_usuario) = UPPER(?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashAlmacenado = rs.getString("contrasena_hash");
                    String estado = rs.getString("estado");

                    if (!"ACTIVO".equalsIgnoreCase(estado)) {
                        return null; // Usuario bloqueado o inactivo
                    }

                    if (SeguridadUtil.verificarContrasena(contrasenaPlana.trim(), hashAlmacenado)) {
                        return mapearUsuario(rs);
                    }
                }
            }
        }
        return null;
    }

    public UsuarioSistema buscarPorNombreUsuario(String nombreUsuario) throws SQLException {
        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            return null;
        }
        String sql = "SELECT id_usuario, nombre_usuario, contrasena_hash, estado, id_rol, id_personal "
                   + "FROM Usuario_Sistema WHERE UPPER(nombre_usuario) = UPPER(?)";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        }
        return null;
    }

    public UsuarioSistema buscarPorId(Long idUsuario) throws SQLException {
        if (idUsuario == null) {
            return null;
        }
        String sql = "SELECT id_usuario, nombre_usuario, contrasena_hash, estado, id_rol, id_personal "
                   + "FROM Usuario_Sistema WHERE id_usuario = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        }
        return null;
    }

    /**
     * Registra un nuevo usuario en la base de datos con contraseña hasheada en SHA-256.
     *
     * @param usuario Datos del usuario.
     * @param contrasenaPlana Contraseña en texto plano a hashear.
     * @return ID generado o asignado del nuevo usuario.
     * @throws SQLException si ocurre un error o el usuario ya existe.
     */
    public Long registrarUsuario(UsuarioSistema usuario, String contrasenaPlana) throws SQLException {
        try (Connection conn = ConexionBD.getConexion()) {
            return registrarUsuario(conn, usuario, contrasenaPlana);
        }
    }

    /**
     * Inserta el usuario reutilizando una conexión existente, de modo que
     * pueda participar en una transacción junto con otros DAO.
     */
    public Long registrarUsuario(Connection conn, UsuarioSistema usuario, String contrasenaPlana) throws SQLException {
        if (usuario == null || contrasenaPlana == null || contrasenaPlana.isBlank()) {
            throw new IllegalArgumentException("El usuario y la contraseña no pueden estar vacíos.");
        }

        // Obtener siguiente ID disponible si no viene especificado
        Long id = usuario.getIdUsuario();
        if (id == null) {
            id = obtenerSiguienteId(conn);
            usuario.setIdUsuario(id);
        }

        String hash = SeguridadUtil.sha256(contrasenaPlana.trim());
        usuario.setContrasenaHash(hash);
        if (usuario.getEstado() == null || usuario.getEstado().isBlank()) {
            usuario.setEstado("ACTIVO");
        }

        String sql = "INSERT INTO Usuario_Sistema (id_usuario, nombre_usuario, contrasena_hash, estado, id_rol, id_personal) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, usuario.getIdUsuario());
            ps.setString(2, usuario.getNombreUsuario().trim());
            ps.setString(3, usuario.getContrasenaHash());
            ps.setString(4, usuario.getEstado());
            ps.setLong(5, usuario.getIdRol());
            if (usuario.getIdPersonal() != null) {
                ps.setLong(6, usuario.getIdPersonal());
            } else {
                ps.setNull(6, Types.NUMERIC);
            }
            ps.executeUpdate();
            return id;
        }
    }

    /**
     * Obtiene el nombre real de la persona asociada a este usuario si existe.
     */
    public String obtenerNombrePersona(UsuarioSistema usuario) {
        if (usuario == null) {
            return "Invitado";
        }
        if (usuario.getIdPersonal() != null) {
            String sql = "SELECT nombre FROM Personal WHERE id_personal = ?";
            try (Connection conn = ConexionBD.getConexion();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, usuario.getIdPersonal());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("nombre");
                    }
                }
            } catch (SQLException ignored) {
            }
        }
        return usuario.getNombreUsuario();
    }

    public Long obtenerSiguienteId() throws SQLException {
        try (Connection conn = ConexionBD.getConexion()) {
            return obtenerSiguienteId(conn);
        }
    }

    public Long obtenerSiguienteId(Connection conn) throws SQLException {
        String sql = "SELECT NVL(MAX(id_usuario), 0) + 1 AS siguiente_id FROM Usuario_Sistema";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong("siguiente_id");
            }
        }
        return 1L;
    }

    private UsuarioSistema mapearUsuario(ResultSet rs) throws SQLException {
        UsuarioSistema u = new UsuarioSistema();
        u.setIdUsuario(rs.getLong("id_usuario"));
        u.setNombreUsuario(rs.getString("nombre_usuario"));
        u.setContrasenaHash(rs.getString("contrasena_hash"));
        u.setEstado(rs.getString("estado"));
        u.setIdRol(rs.getLong("id_rol"));
        long idPersonal = rs.getLong("id_personal");
        if (!rs.wasNull()) {
            u.setIdPersonal(idPersonal);
        }
        return u;
    }
}
