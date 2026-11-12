
package model;

import java.time.LocalDateTime;

public class Bloqueo {

    private Long idBloqueo;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String motivo;
    private Long idHabitacion;
    private Long idUsuario;

    public Bloqueo() {
    }

    public Bloqueo(Long idBloqueo, LocalDateTime fechaInicio,
                   LocalDateTime fechaFin, String motivo,
                   Long idHabitacion, Long idUsuario) {

        this.idBloqueo = idBloqueo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.motivo = motivo;
        this.idHabitacion = idHabitacion;
        this.idUsuario = idUsuario;
    }

    public boolean esValido() {
        return fechaInicio != null
                && fechaFin != null
                && !fechaFin.isBefore(fechaInicio)
                && motivo != null && !motivo.isBlank()
                && motivo.length() <= 300
                && idHabitacion != null
                && idUsuario != null;
    }

    public Long getIdBloqueo() { return idBloqueo; }
    public void setIdBloqueo(Long idBloqueo) { this.idBloqueo = idBloqueo; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public Long getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(Long idHabitacion) { this.idHabitacion = idHabitacion; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
}
