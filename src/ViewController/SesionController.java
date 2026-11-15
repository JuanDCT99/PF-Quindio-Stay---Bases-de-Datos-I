package ViewController;

import java.io.IOException;

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

        cambiarVista(event, "/Resources/Dashboard.fxml", "Reservas");
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
