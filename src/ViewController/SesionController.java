package ViewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SesionController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private TextField txtContrasena;

    @FXML
    private Button btnIniciarSesion;

    @FXML
    private Button btnCrearCuenta;

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

        mostrarAlerta(event, Alert.AlertType.INFORMATION, "Inicio de sesión",
                "La autenticación se conectará con la capa de servicios y la base de datos.");
    }

    @FXML
    private void crearCuenta(ActionEvent event) {
        mostrarAlerta(event, Alert.AlertType.INFORMATION, "Crear cuenta",
                "El registro de nuevos huéspedes se implementará en una vista posterior.");
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
