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
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class BienvenidaController {

    @FXML
    private Button btnEntrar;

    @FXML
    private ImageView imagenBienvenida;

    @FXML
    private StackPane panelImagen;

    @FXML
    private void initialize() {
        if (imagenBienvenida != null && panelImagen != null) {
            imagenBienvenida.fitWidthProperty().bind(panelImagen.widthProperty());
            imagenBienvenida.fitHeightProperty().bind(panelImagen.heightProperty());
        }

        if (btnEntrar != null) {
            btnEntrar.requestFocus();
        }
    }

    @FXML
    private void entrar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/sesion.fxml"));
            Parent root = loader.load();

            Stage stage = obtenerVentana(event);
            stage.setScene(new Scene(root));
            stage.setTitle("QuindíoStay | Inicio de Sesión");
            stage.setMinWidth(1100);
            stage.setMinHeight(580);
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.initOwner(obtenerVentana(event));
            alerta.setTitle("QuindíoStay");
            alerta.setHeaderText("No se pudo abrir el inicio de sesión");
            alerta.setContentText(e.getMessage());
            alerta.showAndWait();
        }
    }

    @FXML
    private void salir(ActionEvent event) {
        obtenerVentana(event).close();
    }

    private Stage obtenerVentana(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }
}
