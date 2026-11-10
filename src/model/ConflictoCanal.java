
package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ConflictoCanal {

    private Long idConflicto;
    private LocalDateTime fechaRegistro;
    private LocalDate fechaEntradaSolicitada;
    private LocalDate fechaSalidaSolicitada;
    private String estadoRevision;
    private String observacion;
    private Long idIntermediario;
    private Long idHabitacion;
    private Long idReserva;
    private Long idUsuario;

    public ConflictoCanal() {
    }

    public ConflictoCanal(Long idConflicto, LocalDateTime fechaRegistro,
                          LocalDate fechaEntradaSolicitada,
                          LocalDate fechaSalidaSolicitada,
                          String estadoRevision, String observacion,
                          Long idIntermediario, Long idHabitacion,
                          Long idReserva, Long idUsuario) {

        this.idConflicto = idConflicto;
        this.fechaRegistro = fechaRegistro;
        this.fechaEntradaSolicitada = fechaEntradaSolicitada;
        this.fechaSalidaSolicitada = fechaSalidaSolicitada;
        this.estadoRevision = estadoRevision;
        this.observacion = observacion;
        this.idIntermediario = idIntermediario;
        this.idHabitacion = idHabitacion;
        this.idReserva = idReserva;
        this.idUsuario = idUsuario;
    }

    public boolean esValido() {
        return fechaRegistro != null
                && fechaEntradaSolicitada != null
                && fechaSalidaSolicitada != null
                && !fechaSalidaSolicitada.isBefore(fechaEntradaSolicitada)
                && ("PENDIENTE".equals(estadoRevision)
                || "EN_REVISION".equals(estadoRevision)
                || "RESUELTO".equals(estadoRevision)
                || "DESCARTADO".equals(estadoRevision))
                && (observacion == null || observacion.length() <= 1000)
                && idIntermediario != null
                && idHabitacion != null
                && idReserva != null;
    }

    public Long getIdConflicto() { return idConflicto; }
    public void setIdConflicto(Long idConflicto) { this.idConflicto = idConflicto; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public LocalDate getFechaEntradaSolicitada() { return fechaEntradaSolicitada; }
    public void setFechaEntradaSolicitada(LocalDate fechaEntradaSolicitada) { this.fechaEntradaSolicitada = fechaEntradaSolicitada; }

    public LocalDate getFechaSalidaSolicitada() { return fechaSalidaSolicitada; }
    public void setFechaSalidaSolicitada(LocalDate fechaSalidaSolicitada) { this.fechaSalidaSolicitada = fechaSalidaSolicitada; }

    public String getEstadoRevision() { return estadoRevision; }
    public void setEstadoRevision(String estadoRevision) { this.estadoRevision = estadoRevision; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public Long getIdIntermediario() { return idIntermediario; }
    public void setIdIntermediario(Long idIntermediario) { this.idIntermediario = idIntermediario; }

    public Long getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(Long idHabitacion) { this.idHabitacion = idHabitacion; }

    public Long getIdReserva() { return idReserva; }
    public void setIdReserva(Long idReserva) { this.idReserva = idReserva; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
}
