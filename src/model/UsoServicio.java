package model;

import java.time.LocalDateTime;

public class UsoServicio {
    private Long idUsoServicio;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private String solicitud;
    private Long idReservaHabitacion;
    private Long idServicio;
    private Long idPersonal;

    public UsoServicio() {
    }

    public UsoServicio(Long idUsoServicio, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin, String solicitud, Long idReservaHabitacion, Long idServicio, Long idPersonal) {
        this.idUsoServicio = idUsoServicio;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.solicitud = solicitud;
        this.idReservaHabitacion = idReservaHabitacion;
        this.idServicio = idServicio;
        this.idPersonal = idPersonal;
    }

    public Long getIdUsoServicio() {
        return idUsoServicio;
    }

    public void setIdUsoServicio(Long idUsoServicio) {
        this.idUsoServicio = idUsoServicio;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public String getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(String solicitud) {
        this.solicitud = solicitud;
    }

    public Long getIdReservaHabitacion() {
        return idReservaHabitacion;
    }

    public void setIdReservaHabitacion(Long idReservaHabitacion) {
        this.idReservaHabitacion = idReservaHabitacion;
    }

    public Long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }

    public Long getIdPersonal() {
        return idPersonal;
    }

    public void setIdPersonal(Long idPersonal) {
        this.idPersonal = idPersonal;
    }
}
