
package model;

import java.time.LocalDateTime;

public class Folio {

    private Long idReserva;
    private LocalDateTime fechaApertura;
    private LocalDateTime fechaCierre;
    private String estado;

    public Folio() {
    }

    public Folio(Long idReserva, LocalDateTime fechaApertura,
                 LocalDateTime fechaCierre, String estado) {

        this.idReserva = idReserva;
        this.fechaApertura = fechaApertura;
        this.fechaCierre = fechaCierre;
        this.estado = estado;
    }

    public boolean esValido() {
        return idReserva != null
                && fechaApertura != null
                && ("ABIERTO".equals(estado) || "CERRADO".equals(estado))
                && (fechaCierre == null || !fechaCierre.isBefore(fechaApertura))
                && !("ABIERTO".equals(estado) && fechaCierre != null)
                && !("CERRADO".equals(estado) && fechaCierre == null);
    }

    public Long getIdReserva() { return idReserva; }
    public void setIdReserva(Long idReserva) { this.idReserva = idReserva; }

    public LocalDateTime getFechaApertura() { return fechaApertura; }
    public void setFechaApertura(LocalDateTime fechaApertura) { this.fechaApertura = fechaApertura; }

    public LocalDateTime getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(LocalDateTime fechaCierre) { this.fechaCierre = fechaCierre; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
