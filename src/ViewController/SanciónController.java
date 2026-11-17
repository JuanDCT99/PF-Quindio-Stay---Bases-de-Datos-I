package ViewController;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dao.SancionDAO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Sancion;

public class SanciónController {

    private final SancionDAO sancionDAO = new SancionDAO();

    @FXML
    private TableView<Sancion> tablaSanciones;
    @FXML
    private TableColumn<Sancion, Number> colIdSancion;
    @FXML
    private TableColumn<Sancion, String> colTipoSancion;
    @FXML
    private TableColumn<Sancion, String> colDescripcionSancion;
    @FXML
    private TableColumn<Sancion, LocalDateTime> colFechaSancion;
    @FXML
    private TableColumn<Sancion, Number> colValorSancion;
    @FXML
    private TableColumn<Sancion, Number> colIdReservaSancion;
    @FXML
    private TableColumn<Sancion, Number> colIdServicioSancion;

    @FXML
    private void initialize() {
        colIdSancion.setCellValueFactory(new PropertyValueFactory<>("idSancion"));
        colTipoSancion.setCellValueFactory(new PropertyValueFactory<>("tipoSancion"));
        colDescripcionSancion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colFechaSancion.setCellValueFactory(new PropertyValueFactory<>("fechaSancion"));
        colValorSancion.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colIdReservaSancion.setCellValueFactory(new PropertyValueFactory<>("idReserva"));
        colIdServicioSancion.setCellValueFactory(new PropertyValueFactory<>("idServicio"));
        recargarTabla();
    }

