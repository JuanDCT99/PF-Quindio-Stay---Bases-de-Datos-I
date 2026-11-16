package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilidad de seguridad para QuindíoStay.
 * Implementa el cálculo de hash SHA-256 para contraseñas de usuarios.
 */
public class SeguridadUtil {

    private SeguridadUtil() {
    }

    /**
     * Calcula el hash SHA-256 en formato hexadecimal en minúsculas.
     *
     * @param texto Cadena en texto plano a transformar.
     * @return Hash SHA-256 en 64 caracteres hexadecimales.
     */
    public static String sha256(String texto) {
        if (texto == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al calcular hash SHA-256", e);
        }
    }

    /**
     * Verifica si una contraseña plana coincide con el hash almacenado en base de datos.
     *
     * @param contrasenaPlana Contraseña suministrada por el usuario.
     * @param hashAlmacenado Hash almacenado en la tabla Usuario_Sistema.
     * @return true si coincide el hash calculado o si coincide textualmente (fallback).
     */
    public static boolean verificarContrasena(String contrasenaPlana, String hashAlmacenado) {
        if (contrasenaPlana == null || hashAlmacenado == null) {
            return false;
        }
        String hashCalculado = sha256(contrasenaPlana);
        return hashCalculado.equalsIgnoreCase(hashAlmacenado) || contrasenaPlana.equals(hashAlmacenado);
    }
}
