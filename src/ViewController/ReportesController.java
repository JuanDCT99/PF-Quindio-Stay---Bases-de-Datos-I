package ViewController;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import model.Reporte;
import service.ReportePDFService;
import service.ReporteService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.net.URL;

public class ReportesController {

    private final ReporteService reporteService = new ReporteService();
    private int reporteSeleccionado = 1; // Por defecto, reporte 1

    @FXML
    private TableView<Reporte> tablaReportes;
    @FXML
    private TableColumn<Reporte, Number> colIdReporte;
    @FXML
    private TableColumn<Reporte, String> colNombreReporte;
    @FXML
    private TableColumn<Reporte, String> colNivel;
    @FXML
    private TableColumn<Reporte, String> colPreguntaNegocio;
    @FXML
    private ComboBox<Reporte> cbReporte;

    @FXML
    private void initialize() {
        colIdReporte.setCellValueFactory(new PropertyValueFactory<>("idReporte"));
        colNombreReporte.setCellValueFactory(new PropertyValueFactory<>("nombreReporte"));
        colNivel.setCellValueFactory(new PropertyValueFactory<>("nivel"));
        colPreguntaNegocio.setCellValueFactory(new PropertyValueFactory<>("preguntaNegocio"));
        recargarReportes(null);
    }

    private void recargarReportes(Long idParaSeleccionar) {
        try {
            Reporte anterior = cbReporte.getValue();
            List<Reporte> reportes = reporteService.listarReportes();
            tablaReportes.setItems(FXCollections.observableArrayList(reportes));
            cbReporte.setItems(FXCollections.observableArrayList(reportes));
            cbReporte.setConverter(new StringConverter<Reporte>() {
                @Override
                public String toString(Reporte reporte) {
                    return reporte == null ? ""
                            : reporte.getIdReporte() + " - " + reporte.getNombreReporte();
                }

                @Override
                public Reporte fromString(String texto) {
                    return null;
                }
            });

            Long idBuscado = idParaSeleccionar != null ? idParaSeleccionar
                    : (anterior != null ? anterior.getIdReporte() : null);
            if (idBuscado != null) {
                for (Reporte reporte : reportes) {
                    if (idBuscado.equals(reporte.getIdReporte())) {
                        cbReporte.getSelectionModel().select(reporte);
                        break;
                    }
                }
            }
            if (cbReporte.getValue() == null && !reportes.isEmpty()) {
                cbReporte.getSelectionModel().selectFirst();
            }
            Reporte seleccionado = cbReporte.getValue();
            if (seleccionado != null && seleccionado.getIdReporte() != null) {
                reporteSeleccionado = seleccionado.getIdReporte().intValue();
            }
        } catch (SQLException e) {
            mostrarAlerta(null, Alert.AlertType.ERROR, "No se pudieron cargar los reportes", e.getMessage());
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

    /**
     * Genera el reporte indicado y lo guarda como PDF.
     * Se conecta con el boton "Generar" del FXML.
     */
    @FXML
    private void generarPDF(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte en PDF");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF", "*.pdf")
        );

        File file = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());
        if (file == null) {
            return;
        }

