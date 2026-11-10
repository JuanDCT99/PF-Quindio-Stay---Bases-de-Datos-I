package model;

import java.time.LocalDateTime;

public class Servicio {
    private Long idServicio;
    private String nombreServicio;
    private String tipoServicio;
    private LocalDateTime horaApertura;
    private LocalDateTime horaCierre;
    private String usoExclusivo;
    private Long idAlojamiento;

    public Servicio() {
    }

    public Servicio(Long idServicio, String nombreServicio, String tipoServicio, LocalDateTime horaApertura, LocalDateTime horaCierre, String usoExclusivo, Long idAlojamiento) {
        this.idServicio = idServicio;
        this.nombreServicio = nombreServicio;
        this.tipoServicio = tipoServicio;
        this.horaApertura = horaApertura;
        this.horaCierre = horaCierre;
        this.usoExclusivo = usoExclusivo;
        this.idAlojamiento = idAlojamiento;
    }

    public Long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public LocalDateTime getHoraApertura() {
        return horaApertura;
    }

    public void setHoraApertura(LocalDateTime horaApertura) {
        this.horaApertura = horaApertura;
    }

    public LocalDateTime getHoraCierre() {
        return horaCierre;
    }

    public void setHoraCierre(LocalDateTime horaCierre) {
        this.horaCierre = horaCierre;
    }

    public String getUsoExclusivo() {
        return usoExclusivo;
    }

    public void setUsoExclusivo(String usoExclusivo) {
        this.usoExclusivo = usoExclusivo;
    }

    public Long getIdAlojamiento() {
        return idAlojamiento;
    }

    public void setIdAlojamiento(Long idAlojamiento) {
        this.idAlojamiento = idAlojamiento;
    }
}
