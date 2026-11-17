import utils.ConexionBD;
public class TestConexion {
    public static void main(String[] args) throws Exception {
        System.out.println("URL: " + ConexionBD.getUrl());
        System.out.println("USUARIO: " + ConexionBD.getUsuario());
        System.out.println("PROBANDO CONEXION...");
        boolean ok = ConexionBD.probarConexion();
        if (ok) {
            System.out.println("CONEXION EXITOSA");
        } else {
            System.out.println("CONEXION FALLIDA");
        }
    }
}
