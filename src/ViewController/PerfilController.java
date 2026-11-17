package ViewController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;

import dao.HuespedDAO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Huesped;

public class PerfilController {

    private final HuespedDAO huespedDAO = new HuespedDAO();

    @FXML
    private TableView<Huesped> tablaPerfil;
    @FXML
    private TableColumn<Huesped, Number> colIdHuesped;
    @FXML
    private TableColumn<Huesped, String> colNombreHuesped;
    @FXML
    private TableColumn<Huesped, String> colDocumento;
    @FXML
    private TableColumn<Huesped, LocalDate> colFechaNacimiento;
    @FXML
    private TableColumn<Huesped, String> colDireccion;
    @FXML
    private TableColumn<Huesped, String> colCorreo;
    @FXML
    private TableColumn<Huesped, String> colTelefono;

    @FXML
    private void initialize() {
        colIdHuesped.setCellValueFactory(new PropertyValueFactory<>("idHuesped"));
        colNombreHuesped.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("documento"));
        colFechaNacimiento.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        recargarTabla();
    }

    private void recargarTabla() {
        try {
            tablaPerfil.setItems(FXCollections.observableArrayList(huespedDAO.listarTodos()));
        } catch (SQLException e) {
            mostrarAlerta(null, Alert.AlertType.ERROR, "No se pudo cargar el perfil", e.getMessage());
        }
    }

    private void mostrarAlerta(ActionEvent event, Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        if (event != null && event.getSource() instanceof Node) {
            alerta.initOwner(((Node) event.getSource()).getScene().getWindow());
        }
        alerta.setTitle("QuindíoStay");
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML
    private void irAHabitacion(ActionEvent event) {
        navegar(event, "/Resources/Habitacion.fxml", "Habitación");
    }

    @FXML
    private void irAServicios(ActionEvent event) {
        navegar(event, "/Resources/Servicios.fxml", "Servicios");
    }

    @FXML
    private void irAPago(ActionEvent event) {
        navegar(event, "/Resources/Pago.fxml", "Pago");
    }

    @FXML
    private void irASancion(ActionEvent event) {
        navegar(event, "/Resources/Sancion.fxml", "Sanción");
    }

    @FXML
    private void irAReportes(ActionEvent event) {
        navegar(event, "/Resources/Reportes.fxml", "Reportes");
    }

    @FXML
    private void irAPerfil(ActionEvent event) {
        navegar(event, "/Resources/Perfil.fxml", "Perfil");
    }

    @FXML
    private void irAReservas(ActionEvent event) {
        navegar(event, "/Resources/Dashboard.fxml", "Reservas");
    }

    private void navegar(ActionEvent event, String ruta, String titulo) {
        try {
            URL recurso = getClass().getResource(ruta);
            if (recurso == null) {
                throw new IOException("No se encontró el recurso " + ruta);
            }

            FXMLLoader loader = new FXMLLoader(recurso);
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
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.initOwner(((Node) event.getSource()).getScene().getWindow());
            alerta.setTitle("QuindíoStay");
            alerta.setHeaderText("No se pudo abrir la pantalla de " + titulo.toLowerCase());
            alerta.setContentText(e.getMessage());
            alerta.showAndWait();
        }
    }
}