    private void recargarTabla() {
        try {
            tablaSanciones.setItems(FXCollections.observableArrayList(sancionDAO.listarTodos()));
        } catch (SQLException e) {
            mostrarAlerta(null, Alert.AlertType.ERROR, "No se pudieron cargar las sanciones", e.getMessage());
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
    private void crearSancion(ActionEvent event) {
        TextInputDialog dlgReserva = new TextInputDialog("1001");
        dlgReserva.setTitle("Nueva Sanción | QuindíoStay");
        dlgReserva.setHeaderText("Registro de Sanción");
        dlgReserva.setContentText("ID de la Reserva:");
        Optional<String> idReserva = dlgReserva.showAndWait();
        if (idReserva.isEmpty()) return;

        List<String> tipos = new ArrayList<>(List.of("DANOS", "MORA", "CANCELACION", "NO_SHOW"));
        ChoiceDialog<String> dlgTipo = new ChoiceDialog<>(tipos.get(0), tipos);
        dlgTipo.setTitle("Tipo de Sanción");
        dlgTipo.setHeaderText("Clasificación de la sanción");
        dlgTipo.setContentText("Seleccione el tipo:");
        Optional<String> tipo = dlgTipo.showAndWait();
        if (tipo.isEmpty()) return;

        TextInputDialog dlgDescripcion = new TextInputDialog("Cargo por incumplimiento de normas");
        dlgDescripcion.setTitle("Nueva Sanción | QuindíoStay");
        dlgDescripcion.setHeaderText("Descripción");
        dlgDescripcion.setContentText("Describa la sanción:");
        Optional<String> descripcion = dlgDescripcion.showAndWait();
        if (descripcion.isEmpty()) return;

        TextInputDialog dlgValor = new TextInputDialog("50000");
        dlgValor.setTitle("Nueva Sanción | QuindíoStay");
        dlgValor.setHeaderText("Valor de la sanción");
        dlgValor.setContentText("Valor a cobrar ($):");
        Optional<String> valor = dlgValor.showAndWait();
        if (valor.isEmpty()) return;

        TextInputDialog dlgServicio = new TextInputDialog("");
        dlgServicio.setTitle("Nueva Sanción | QuindíoStay");
        dlgServicio.setHeaderText("Servicio asociado (opcional)");
        dlgServicio.setContentText("ID del Servicio (deje vacío si no aplica):");
        Optional<String> idServicio = dlgServicio.showAndWait();
        if (idServicio.isEmpty()) return;

        try {
            BigDecimal monto = new BigDecimal(valor.get().trim());
            if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                mostrarAlerta(event, Alert.AlertType.WARNING, "Valor inválido", "El valor debe ser mayor a cero.");
                return;
            }
            String textoServicio = idServicio.get().trim();
            Long servicio = textoServicio.isEmpty() ? null : Long.valueOf(textoServicio);

            Sancion sancion = new Sancion();
            sancion.setTipoSancion(tipo.get());
            sancion.setDescripcion(descripcion.get().trim());
            sancion.setFechaSancion(LocalDateTime.now());
            sancion.setValor(monto);
            sancion.setIdReserva(Long.valueOf(idReserva.get().trim()));
            sancion.setIdServicio(servicio);

            Long idGenerado = sancionDAO.registrarSancion(sancion);
            recargarTabla();
            mostrarAlerta(event, Alert.AlertType.INFORMATION, "Sanción Creada",
                    "Sanción #" + idGenerado + " registrada para la reserva #" + sancion.getIdReserva() + ".");
        } catch (NumberFormatException e) {
            mostrarAlerta(event, Alert.AlertType.WARNING, "Dato inválido",
                    "Ingrese números válidos para el ID de reserva, el valor y el ID de servicio.");
        } catch (SQLException e) {
            mostrarAlerta(event, Alert.AlertType.ERROR, "Error de base de datos", e.getMessage());
        }
    }

    @FXML
    private void editarSancion(ActionEvent event) {
        TextInputDialog dlgId = new TextInputDialog("1");
        dlgId.setTitle("Editar Sanción | QuindíoStay");
        dlgId.setHeaderText("Modificación de Sanción");
        dlgId.setContentText("ID de la sanción:");
        Optional<String> id = dlgId.showAndWait();
        if (id.isEmpty()) return;

        try {
            Sancion sancion = sancionDAO.buscarPorId(Long.valueOf(id.get().trim()));
            if (sancion == null) {
                mostrarAlerta(event, Alert.AlertType.WARNING, "No encontrada",
                        "No existe una sanción con ID " + id.get().trim() + ".");
                return;
            }

            List<String> tipos = new ArrayList<>(List.of("DANOS", "MORA", "CANCELACION", "NO_SHOW"));
            String tipoActual = sancion.getTipoSancion();
            if (tipoActual != null && !tipos.contains(tipoActual)) {
                tipos.add(0, tipoActual);
            }
            ChoiceDialog<String> dlgTipo = new ChoiceDialog<>(
                    tipoActual != null ? tipoActual : tipos.get(0), tipos);
            dlgTipo.setTitle("Tipo de Sanción");
            dlgTipo.setHeaderText("Sanción #" + sancion.getIdSancion());
            dlgTipo.setContentText("Seleccione el tipo:");
            Optional<String> tipo = dlgTipo.showAndWait();
            if (tipo.isEmpty()) return;

            TextInputDialog dlgDescripcion = new TextInputDialog(sancion.getDescripcion());
            dlgDescripcion.setTitle("Editar Sanción | QuindíoStay");
            dlgDescripcion.setHeaderText("Descripción");
            dlgDescripcion.setContentText("Describa la sanción:");
            Optional<String> descripcion = dlgDescripcion.showAndWait();
            if (descripcion.isEmpty()) return;

            TextInputDialog dlgValor = new TextInputDialog(
                    sancion.getValor() == null ? "" : sancion.getValor().toPlainString());
            dlgValor.setTitle("Editar Sanción | QuindíoStay");
            dlgValor.setHeaderText("Valor de la sanción");
            dlgValor.setContentText("Valor a cobrar ($):");
            Optional<String> valor = dlgValor.showAndWait();
            if (valor.isEmpty()) return;

            TextInputDialog dlgServicio = new TextInputDialog(
                    sancion.getIdServicio() == null ? "" : sancion.getIdServicio().toString());
            dlgServicio.setTitle("Editar Sanción | QuindíoStay");
            dlgServicio.setHeaderText("Servicio asociado (opcional)");
            dlgServicio.setContentText("ID del Servicio (deje vacío si no aplica):");
            Optional<String> idServicio = dlgServicio.showAndWait();
            if (idServicio.isEmpty()) return;

            BigDecimal monto = new BigDecimal(valor.get().trim());
            if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                mostrarAlerta(event, Alert.AlertType.WARNING, "Valor inválido", "El valor debe ser mayor a cero.");
                return;
            }
            String textoServicio = idServicio.get().trim();
            sancion.setTipoSancion(tipo.get());
            sancion.setDescripcion(descripcion.get().trim());
            sancion.setValor(monto);
            sancion.setIdServicio(textoServicio.isEmpty() ? null : Long.valueOf(textoServicio));

            if (sancionDAO.actualizarSancion(sancion)) {
                recargarTabla();
                mostrarAlerta(event, Alert.AlertType.INFORMATION, "Sanción Actualizada",
                        "La sanción #" + sancion.getIdSancion() + " fue actualizada.");
            } else {
                mostrarAlerta(event, Alert.AlertType.WARNING, "Aviso", "No se pudo actualizar la sanción.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(event, Alert.AlertType.WARNING, "Dato inválido",
                    "Ingrese números válidos para el ID, el valor y el ID de servicio.");
        } catch (SQLException e) {
            mostrarAlerta(event, Alert.AlertType.ERROR, "Error de base de datos", e.getMessage());
        }
    }

    @FXML
    private void eliminarSancion(ActionEvent event) {
        TextInputDialog dlgId = new TextInputDialog("1");
        dlgId.setTitle("Eliminar Sanción | QuindíoStay");
        dlgId.setHeaderText("Eliminación de Sanción");
        dlgId.setContentText("ID de la sanción:");
        Optional<String> id = dlgId.showAndWait();
        if (id.isEmpty()) return;

        try {
            Long idSancion = Long.valueOf(id.get().trim());
            Sancion sancion = sancionDAO.buscarPorId(idSancion);
            if (sancion == null) {
                mostrarAlerta(event, Alert.AlertType.WARNING, "No encontrada",
                        "No existe una sanción con ID " + idSancion + ".");
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Eliminar la sanción #" + idSancion + "?");
            confirmacion.setContentText("Tipo: " + sancion.getTipoSancion() + " | Valor: $" + sancion.getValor());
            Optional<ButtonType> resp = confirmacion.showAndWait();
            if (resp.isPresent() && resp.get() == ButtonType.OK) {
                sancionDAO.eliminarSancion(idSancion);
                recargarTabla();
                mostrarAlerta(event, Alert.AlertType.INFORMATION, "Sanción Eliminada",
                        "La sanción #" + idSancion + " fue eliminada.");
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
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.initOwner(((Node) event.getSource()).getScene().getWindow());
            alerta.setTitle("QuindíoStay");
            alerta.setHeaderText("No se pudo abrir la pantalla de " + titulo.toLowerCase());
            alerta.setContentText(e.getMessage());
            alerta.showAndWait();
        }
    }
}
