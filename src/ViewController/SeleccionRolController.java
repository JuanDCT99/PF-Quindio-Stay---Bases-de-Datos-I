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
import javafx.stage.Stage;

public class SeleccionRolController {

    @FXML
    private Button btnEntrarHuesped;

    @FXML
    private Button btnEntrarPersonal;

    @FXML
    private Button btnEntrarAdmin;

    @FXML
    private Button btnVolver;

    @FXML
    private void initialize() {
        if (btnEntrarHuesped != null) {
            btnEntrarHuesped.setOnAction(event -> abrirRegistro(event, "/Resources/RegistroHuesped.fxml", "Huésped"));
        }
        if (btnEntrarPersonal != null) {
            btnEntrarPersonal.setOnAction(event -> abrirRegistro(event, "/Resources/RegistroPersonal.fxml", "Personal"));
        }
        if (btnEntrarAdmin != null) {
            btnEntrarAdmin.setOnAction(event -> abrirRegistro(event, "/Resources/RegistroAdmin.fxml", "Administrador"));
        }
        if (btnVolver != null) {
            btnVolver.setOnAction(this::volverAlInicioSesion);
        }
    }

    private void volverAlInicioSesion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/sesion.fxml"));
            Parent root = loader.load();

            Stage stage = obtenerVentana(event);
            stage.setScene(new Scene(root));
            stage.setTitle("QuindíoStay | Inicio de Sesión");
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.initOwner(obtenerVentana(event));
            alerta.setTitle("QuindíoStay");
            alerta.setHeaderText("No se pudo volver al inicio de sesión");
            alerta.setContentText(e.getMessage());
            alerta.showAndWait();
        }
    }

    private void abrirRegistro(ActionEvent event, String ruta, String rol) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
            Parent root = loader.load();

            Stage stage = obtenerVentana(event);
            stage.setScene(new Scene(root));
            stage.setTitle("QuindíoStay | Registro de " + rol);
            stage.setMinWidth(1100);
            stage.setMinHeight(580);
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.initOwner(obtenerVentana(event));
            alerta.setTitle("QuindíoStay");
            alerta.setHeaderText("No se pudo abrir el registro de " + rol.toLowerCase());
            alerta.setContentText(e.getMessage());
            alerta.showAndWait();
        }
    }

    private Stage obtenerVentana(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }
}
