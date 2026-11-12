package ViewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.initOwner(obtenerVentana(event));
        alerta.setTitle("QuindíoStay");
        alerta.setHeaderText("Bienvenido a QuindíoStay");
        alerta.setContentText("El módulo de inicio de sesión se conectará en la siguiente pantalla.");
        alerta.showAndWait();
    }

    @FXML
    private void salir(ActionEvent event) {
        obtenerVentana(event).close();
    }

    private Stage obtenerVentana(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }
}
