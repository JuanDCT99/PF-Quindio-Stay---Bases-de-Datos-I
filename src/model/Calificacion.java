package model;

import java.time.LocalDateTime;

public class Calificacion {
    private Long idCalificacion;
    private Integer puntuacion;
    private String comentario;
    private LocalDateTime fechaCalificacion;
    private Long idHuesped;
    private Long idReserva;

    public Calificacion() {
    }

    public Calificacion(Long idCalificacion, Integer puntuacion, String comentario, LocalDateTime fechaCalificacion, Long idHuesped, Long idReserva) {
        this.idCalificacion = idCalificacion;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.fechaCalificacion = fechaCalificacion;
        this.idHuesped = idHuesped;
        this.idReserva = idReserva;
    }

    public Long getIdCalificacion() {
        return idCalificacion;
    }

    public void setIdCalificacion(Long idCalificacion) {
        this.idCalificacion = idCalificacion;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getFechaCalificacion() {
        return fechaCalificacion;
    }

    public void setFechaCalificacion(LocalDateTime fechaCalificacion) {
        this.fechaCalificacion = fechaCalificacion;
    }

    public Long getIdHuesped() {
        return idHuesped;
    }

    public void setIdHuesped(Long idHuesped) {
        this.idHuesped = idHuesped;
    }

    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }
}
