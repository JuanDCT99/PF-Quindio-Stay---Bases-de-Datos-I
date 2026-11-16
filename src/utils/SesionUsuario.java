package utils;

import model.Rol;
import model.UsuarioSistema;

/**
 * Mantiene la sesión del usuario autenticado en la aplicación.
 * Permite a los controladores verificar roles y permisos (RN-28).
 */
public class SesionUsuario {

    private static UsuarioSistema usuarioActual;
    private static Rol rolActual;
    private static String nombreCompleto;

    private SesionUsuario() {
    }

    public static void iniciarSesion(UsuarioSistema usuario, Rol rol, String nombre) {
        usuarioActual = usuario;
        rolActual = rol;
        nombreCompleto = (nombre != null && !nombre.isBlank()) ? nombre : usuario.getNombreUsuario();
    }

    public static void cerrarSesion() {
        usuarioActual = null;
        rolActual = null;
        nombreCompleto = null;
    }

    public static boolean haySesionIniciada() {
        return usuarioActual != null;
    }

    public static UsuarioSistema getUsuarioActual() {
        return usuarioActual;
    }

    public static Rol getRolActual() {
        return rolActual;
    }

    public static String getNombreCompleto() {
        return nombreCompleto;
    }

    public static String getNombreRol() {
        return (rolActual != null && rolActual.getNombreRol() != null)
                ? rolActual.getNombreRol().toUpperCase()
                : "INVITADO";
    }

    public static boolean esAdministrador() {
        return "ADMINISTRADOR".equalsIgnoreCase(getNombreRol());
    }

    public static boolean esRecepcionista() {
        return "RECEPCIONISTA".equalsIgnoreCase(getNombreRol());
    }

    public static boolean esHuesped() {
        return "HUESPED".equalsIgnoreCase(getNombreRol());
    }
}
