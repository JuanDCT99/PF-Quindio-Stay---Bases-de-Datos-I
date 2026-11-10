
package model;

import java.time.LocalDateTime;

public class Notificacion {

    private Long idNotificacion;
    private String tipoNotificacion;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private String estadoEnvio;
    private Long idHuesped;
    private Long idReserva;

    public Notificacion() {
    }

    public Notificacion(Long idNotificacion, String tipoNotificacion,
                        String mensaje, LocalDateTime fechaEnvio,
                        String estadoEnvio, Long idHuesped, Long idReserva) {

        this.idNotificacion = idNotificacion;
        this.tipoNotificacion = tipoNotificacion;
        this.mensaje = mensaje;
        this.fechaEnvio = fechaEnvio;
        this.estadoEnvio = estadoEnvio;
        this.idHuesped = idHuesped;
        this.idReserva = idReserva;
    }

    public boolean esValido() {
        return tipoNotificacion != null && !tipoNotificacion.isBlank()
                && tipoNotificacion.length() <= 30
                && mensaje != null && !mensaje.isBlank()
                && mensaje.length() <= 1000
                && fechaEnvio != null
                && ("ENVIADA".equals(estadoEnvio)
                || "FALLIDA".equals(estadoEnvio)
                || "PENDIENTE".equals(estadoEnvio))
                && idHuesped != null;
    }

    public Long getIdNotificacion() { return idNotificacion; }
    public void setIdNotificacion(Long idNotificacion) { this.idNotificacion = idNotificacion; }

    public String getTipoNotificacion() { return tipoNotificacion; }
    public void setTipoNotificacion(String tipoNotificacion) { this.tipoNotificacion = tipoNotificacion; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public String getEstadoEnvio() { return estadoEnvio; }
    public void setEstadoEnvio(String estadoEnvio) { this.estadoEnvio = estadoEnvio; }

    public Long getIdHuesped() { return idHuesped; }
    public void setIdHuesped(Long idHuesped) { this.idHuesped = idHuesped; }

    public Long getIdReserva() { return idReserva; }
    public void setIdReserva(Long idReserva) { this.idReserva = idReserva; }
}
