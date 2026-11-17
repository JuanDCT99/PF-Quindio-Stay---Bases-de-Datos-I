package utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestor de conexiones JDBC para Oracle Database (Oracle XE / Free).
 * Lee la configuración desde el archivo db.properties en classpath
 * con soporte para variables de entorno y valores por defecto.
 */
public class ConexionBD {

    private static String url = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
    private static String usuario = "QUINDIOSTAY";
    private static String contrasena = "Quindio123";

    static {
        cargarConfiguracion();
    }

    private ConexionBD() {
    }

    private static void cargarConfiguracion() {
        try (InputStream is = ConexionBD.class.getResourceAsStream("/Resources/db.properties")) {
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                if (props.getProperty("db.url") != null && !props.getProperty("db.url").isBlank()) {
                    url = props.getProperty("db.url").trim();
                }
                if (props.getProperty("db.user") != null && !props.getProperty("db.user").isBlank()) {
                    usuario = props.getProperty("db.user").trim();
                }
                if (props.getProperty("db.password") != null && !props.getProperty("db.password").isBlank()) {
                    contrasena = props.getProperty("db.password").trim();
                }
            }
        } catch (Exception e) {
            System.err.println("Advertencia: No se pudo leer db.properties. Usando valores por defecto. Error: " + e.getMessage());
        }

        // Sobrescribir con variables de entorno si existen
        String envUrl = System.getenv("DB_URL");
        if (envUrl != null && !envUrl.isBlank()) {
            url = envUrl.trim();
        }
        String envUser = System.getenv("DB_USER");
        if (envUser != null && !envUser.isBlank()) {
            usuario = envUser.trim();
        }
        String envPass = System.getenv("DB_PASSWORD");
        if (envPass != null && !envPass.isBlank()) {
            contrasena = envPass.trim();
        }

        // Registrar driver explícitamente por compatibilidad
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException ignored) {
            // El driver se auto-registra en JDBC 4.0+ con ojdbc11
        }
    }

    /**
     * Obtiene una nueva conexión a la base de datos Oracle.
     *
     * @return Connection activa.
     * @throws SQLException si ocurre un error al conectar.
     */
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(url, usuario, contrasena);
    }

    /**
     * Prueba si la conexión a la base de datos es exitosa.
     *
     * @return true si la conexión se establece correctamente.
     */
    public static boolean probarConexion() {
        try (Connection conn = getConexion()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Fallo de conexión a la base de datos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cierra de manera segura uno o más recursos JDBC (Connection, Statement, ResultSet).
     *
     * @param recursos Recursos a cerrar.
     */
    public static void cerrar(AutoCloseable... recursos) {
        if (recursos == null) {
            return;
        }
        for (AutoCloseable r : recursos) {
            if (r != null) {
                try {
                    r.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public static String getUrl() {
        return url;
    }

    public static String getUsuario() {
        return usuario;
    }
}
