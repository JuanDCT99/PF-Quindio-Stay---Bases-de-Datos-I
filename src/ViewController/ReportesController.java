package ViewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import service.ReportePDFService;
import service.ReporteService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.net.URL;

public class ReportesController {

    private final ReporteService reporteService = new ReporteService();
    private int reporteSeleccionado = 1; // Por defecto, reporte 1

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

            // Nombre del reporte segun su ID
            String[] nombresReportes = {"Catalogo de habitaciones", "Habitaciones nunca reservadas",
                    "Tarifario vigente", "Ingresos por temporada", "Reservas y comisiones",
                    "Estado de folios", "Uso de servicios", "Cancelaciones", "Análisis de cancelaciones",
                    "Segmentacion RFM"};
            String nombreReporte = "Reporte_" + reporteSeleccionado + "_" + nombresReportes[reporteSeleccionado - 1];

            ReportePDFService pdfService = new ReportePDFService(nombreReporte);
            pdfService.generar(datos, file.getAbsolutePath());

            mostrarAlerta(event, Alert.AlertType.INFORMATION, "PDF generado",
                    "El reporte se ha guardado en: " + file.getAbsolutePath());

        } catch (SQLException e) {
            mostrarAlerta(event, Alert.AlertType.ERROR, "Error de base de datos",
                    "No se pudo conectar a la base de datos.\nDetalle: " + e.getMessage());
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
        // Este metodo se llamara desde el FXML cuando se seleccione el reporte
        // Por ahora, solo indica que se quiere generar el reporte seleccionado
        // La logica de seleccion se puede mejorar con un ComboBox o lista
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
