package ViewController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;

import dao.ServicioDAO;
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
import model.Servicio;

public class ServiciosController {

    private final ServicioDAO servicioDAO = new ServicioDAO();

    @FXML
    private TableView<Servicio> tablaServicios;
    @FXML
    private TableColumn<Servicio, Number> colIdServicio;
    @FXML
    private TableColumn<Servicio, String> colNombreServicio;
    @FXML
    private TableColumn<Servicio, String> colTipoServicio;
    @FXML
    private TableColumn<Servicio, LocalDateTime> colHoraApertura;
    @FXML
    private TableColumn<Servicio, LocalDateTime> colHoraCierre;
    @FXML
    private TableColumn<Servicio, String> colUsoExclusivo;
    @FXML
    private TableColumn<Servicio, Number> colIdAlojamientoServicio;

    @FXML
    private void initialize() {
        colIdServicio.setCellValueFactory(new PropertyValueFactory<>("idServicio"));
        colNombreServicio.setCellValueFactory(new PropertyValueFactory<>("nombreServicio"));
        colTipoServicio.setCellValueFactory(new PropertyValueFactory<>("tipoServicio"));
        colHoraApertura.setCellValueFactory(new PropertyValueFactory<>("horaApertura"));
        colHoraCierre.setCellValueFactory(new PropertyValueFactory<>("horaCierre"));
        colUsoExclusivo.setCellValueFactory(new PropertyValueFactory<>("usoExclusivo"));
        colIdAlojamientoServicio.setCellValueFactory(new PropertyValueFactory<>("idAlojamiento"));
        recargarTabla();
    }

    private void recargarTabla() {
        try {
            tablaServicios.setItems(FXCollections.observableArrayList(servicioDAO.listarTodos()));
        } catch (SQLException e) {
            mostrarAlerta(null, Alert.AlertType.ERROR, "No se pudieron cargar los servicios", e.getMessage());
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