        try {
            // Obtener los datos del reporte seleccionado
            List<Map<String, Object>> datos = reporteService.ejecutar((long) reporteSeleccionado);

            if (datos == null || datos.isEmpty()) {
                mostrarAlerta(event, Alert.AlertType.WARNING, "Sin datos",
                        "No hay datos para generar el reporte. Verifica la base de datos.");
                return;
            }

            // Nombre del reporte tomado del catálogo (soporta reportes creados por el usuario)
            Reporte seleccionado = cbReporte.getValue();
            String nombreBase = seleccionado != null && seleccionado.getNombreReporte() != null
                    ? seleccionado.getNombreReporte().replaceAll("[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]", "").trim().replaceAll("\\s+", "_")
                    : "Personalizado";
            String nombreReporte = "Reporte_" + reporteSeleccionado + "_" + nombreBase;

            ReportePDFService pdfService = new ReportePDFService(nombreReporte);
            pdfService.generar(datos, file.getAbsolutePath());

            mostrarAlerta(event, Alert.AlertType.INFORMATION, "PDF generado",
                    "El reporte se ha guardado en: " + file.getAbsolutePath());

        } catch (SQLException e) {
            mostrarAlerta(event, Alert.AlertType.ERROR, "Error de base de datos",
                    "No se pudo conectar a la base de datos.\nDetalle: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            mostrarAlerta(event, Alert.AlertType.WARNING, "Reporte sin consulta",
                    "El reporte seleccionado no tiene una consulta SQL asociada. "
                    + "Solo los 10 reportes originales pueden generarse en PDF.");
        } catch (Exception e) {
            mostrarAlerta(event, Alert.AlertType.ERROR, "Error al generar PDF",
                    "No se pudo generar el reporte.\nDetalle: " + e.getMessage());
        }
    }

    /**
     * Cambia el reporte seleccionado para generar.
     */
    @FXML
    private void seleccionarReporte(ActionEvent event) {
        Reporte seleccionado = cbReporte.getValue();
        if (seleccionado != null && seleccionado.getIdReporte() != null) {
            reporteSeleccionado = seleccionado.getIdReporte().intValue();
        }
    }

    @FXML
    private void crearReporte(ActionEvent event) {
        TextInputDialog dlgNombre = new TextInputDialog("");
        dlgNombre.setTitle("Nuevo Reporte | QuindíoStay");
        dlgNombre.setHeaderText("Creación de Reporte");
        dlgNombre.setContentText("Nombre del reporte:");
        Optional<String> nombre = dlgNombre.showAndWait();
        if (nombre.isEmpty()) return;

        ChoiceDialog<String> dlgNivel = new ChoiceDialog<>("SIMPLE",
                List.of("SIMPLE", "INTERMEDIO", "AVANZADO"));
        dlgNivel.setTitle("Nivel del Reporte");
        dlgNivel.setHeaderText("Clasificación del reporte");
        dlgNivel.setContentText("Seleccione el nivel:");
        Optional<String> nivel = dlgNivel.showAndWait();
        if (nivel.isEmpty()) return;

        TextInputDialog dlgPregunta = new TextInputDialog("");
        dlgPregunta.setTitle("Nuevo Reporte | QuindíoStay");
        dlgPregunta.setHeaderText("Pregunta de negocio");
        dlgPregunta.setContentText("¿Qué pregunta responde este reporte?");
        Optional<String> pregunta = dlgPregunta.showAndWait();
        if (pregunta.isEmpty()) return;

        Reporte reporte = new Reporte();
        reporte.setNombreReporte(nombre.get().trim());
        reporte.setNivel(nivel.get());
        reporte.setPreguntaNegocio(pregunta.get().trim());
        if (!reporte.esValido()) {
            mostrarAlerta(event, Alert.AlertType.WARNING, "Datos inválidos",
                    "Verifique el nombre (máx. 120 caracteres) y la pregunta (máx. 500 caracteres).");
            return;
        }

        try {
            Long idGenerado = reporteService.crearReporte(reporte);
            recargarReportes(idGenerado);
            mostrarAlerta(event, Alert.AlertType.INFORMATION, "Reporte Creado",
                    "El reporte #" + idGenerado + " fue agregado al catálogo.");
        } catch (SQLException e) {
            mostrarAlerta(event, Alert.AlertType.ERROR, "Error de base de datos", e.getMessage());
        }
    }

    @FXML
    private void editarReporte(ActionEvent event) {
        TextInputDialog dlgId = new TextInputDialog("1");
        dlgId.setTitle("Editar Reporte | QuindíoStay");
        dlgId.setHeaderText("Modificación de Reporte");
        dlgId.setContentText("ID del reporte:");
        Optional<String> id = dlgId.showAndWait();
        if (id.isEmpty()) return;

        try {
            Long idReporte = Long.valueOf(id.get().trim());
            Reporte reporte = reporteService.buscarPorId(idReporte);
            if (reporte == null) {
                mostrarAlerta(event, Alert.AlertType.WARNING, "No encontrado",
                        "No existe un reporte con ID " + idReporte + ".");
                return;
            }

            List<String> niveles = new ArrayList<>(List.of("SIMPLE", "INTERMEDIO", "AVANZADO"));
            String nivelActual = reporte.getNivel();
            if (nivelActual != null && !niveles.contains(nivelActual)) {
                niveles.add(0, nivelActual);
            }
            ChoiceDialog<String> dlgNivel = new ChoiceDialog<>(
                    nivelActual != null ? nivelActual : niveles.get(0), niveles);
            dlgNivel.setTitle("Nivel del Reporte");
            dlgNivel.setHeaderText("Reporte #" + idReporte);
            dlgNivel.setContentText("Seleccione el nivel:");
            Optional<String> nivel = dlgNivel.showAndWait();
            if (nivel.isEmpty()) return;

            TextInputDialog dlgNombre = new TextInputDialog(
                    reporte.getNombreReporte() == null ? "" : reporte.getNombreReporte());
            dlgNombre.setTitle("Editar Reporte | QuindíoStay");
            dlgNombre.setHeaderText("Nombre del reporte");
            dlgNombre.setContentText("Nombre del reporte:");
            Optional<String> nombre = dlgNombre.showAndWait();
            if (nombre.isEmpty()) return;

            TextInputDialog dlgPregunta = new TextInputDialog(
                    reporte.getPreguntaNegocio() == null ? "" : reporte.getPreguntaNegocio());
            dlgPregunta.setTitle("Editar Reporte | QuindíoStay");
            dlgPregunta.setHeaderText("Pregunta de negocio");
            dlgPregunta.setContentText("¿Qué pregunta responde este reporte?");
            Optional<String> pregunta = dlgPregunta.showAndWait();
            if (pregunta.isEmpty()) return;

            reporte.setNombreReporte(nombre.get().trim());
            reporte.setNivel(nivel.get());
            reporte.setPreguntaNegocio(pregunta.get().trim());
            if (!reporte.esValido()) {
                mostrarAlerta(event, Alert.AlertType.WARNING, "Datos inválidos",
                        "Verifique el nombre (máx. 120 caracteres) y la pregunta (máx. 500 caracteres).");
                return;
            }

            if (reporteService.actualizarReporte(reporte)) {
                recargarReportes(idReporte);
                mostrarAlerta(event, Alert.AlertType.INFORMATION, "Reporte Actualizado",
                        "El reporte #" + idReporte + " fue actualizado.");
            } else {
                mostrarAlerta(event, Alert.AlertType.WARNING, "Aviso", "No se pudo actualizar el reporte.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(event, Alert.AlertType.WARNING, "Dato inválido", "El ID debe ser numérico.");
        } catch (SQLException e) {
            mostrarAlerta(event, Alert.AlertType.ERROR, "Error de base de datos", e.getMessage());
        }
    }

    @FXML
    private void eliminarReporte(ActionEvent event) {
        Reporte seleccionado = tablaReportes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            seleccionado = cbReporte.getValue();
        }
        if (seleccionado == null || seleccionado.getIdReporte() == null) {
            mostrarAlerta(event, Alert.AlertType.WARNING, "Sin selección",
                    "Seleccione el reporte que desea eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Eliminar el reporte #" + seleccionado.getIdReporte() + " - "
                        + seleccionado.getNombreReporte() + "?",
                ButtonType.YES, ButtonType.NO);
        if (event.getSource() instanceof Node) {
            confirmacion.initOwner(((Node) event.getSource()).getScene().getWindow());
        }
        confirmacion.setTitle("QuindioStay");
        confirmacion.setHeaderText("Confirmar eliminación");
        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isEmpty() || respuesta.get() != ButtonType.YES) {
            return;
        }

        try {
            boolean eliminado = reporteService.eliminarReporte(seleccionado.getIdReporte());
            if (eliminado) {
                recargarReportes(null);
                mostrarAlerta(event, Alert.AlertType.INFORMATION, "Reporte eliminado",
                        "El reporte #" + seleccionado.getIdReporte() + " fue eliminado del catálogo.");
            } else {
                mostrarAlerta(event, Alert.AlertType.WARNING, "Aviso",
                        "No se pudo eliminar el reporte. Puede que ya no exista.");
            }
        } catch (SQLException e) {
            mostrarAlerta(event, Alert.AlertType.ERROR, "Error de base de datos", e.getMessage());
        }
    }

    private void navegar(ActionEvent event, String ruta, String titulo) {
        try {
            URL recurso = getClass().getResource(ruta);
            if (recurso == null) {
                throw new IOException("No se encontro el recurso " + ruta);
            }

            FXMLLoader loader = new FXMLLoader(recurso);
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("QuindioStay | " + titulo);
            stage.setMinWidth(1100);
            stage.setMinHeight(580);
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.initOwner(((Node) event.getSource()).getScene().getWindow());
            alerta.setTitle("QuindioStay");
            alerta.setHeaderText("No se pudo abrir la pantalla de " + titulo.toLowerCase());
            alerta.setContentText(e.getMessage());
            alerta.showAndWait();
        }
    }

    private void mostrarAlerta(ActionEvent event, Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        if (event != null && event.getSource() instanceof Node) {
            alerta.initOwner(((Node) event.getSource()).getScene().getWindow());
        }
        alerta.setTitle("QuindioStay");
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
