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
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.SQLException;
import dao.AlojamientoDAO;
import dao.RolDAO;
import model.Personal;
import model.Rol;
import service.RegistroPersonalService;

public class RegistroPersonalController {

    private static final String HORARIO_PREDETERMINADO = "7:00 a 19:00";

    @FXML
    private TextField fldId;

    @FXML
    private TextField fldUser;

    @FXML
    private TextField fldContrasena;

    @FXML
    private TextField fldNombre;

    @FXML
    private TextField fldFuncion;

    @FXML
    private TextField fldCargo;

    @FXML
    private Button btnCrearCuenta;

    @FXML
    private Button btnVolver;

    @FXML
    private Text txtRegistro;

    @FXML
    private void initialize() {
        if (txtRegistro != null) {
            txtRegistro.setText("Registro de Personal");
        }
        if (btnCrearCuenta != null) {
            btnCrearCuenta.setOnAction(this::registrar);
        }
        if (btnVolver != null) {
            btnVolver.setOnAction(this::volverASeleccionRol);
        }
        if (fldNombre != null) {
            fldNombre.requestFocus();
        }
    }

    @FXML
    private void registrar(ActionEvent event) {
        if (esVacio(fldUser) || esVacio(fldContrasena) || esVacio(fldNombre)
                || esVacio(fldFuncion) || esVacio(fldCargo)) {
            mostrarAlerta(event, Alert.AlertType.WARNING, "Campos requeridos",
                    "Complete nombre, función, cargo, usuario y contraseña.");
            return;
        }

        if (fldUser.getText().trim().length() > 50) {
            mostrarAlerta(event, Alert.AlertType.WARNING, "Usuario no válido",
                    "El nombre de usuario no puede superar los 50 caracteres.");
            return;
        }

        Long id = null;
        if (!esVacio(fldId)) {
            try {
                id = Long.valueOf(fldId.getText().trim());
            } catch (NumberFormatException e) {
                mostrarAlerta(event, Alert.AlertType.WARNING, "ID inválido",
                        "El ID debe ser un número entero. Déjelo vacío si se genera automáticamente.");
                return;
            }
        }

        Personal personal = new Personal();
        personal.setIdPersonal(id);
        personal.setNombre(fldNombre.getText().trim());
        personal.setFuncion(fldFuncion.getText().trim());
        personal.setCargo(fldCargo.getText().trim());
        personal.setHorarioTrabajo(HORARIO_PREDETERMINADO);

        if (personal.getNombre().length() > 120 || personal.getFuncion().length() > 100
                || personal.getCargo().length() > 60) {
            mostrarAlerta(event, Alert.AlertType.WARNING, "Datos no válidos",
                    "Revise la información: el nombre admite hasta 120 caracteres, "
                    + "la función 100 y el cargo 60.");
            return;
        }

        try {
            RolDAO rolDAO = new RolDAO();

            // Asignar rol Recepcionista por defecto para personal del sistema
            Rol rolRecep = rolDAO.buscarPorNombre("RECEPCIONISTA");
            if (rolRecep == null || rolRecep.getIdRol() == null) {
                mostrarAlerta(event, Alert.AlertType.ERROR, "Catálogo incompleto",
                        "No existe el rol RECEPCIONISTA. Ejecute Database/DML/01_seed_data.sql "
                        + "(el script termina con COMMIT) y vuelva a intentar.");
                return;
            }
            if (new AlojamientoDAO().buscarPorId(1L) == null) {
                mostrarAlerta(event, Alert.AlertType.ERROR, "Catálogo incompleto",
                        "No existe el alojamiento con ID 1, requerido por el personal. "
                        + "Ejecute Database/DML/01_seed_data.sql y vuelva a intentar.");
                return;
            }
            Long idRol = rolRecep.getIdRol();

            Long idPersonalGenerado = new RegistroPersonalService().registrar(
                    personal,
                    idRol,
                    fldUser.getText().trim(),
                    fldContrasena.getText().trim()
            );

            mostrarAlerta(event, Alert.AlertType.INFORMATION, "Registro exitoso",
                    "El colaborador " + personal.getNombre() + " fue registrado exitosamente con usuario: "
                    + fldUser.getText().trim() + ".\nYa puede iniciar sesión.");
            volverAlInicioSesion(event);
        } catch (SQLException e) {
            mostrarAlerta(event, Alert.AlertType.ERROR, "Error de registro",
                    "No se pudo guardar el personal en Oracle XE: " + e.getMessage());
        }
    }

    private void volverASeleccionRol(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/SeleccionRol.fxml"));
            Parent root = loader.load();

            Stage stage = obtenerVentana(event);
            stage.setScene(new Scene(root));
            stage.setTitle("QuindíoStay | Selección de Rol");
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            mostrarAlerta(event, Alert.AlertType.ERROR, "No se pudo volver a la selección de rol", e.getMessage());
        }
    }

    private void volverAlInicioSesion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/sesion.fxml"));
            Parent root = loader.load();

            Stage stage = obtenerVentana(event);
            stage.setScene(new Scene(root));
            stage.setTitle("QuindíoStay | Inicio de Sesión");
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            mostrarAlerta(event, Alert.AlertType.ERROR, "No se pudo volver al inicio de sesión", e.getMessage());
        }
    }

    private boolean esVacio(TextField campo) {
        return campo == null || campo.getText() == null || campo.getText().isBlank();
    }

    private Stage obtenerVentana(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    private void mostrarAlerta(ActionEvent event, Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.initOwner(obtenerVentana(event));
        alerta.setTitle("QuindíoStay");
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
