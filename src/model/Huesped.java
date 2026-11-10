package model;

import java.time.LocalDate;

public class Huesped {
    private Long idHuesped;
    private String nombre;
    private String documento;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String correo;
    private String telefono;

    public Huesped() {
    }

    public Huesped(Long idHuesped, String nombre, String documento, LocalDate fechaNacimiento, String direccion, String correo, String telefono) {
        this.idHuesped = idHuesped;
        this.nombre = nombre;
        this.documento = documento;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.correo = correo;
        this.telefono = telefono;
    }

    public Long getIdHuesped() {
        return idHuesped;
    }

    public void setIdHuesped(Long idHuesped) {
        this.idHuesped = idHuesped;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
