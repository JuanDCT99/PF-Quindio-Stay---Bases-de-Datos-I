package model;

import java.time.LocalDateTime;

public class Alojamiento {
    private Long idAlojamiento;
    private String nombreAlojamiento;
    private String nit;
    private String direccion;
    private String telefono;
    private String aceptaMascotas;
    private LocalDateTime horaSalidaLimite;
    private Integer plazoConfirmacionHoras;

    public Alojamiento() {
    }

    public Alojamiento(Long idAlojamiento, String nombreAlojamiento, String nit, String direccion, String telefono, String aceptaMascotas, LocalDateTime horaSalidaLimite, Integer plazoConfirmacionHoras) {
        this.idAlojamiento = idAlojamiento;
        this.nombreAlojamiento = nombreAlojamiento;
        this.nit = nit;
        this.direccion = direccion;
        this.telefono = telefono;
        this.aceptaMascotas = aceptaMascotas;
        this.horaSalidaLimite = horaSalidaLimite;
        this.plazoConfirmacionHoras = plazoConfirmacionHoras;
    }

    public Long getIdAlojamiento() {
        return idAlojamiento;
    }

    public void setIdAlojamiento(Long idAlojamiento) {
        this.idAlojamiento = idAlojamiento;
    }

    public String getNombreAlojamiento() {
        return nombreAlojamiento;
    }

    public void setNombreAlojamiento(String nombreAlojamiento) {
        this.nombreAlojamiento = nombreAlojamiento;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getAceptaMascotas() {
        return aceptaMascotas;
    }

    public void setAceptaMascotas(String aceptaMascotas) {
        this.aceptaMascotas = aceptaMascotas;
    }

    public LocalDateTime getHoraSalidaLimite() {
        return horaSalidaLimite;
    }

    public void setHoraSalidaLimite(LocalDateTime horaSalidaLimite) {
        this.horaSalidaLimite = horaSalidaLimite;
    }

    public Integer getPlazoConfirmacionHoras() {
        return plazoConfirmacionHoras;
    }

    public void setPlazoConfirmacionHoras(Integer plazoConfirmacionHoras) {
        this.plazoConfirmacionHoras = plazoConfirmacionHoras;
    }
}
