package ViewController;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import dao.FolioDAO;
import dao.PagoDAO;
import dao.ReservaDAO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Pago;
import model.Reserva;

public class PagoController {

    private final PagoDAO pagoDAO = new PagoDAO();
    private final FolioDAO folioDAO = new FolioDAO();
    private final ReservaDAO reservaDAO = new ReservaDAO();

    @FXML
    private TableView<Pago> tablaPagos;
    @FXML
    private TableColumn<Pago, Number> colIdPago;
    @FXML
    private TableColumn<Pago, Number> colMonto;
    @FXML
    private TableColumn<Pago, String> colMedioPago;
    @FXML
    private TableColumn<Pago, LocalDateTime> colFechaPago;
    @FXML
    private TableColumn<Pago, Number> colIdReservaPago;

    @FXML
    private void initialize() {
        colIdPago.setCellValueFactory(new PropertyValueFactory<>("idPago"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        colMedioPago.setCellValueFactory(new PropertyValueFactory<>("medioPago"));
        colFechaPago.setCellValueFactory(new PropertyValueFactory<>("fechaPago"));
        colIdReservaPago.setCellValueFactory(new PropertyValueFactory<>("idReserva"));
        recargarTabla();
    }

    private void recargarTabla() {
        try {
            tablaPagos.setItems(FXCollections.observableArrayList(pagoDAO.listarTodos()));
        } catch (SQLException e) {
            mostrarAlerta(null, Alert.AlertType.ERROR, "No se pudieron cargar los pagos", e.getMessage());
        }
    }

    @FXML
    private void crearPago(ActionEvent event) {
        TextInputDialog dialogRes = new TextInputDialog("1001");
        dialogRes.setTitle("Registrar Pago | QuindíoStay");
        dialogRes.setHeaderText("Abono a Cuenta de Reserva");
        dialogRes.setContentText("ID de la Reserva (Ej: 1001, 1002):");
        Optional<String> resId = dialogRes.showAndWait();
        if (resId.isEmpty()) return;

        TextInputDialog dialogMonto = new TextInputDialog("150000");
        dialogMonto.setTitle("Registrar Pago | QuindíoStay");
        dialogMonto.setHeaderText("Monto del Abono");
        dialogMonto.setContentText("Valor a pagar ($):");
        Optional<String> resMonto = dialogMonto.showAndWait();
        if (resMonto.isEmpty()) return;

        ChoiceDialog<String> medioDialog = new ChoiceDialog<>("EFECTIVO", "EFECTIVO", "TARJETA_CREDITO", "TARJETA_DEBITO", "TRANSFERENCIA");
        medioDialog.setTitle("Medio de Pago");
        medioDialog.setHeaderText("Forma de pago");
        medioDialog.setContentText("Seleccione medio:");
        Optional<String> resMedio = medioDialog.showAndWait();
        if (resMedio.isEmpty()) return;

        try {
            Long idReserva = Long.parseLong(resId.get().trim());
            Reserva reserva = reservaDAO.buscarPorId(idReserva);
            if (reserva == null) {
                mostrarAlerta(event, Alert.AlertType.WARNING, "Reserva no encontrada",
                        "No existe la reserva #" + idReserva + ". Verifique el ID antes de registrar el pago.");
                return;
            }
            BigDecimal monto = new BigDecimal(resMonto.get().trim());
            if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                mostrarAlerta(event, Alert.AlertType.WARNING, "Monto inválido", "El monto debe ser mayor a cero.");
                return;
            }

            Pago pago = new Pago();
            pago.setMonto(monto);
            pago.setMedioPago(resMedio.get());
            pago.setFechaPago(LocalDateTime.now());
            pago.setIdReserva(idReserva);

            Long idPagoGen = pagoDAO.registrarPago(pago);
            recargarTabla();
            BigDecimal nuevoSaldo = folioDAO.calcularSaldoPendiente(idReserva);

            mostrarAlerta(event, Alert.AlertType.INFORMATION, "Pago Registrado",
                    "Pago #" + idPagoGen + " registrado con éxito en Oracle XE.\n"
                    + "Reserva #" + idReserva + "\n"
                    + "Monto: $" + monto + " (" + resMedio.get() + ")\n"
                    + "Saldo restante de la cuenta: $" + nuevoSaldo);

        } catch (NumberFormatException e) {
            mostrarAlerta(event, Alert.AlertType.WARNING, "Dato inválido", "Ingrese números válidos para ID y monto.");
        } catch (SQLException e) {
            mostrarAlerta(event, Alert.AlertType.ERROR, "Error de base de datos", e.getMessage());
        }
    }

    @FXML
    private void consultarSaldo(ActionEvent event) {
        TextInputDialog dialogRes = new TextInputDialog("1001");
        dialogRes.setTitle("Consultar Balance | QuindíoStay");
        dialogRes.setHeaderText("Estado de Cuenta del Folio");
        dialogRes.setContentText("ID de la Reserva:");
        Optional<String> resId = dialogRes.showAndWait();
        if (resId.isEmpty()) return;

        try {
            Long idReserva = Long.parseLong(resId.get().trim());
            BigDecimal totalPagos = pagoDAO.totalPagadoPorReserva(idReserva);
            BigDecimal saldoPendiente = folioDAO.calcularSaldoPendiente(idReserva);

            mostrarAlerta(event, Alert.AlertType.INFORMATION, "Balance del Folio #" + idReserva,
                    "Estado financiero de la Reserva #" + idReserva + ":\n\n"
                    + "• Total pagado a la fecha: $" + totalPagos + "\n"
                    + "• Saldo pendiente por liquidar: $" + saldoPendiente);

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
