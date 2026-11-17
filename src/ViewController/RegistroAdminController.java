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
import dao.RolDAO;
import dao.UsuarioDAO;
import model.Rol;
import model.UsuarioSistema;

public class RegistroAdminController {

    @FXML
    private TextField fldId;

    @FXML
    private TextField fldUser;

    @FXML
    private TextField fldContrasena;

    @FXML
    private TextField fldNombre;

    @FXML
    private TextField fldDocumento;

    @FXML
    private Button btnCrearCuenta;

    @FXML
    private Button btnVolver;

    @FXML
    private Text txtRegistro;

    @FXML
    private void initialize() {
        if (txtRegistro != null) {
            txtRegistro.setText("Registro de Administrador");
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
        if (esVacio(fldUser) || esVacio(fldContrasena) || esVacio(fldNombre) || esVacio(fldDocumento)) {
            mostrarAlerta(event, Alert.AlertType.WARNING, "Campos requeridos",
                    "Complete nombre, documento, usuario y contraseña.");
            return;
        }

        if (fldUser.getText().trim().length() > 50) {
            mostrarAlerta(event, Alert.AlertType.WARNING, "Usuario no válido",
                    "El nombre de usuario no puede superar los 50 caracteres.");
            return;
        }

        if (fldNombre.getText().trim().length() > 120 || fldDocumento.getText().trim().length() > 25) {
            mostrarAlerta(event, Alert.AlertType.WARNING, "Datos no válidos",
                    "El nombre admite hasta 120 caracteres y el documento 25.");
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

        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            if (usuarioDAO.buscarPorNombreUsuario(fldUser.getText().trim()) != null) {
                mostrarAlerta(event, Alert.AlertType.WARNING, "Usuario existente",
                        "El nombre de usuario '" + fldUser.getText().trim() + "' ya está registrado. Use otro.");
                return;
            }
            boolean idDuplicado = id != null && usuarioDAO.buscarPorId(id) != null;
            if (idDuplicado) {
                id = null;
            }

            RolDAO rolDAO = new RolDAO();
            Rol rolAdmin = rolDAO.buscarPorNombre("ADMINISTRADOR");
            if (rolAdmin == null || rolAdmin.getIdRol() == null) {
                mostrarAlerta(event, Alert.AlertType.ERROR, "Catálogo incompleto",
                        "No existe el rol ADMINISTRADOR. Ejecute Database/DML/01_seed_data.sql "
                        + "(el script termina con COMMIT) y vuelva a intentar.");
                return;
            }
            Long idRol = rolAdmin.getIdRol();

            UsuarioSistema nuevoUsuario = new UsuarioSistema(
                    id,
                    fldUser.getText().trim(),
                    null,
                    "ACTIVO",
                    idRol,
                    null
            );
            Long idGenerado = usuarioDAO.registrarUsuario(nuevoUsuario, fldContrasena.getText().trim());
            String avisoId = idDuplicado
                    ? "\nEl ID ingresado ya existía; se generó automáticamente el ID #" + idGenerado + "."
                    : "";

            mostrarAlerta(event, Alert.AlertType.INFORMATION, "Registro exitoso",
                    "El administrador " + fldNombre.getText().trim() + " fue registrado exitosamente con usuario: "
                    + fldUser.getText().trim() + ".\nID asignado: #" + idGenerado + "." + avisoId
                    + "\nYa puede iniciar sesión.");
            volverAlInicioSesion(event);
        } catch (SQLException e) {
            String detalle = e.getErrorCode() == 1
                    ? "El ID o el nombre de usuario ya están registrados. Deje el ID vacío para generarlo automáticamente."
                    : e.getMessage();
            mostrarAlerta(event, Alert.AlertType.ERROR, "Error de registro",
                    "No se pudo guardar el administrador en Oracle XE: " + detalle);
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
