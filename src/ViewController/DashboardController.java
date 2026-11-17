package ViewController;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import dao.ReservaDAO;
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
import model.Reserva;
import utils.SesionUsuario;

public class DashboardController {

    private final ReservaDAO reservaDAO = new ReservaDAO();

    @FXML
    private void initialize() {
        // Inicialización de la pantalla de reservas
    }

    @FXML
    private void crearReserva(ActionEvent event) {
        TextInputDialog dialogTitular = new TextInputDialog("1");
        dialogTitular.setTitle("Nueva Reserva | QuindíoStay");
        dialogTitular.setHeaderText("Creación de Reserva");
        dialogTitular.setContentText("ID del Huésped Titular (Ej: 1):");
        Optional<String> resTitular = dialogTitular.showAndWait();
        if (resTitular.isEmpty()) return;

        TextInputDialog dialogHab = new TextInputDialog("1");
        dialogHab.setTitle("Nueva Reserva | QuindíoStay");
        dialogHab.setHeaderText("Habitación a reservar");
        dialogHab.setContentText("ID de Habitación (Ej: 1):");
        Optional<String> resHab = dialogHab.showAndWait();
        if (resHab.isEmpty()) return;

        TextInputDialog dialogNoches = new TextInputDialog("2");
        dialogNoches.setTitle("Nueva Reserva | QuindíoStay");
        dialogNoches.setHeaderText("Duración de la estadía");
        dialogNoches.setContentText("Cantidad de noches (Ej: 2):");
        Optional<String> resNoches = dialogNoches.showAndWait();
        if (resNoches.isEmpty()) return;

        try {
            Long idTitular = Long.parseLong(resTitular.get().trim());
            Long idHabitacion = Long.parseLong(resHab.get().trim());
            int noches = Integer.parseInt(resNoches.get().trim());
            if (noches <= 0) noches = 1;

            LocalDate entrada = LocalDate.now().plusDays(1);
            LocalDate salida = entrada.plusDays(noches);
            BigDecimal precioNoche = new BigDecimal("250000.00");

            Reserva nuevaReserva = new Reserva();
            nuevaReserva.setEstado("CONFIRMADA");
            nuevaReserva.setFechaCreacion(LocalDateTime.now());
            nuevaReserva.setCanalDirecto("S");
            nuevaReserva.setIdPlan(1L);
            nuevaReserva.setIdTitular(idTitular);

            Long idGenerado = reservaDAO.crearReservaCompleta(nuevaReserva, idHabitacion, entrada, salida, precioNoche);

            mostrarAlerta(event, Alert.AlertType.INFORMATION, "Reserva Creada con Éxito",
                    "Reserva #" + idGenerado + " registrada exitosamente en Oracle XE.\n"
                    + "Titular ID: " + idTitular + "\n"
                    + "Habitación ID: " + idHabitacion + "\n"
                    + "Fechas: " + entrada + " al " + salida + " (" + noches + " noches)\n"
                    + "Folio de cobro inicial generado automáticamente.");

        } catch (NumberFormatException e) {
            mostrarAlerta(event, Alert.AlertType.WARNING, "Dato inválido", "Ingrese números válidos para los identificadores.");
        } catch (SQLException e) {
            mostrarAlerta(event, Alert.AlertType.ERROR, "Error al crear reserva",
                    "Fallo en base de datos: " + e.getMessage());
        }
    }

    @FXML
    private void editarReserva(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("1001");
        dialog.setTitle("Gestión de Reserva | QuindíoStay");
        dialog.setHeaderText("Operación de Estancia");
        dialog.setContentText("Ingrese el ID de la reserva (Ej: 1001, 1002):");
        Optional<String> resId = dialog.showAndWait();
        if (resId.isEmpty()) return;

        try {
            Long idReserva = Long.parseLong(resId.get().trim());
            Reserva reserva = reservaDAO.buscarPorId(idReserva);
            if (reserva == null) {
                mostrarAlerta(event, Alert.AlertType.WARNING, "No encontrada", "La reserva #" + idReserva + " no existe.");
                return;
            }

            ChoiceDialog<String> opDialog = new ChoiceDialog<>("Check-In", "Check-In", "Check-Out");
            opDialog.setTitle("Operación de Reserva #" + idReserva);
            opDialog.setHeaderText("Estado actual: " + reserva.getEstado() + " | Precio: $" + reserva.getPrecioTotal());
            opDialog.setContentText("Seleccione la acción:");
            Optional<String> operacion = opDialog.showAndWait();
            if (operacion.isEmpty()) return;

            if ("Check-In".equals(operacion.get())) {
                boolean ok = reservaDAO.registrarCheckIn(idReserva);
                if (ok) {
                    mostrarAlerta(event, Alert.AlertType.INFORMATION, "Check-In Exitoso",
                            "Check-in completado para la reserva #" + idReserva + ".\nHabitación marcada como OCUPADA.");
                } else {
                    mostrarAlerta(event, Alert.AlertType.WARNING, "Aviso", "No se pudo registrar check-in.");
                }
            } else {
                boolean ok = reservaDAO.registrarCheckOut(idReserva);
                if (ok) {
                    mostrarAlerta(event, Alert.AlertType.INFORMATION, "Check-Out Exitoso",
                            "Check-out completado para la reserva #" + idReserva + ".\nFolio cerrado y habitación pasada a EN_PREPARACION.");
                } else {
                    mostrarAlerta(event, Alert.AlertType.WARNING, "Aviso", "No se pudo registrar check-out.");
                }
            }

        } catch (NumberFormatException e) {
            mostrarAlerta(event, Alert.AlertType.WARNING, "Dato inválido", "El ID debe ser numérico.");
        } catch (SQLException e) {
            mostrarAlerta(event, Alert.AlertType.ERROR, "Error de base de datos", e.getMessage());
        }
    }

    @FXML
    private void cancelarReserva(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("1001");
        dialog.setTitle("Cancelar Reserva | QuindíoStay");
        dialog.setHeaderText("Cancelación de Reserva");
        dialog.setContentText("Ingrese el ID de la reserva a cancelar:");
        Optional<String> resId = dialog.showAndWait();
        if (resId.isEmpty()) return;

        try {
            Long idReserva = Long.parseLong(resId.get().trim());
            Reserva reserva = reservaDAO.buscarPorId(idReserva);
            if (reserva == null) {
                mostrarAlerta(event, Alert.AlertType.WARNING, "No encontrada", "La reserva #" + idReserva + " no existe.");
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar cancelación");
            confirmacion.setHeaderText("¿Está seguro de cancelar la reserva #" + idReserva + "?");
            confirmacion.setContentText("Estado actual: " + reserva.getEstado());
            Optional<ButtonType> resp = confirmacion.showAndWait();

            if (resp.isPresent() && resp.get() == ButtonType.OK) {
                reservaDAO.actualizarEstado(idReserva, "CANCELADA");
                mostrarAlerta(event, Alert.AlertType.INFORMATION, "Cancelada",
                        "La reserva #" + idReserva + " fue marcada como CANCELADA en Oracle XE.");
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
