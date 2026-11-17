package ViewController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;

import dao.HabitacionDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import model.Habitacion;

public class HabitacionController {

    private final HabitacionDAO habitacionDAO = new HabitacionDAO();

    @FXML
    private void crearHabitacion(ActionEvent event) {
        TextInputDialog dialogNum = new TextInputDialog("101");
        dialogNum.setTitle("Nueva Habitación | QuindíoStay");
        dialogNum.setHeaderText("Registro de Habitación");
        dialogNum.setContentText("Número de habitación (Ej: 101, C-02):");
        Optional<String> resNum = dialogNum.showAndWait();
        if (resNum.isEmpty()) return;

        TextInputDialog dialogCap = new TextInputDialog("2");
        dialogCap.setTitle("Nueva Habitación | QuindíoStay");
        dialogCap.setHeaderText("Capacidad máxima");
        dialogCap.setContentText("Capacidad de personas (1 - 10):");
        Optional<String> resCap = dialogCap.showAndWait();
        if (resCap.isEmpty()) return;

        ChoiceDialog<String> tipoDialog = new ChoiceDialog<>("Estándar", "Estándar", "Suite", "Cabaña Familiar");
        tipoDialog.setTitle("Tipo de Habitación");
        tipoDialog.setHeaderText("Categoría");
        tipoDialog.setContentText("Seleccione tipo:");
        Optional<String> resTipo = tipoDialog.showAndWait();
        if (resTipo.isEmpty()) return;

        try {
            int cap = Integer.parseInt(resCap.get().trim());
            Long idTipo = 1L;
            if ("Suite".equals(resTipo.get())) idTipo = 2L;
            else if ("Cabaña Familiar".equals(resTipo.get())) idTipo = 3L;

            Habitacion h = new Habitacion();
            h.setNumeroHabitacion(resNum.get().trim());
            h.setCapacidadMaxima(cap);
            h.setDescripcion("Habitación " + resTipo.get() + " con vista a los cafetales");
            h.setEstadoOperativo("DISPONIBLE");
            h.setIdTipoHabitacion(idTipo);
            h.setIdAlojamiento(1L); // Alojamiento principal

            Long idGen = habitacionDAO.crearHabitacion(h);

            mostrarAlerta(event, Alert.AlertType.INFORMATION, "Habitación Creada",
                    "Habitación #" + h.getNumeroHabitacion() + " (ID: " + idGen + ") registrada en Oracle XE.\n"
                    + "Estado: DISPONIBLE | Capacidad: " + cap + " personas.");

        } catch (NumberFormatException e) {
            mostrarAlerta(event, Alert.AlertType.WARNING, "Dato inválido", "La capacidad debe ser un número entero.");
        } catch (SQLException e) {
            mostrarAlerta(event, Alert.AlertType.ERROR, "Error de base de datos", e.getMessage());
        }
    }

    @FXML
    private void editarHabitacion(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Cambiar Estado | QuindíoStay");
        dialog.setHeaderText("Estado Operativo de Habitación");
        dialog.setContentText("Ingrese el ID de la habitación:");
        Optional<String> resId = dialog.showAndWait();
        if (resId.isEmpty()) return;

        try {
            Long idHab = Long.parseLong(resId.get().trim());
            Habitacion h = habitacionDAO.buscarPorId(idHab);
            if (h == null) {
                mostrarAlerta(event, Alert.AlertType.WARNING, "No encontrada", "La habitación con ID " + idHab + " no existe.");
                return;
            }

            ChoiceDialog<String> estadoDialog = new ChoiceDialog<>(h.getEstadoOperativo(), "DISPONIBLE", "MANTENIMIENTO", "FUERA_DE_SERVICIO");
            estadoDialog.setTitle("Estado Operativo");
            estadoDialog.setHeaderText("Habitación " + h.getNumeroHabitacion() + " (Estado actual: " + h.getEstadoOperativo() + ")");
            estadoDialog.setContentText("Nuevo estado:");
            Optional<String> nuevoEstado = estadoDialog.showAndWait();
            if (nuevoEstado.isEmpty()) return;

            habitacionDAO.cambiarEstadoOperativo(idHab, nuevoEstado.get());
            mostrarAlerta(event, Alert.AlertType.INFORMATION, "Estado Actualizado",
                    "La habitación " + h.getNumeroHabitacion() + " ahora está en estado: " + nuevoEstado.get());

        } catch (NumberFormatException e) {
            mostrarAlerta(event, Alert.AlertType.WARNING, "Dato inválido", "El ID debe ser numérico.");
        } catch (SQLException e) {
            mostrarAlerta(event, Alert.AlertType.ERROR, "Error de base de datos", e.getMessage());
        }
    }

    @FXML
    private void eliminarHabitacion(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Eliminar Habitación | QuindíoStay");
        dialog.setHeaderText("Eliminar o Bloquear Habitación");
        dialog.setContentText("Ingrese el ID de la habitación a eliminar:");
        Optional<String> resId = dialog.showAndWait();
        if (resId.isEmpty()) return;

        try {
            Long idHab = Long.parseLong(resId.get().trim());
            Habitacion h = habitacionDAO.buscarPorId(idHab);
            if (h == null) {
                mostrarAlerta(event, Alert.AlertType.WARNING, "No encontrada", "La habitación con ID " + idHab + " no existe.");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmar eliminación");
            confirm.setHeaderText("¿Desea eliminar la habitación " + h.getNumeroHabitacion() + "?");
            confirm.setContentText("Nota: Si tiene reservas asociadas, Oracle aplicará restricción de integridad referencial.");
            Optional<ButtonType> resp = confirm.showAndWait();

            if (resp.isPresent() && resp.get() == ButtonType.OK) {
                try {
                    habitacionDAO.eliminarHabitacion(idHab);
                    mostrarAlerta(event, Alert.AlertType.INFORMATION, "Eliminada", "Habitación eliminada de Oracle XE.");
                } catch (SQLException e) {
                    mostrarAlerta(event, Alert.AlertType.WARNING, "Restricción Referencial",
                            "No se puede eliminar la habitación porque tiene historial o reservas asociadas.\n"
                            + "Se recomienda cambiar su estado operativo a FUERA_DE_SERVICIO o MANTENIMIENTO.");
                }
            }

        } catch (NumberFormatException e) {
            mostrarAlerta(event, Alert.AlertType.WARNING, "Dato inválido", "El ID debe ser numérico.");
        } catch (SQLException e) {
            mostrarAlerta(event, Alert.AlertType.ERROR, "Error de base de datos", e.getMessage());
        }
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
            mostrarAlerta(event, Alert.AlertType.ERROR,
                    "No se pudo abrir la pantalla de " + titulo.toLowerCase(), e.getMessage());
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
}
