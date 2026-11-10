
package model;

import java.time.LocalDateTime;

public class ReporteGenerado {

    private Long idReporteGenerado;
    private LocalDateTime fechaGeneracion;
    private String formato;
    private Long idReporte;
    private Long idUsuario;

    public ReporteGenerado() {
    }

    public ReporteGenerado(Long idReporteGenerado, LocalDateTime fechaGeneracion,
                           String formato, Long idReporte, Long idUsuario) {

        this.idReporteGenerado = idReporteGenerado;
        this.fechaGeneracion = fechaGeneracion;
        this.formato = formato;
        this.idReporte = idReporte;
        this.idUsuario = idUsuario;
    }

    public boolean esValido() {
        return fechaGeneracion != null
                && formato != null && !formato.isBlank()
                && formato.length() <= 10
                && idReporte != null
                && idUsuario != null;
    }

    public Long getIdReporteGenerado() { return idReporteGenerado; }
    public void setIdReporteGenerado(Long idReporteGenerado) { this.idReporteGenerado = idReporteGenerado; }

    public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(LocalDateTime fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }

    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }

    public Long getIdReporte() { return idReporte; }
    public void setIdReporte(Long idReporte) { this.idReporte = idReporte; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
}
