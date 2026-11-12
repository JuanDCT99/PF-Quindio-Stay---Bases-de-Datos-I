
package model;

import java.time.LocalDate;

public class Temporada {

    private Long idTemporada;
    private String nombreTemporada;
    private Integer anio;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Temporada() {
    }

    public Temporada(Long idTemporada, String nombreTemporada,
                     Integer anio, LocalDate fechaInicio,
                     LocalDate fechaFin) {

        this.idTemporada = idTemporada;
        this.nombreTemporada = nombreTemporada;
        this.anio = anio;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public boolean esValido() {
        return nombreTemporada != null
                && (nombreTemporada.equals("ALTA")
                || nombreTemporada.equals("MEDIA")
                || nombreTemporada.equals("BAJA"))
                && anio != null
                && anio >= 1000
                && anio <= 9999
                && fechaInicio != null
                && fechaFin != null
                && !fechaFin.isBefore(fechaInicio);
    }

    public Long getIdTemporada() {
        return idTemporada;
    }

    public void setIdTemporada(Long idTemporada) {
        this.idTemporada = idTemporada;
    }

    public String getNombreTemporada() {
        return nombreTemporada;
    }

    public void setNombreTemporada(String nombreTemporada) {
        this.nombreTemporada = nombreTemporada;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
}
