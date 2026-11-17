package ViewController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

import model.Huesped;
import service.RegistroHuespedService;

public class RegistroHuespedController {

    private static final DateTimeFormatter[] FORMATOS_FECHA = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("d/M/yyyy")
    };

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
    private TextField fldFechaNacimiento;

    @FXML
    private TextField fldDireccion;

    @FXML
    private TextField fldTelefono;

    @FXML
    private TextField fldCorreo;

    @FXML
    private Button btnCrearCuenta;

    @FXML
    private Button btnVolver;

    @FXML
    private Text txtRegistro;

    @FXML
    private void initialize() {
        if (txtRegistro != null) {
            txtRegistro.setText("Registro de Huésped");
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
                || esVacio(fldDocumento) || esVacio(fldFechaNacimiento)) {
            mostrarAlerta(event, Alert.AlertType.WARNING, "Campos requeridos",
                    "Complete nombre, documento, fecha de nacimiento, usuario y contraseña.");
            return;
        }

        if (fldUser.getText().trim().length() > 50) {
            mostrarAlerta(event, Alert.AlertType.WARNING, "Usuario no válido",
                    "El nombre de usuario no puede superar los 50 caracteres.");
            return;
        }

        LocalDate fechaNacimiento = parsearFecha(fldFechaNacimiento.getText().trim());
        if (fechaNacimiento == null) {
            mostrarAlerta(event, Alert.AlertType.WARNING, "Fecha inválida",
                    "Ingrese la fecha de nacimiento con el formato aaaa-MM-dd (por ejemplo, 1998-05-20).");
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

        Huesped huesped = new Huesped();
        huesped.setIdHuesped(id);
        huesped.setNombre(fldNombre.getText().trim());
        huesped.setDocumento(fldDocumento.getText().trim());
        huesped.setFechaNacimiento(fechaNacimiento);
        huesped.setDireccion(texto(fldDireccion));
        huesped.setTelefono(texto(fldTelefono));
        huesped.setCorreo(texto(fldCorreo));

        if (!huesped.esValido()) {
            mostrarAlerta(event, Alert.AlertType.WARNING, "Datos no válidos",
                    "Revise la información: el nombre admite hasta 80 caracteres, el documento 25, "
                    + "la dirección 200, el correo 120 y el teléfono 25.");
            return;
        }

        try {
            RegistroHuespedService registroService = new RegistroHuespedService();
            registroService.registrar(huesped, fldUser.getText().trim(), fldContrasena.getText().trim());

            mostrarAlerta(event, Alert.AlertType.INFORMATION, "Registro exitoso",
                    "El huésped " + huesped.getNombre() + " fue registrado exitosamente con usuario: "
                    + fldUser.getText().trim() + ".\nYa puede iniciar sesión.");
            volverAlInicioSesion(event);
        } catch (SQLException e) {
            mostrarAlerta(event, Alert.AlertType.ERROR, "Error de registro", mensajeError(e));
        }
    }

    private String mensajeError(SQLException e) {
        switch (e.getErrorCode()) {
            case 1:
                return "El documento del huésped o el nombre de usuario ya están registrados. "
                        + "Verifique los datos e intente de nuevo.";
            case 942:
                return "La base de datos no tiene las tablas del proyecto. "
                        + "Ejecute primero los scripts DDL y DML sobre el esquema QUINDIOSTAY.";
            case 1017:
                return "Credenciales inválidas para Oracle. Revise db.user y db.password en db.properties.";
            case 12541:
            case 17002:
                return "No se pudo conectar con Oracle en localhost:1521. "
                        + "Verifique que el contenedor/servicio Oracle esté iniciado y que el listener escuche en el puerto 1521.";
            default:
                return "No se pudo guardar el huésped en Oracle: " + e.getMessage();
        }
    }

    private LocalDate parsearFecha(String valor) {
        for (DateTimeFormatter formato : FORMATOS_FECHA) {
            try {
                return LocalDate.parse(valor, formato);
            } catch (DateTimeParseException ignored) {
                // Se prueba con el siguiente formato disponible.
            }
        }
        return null;
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

    private String texto(TextField campo) {
        if (campo == null || campo.getText() == null || campo.getText().isBlank()) {
            return null;
        }
        return campo.getText().trim();
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
