package ViewController;

import java.io.IOException;
import java.sql.SQLException;

import dao.RolDAO;
import dao.UsuarioDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Rol;
import model.UsuarioSistema;
import utils.ConexionBD;
import utils.SesionUsuario;

public class SesionController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private TextField txtContrasena;

    @FXML
    private Button btnIniciarSesion;

    @FXML
    private Button btnCrearCuenta;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final RolDAO rolDAO = new RolDAO();

    @FXML
    private void initialize() {
        if (txtUsuario != null) {
            txtUsuario.requestFocus();
        }
    }

    @FXML
    private void iniciarSesion(ActionEvent event) {
        if (esVacio(txtUsuario) || esVacio(txtContrasena)) {
            mostrarAlerta(event, Alert.AlertType.WARNING, "Campos requeridos",
                    "Ingrese su usuario y contraseña para continuar.");
            return;
        }

        String username = txtUsuario.getText().trim();
        String password = txtContrasena.getText().trim();

        try {
            UsuarioSistema usuario = usuarioDAO.autenticar(username, password);
            if (usuario == null) {
                mostrarAlerta(event, Alert.AlertType.ERROR, "Acceso denegado",
                        "Usuario o contraseña incorrectos, o la cuenta se encuentra inactiva.\n\n"
                        + "Usuario de prueba: admin_demo\n"
                        + "Contraseña: Admin123");
                return;
            }

            Rol rol = rolDAO.buscarPorId(usuario.getIdRol());
            String nombrePersona = usuarioDAO.obtenerNombrePersona(usuario);
            SesionUsuario.iniciarSesion(usuario, rol, nombrePersona);

            // Redirigir según el rol
            String vista = "/Resources/Dashboard.fxml";
            String titulo = "Reservas (" + SesionUsuario.getNombreRol() + ")";
            if (SesionUsuario.esHuesped()) {
                vista = "/Resources/Perfil.fxml";
                titulo = "Mi Perfil de Huésped";
            }

            cambiarVista(event, vista, titulo);

        } catch (SQLException e) {
            mostrarAlerta(event, Alert.AlertType.ERROR, "Error de Conexión a Base de Datos",
                    "No se pudo conectar a Oracle XE (" + ConexionBD.getUrl() + ").\n"
                    + "Verifique que el servicio de base de datos esté activo.\n\n"
                    + "Detalle: " + e.getMessage());
        }
    }

    @FXML
    private void crearCuenta(ActionEvent event) {
        cambiarVista(event, "/Resources/SeleccionRol.fxml", "Selección de Rol");
    }

    private void cambiarVista(ActionEvent event, String ruta, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("QuindíoStay | " + titulo);
            stage.setMinWidth(1100);
            stage.setMinHeight(580);
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            mostrarAlerta(event, Alert.AlertType.ERROR,
                    "No se pudo abrir la pantalla de " + titulo.toLowerCase(), e.getMessage());
        }
    }

    private boolean esVacio(TextField campo) {
        return campo == null || campo.getText() == null || campo.getText().isBlank();
    }

    private void mostrarAlerta(ActionEvent event, Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.initOwner(((Node) event.getSource()).getScene().getWindow());
        alerta.setTitle("QuindíoStay");
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
