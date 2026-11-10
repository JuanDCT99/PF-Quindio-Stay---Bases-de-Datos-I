
package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Reserva {

    private Long idReserva;
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime horaEstimadaLlegada;
    private LocalDateTime fechaLimiteConfirmacion;
    private String canalDirecto;
    private BigDecimal precioTotal;
    private Long idPlan;
    private Long idTitular;
    private Long idIntermediario;

    public Reserva() {
    }

    public Reserva(Long idReserva, String estado, LocalDateTime fechaCreacion,
                   LocalDateTime horaEstimadaLlegada, LocalDateTime fechaLimiteConfirmacion,
                   String canalDirecto, BigDecimal precioTotal, Long idPlan,
                   Long idTitular, Long idIntermediario) {

        this.idReserva = idReserva;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.horaEstimadaLlegada = horaEstimadaLlegada;
        this.fechaLimiteConfirmacion = fechaLimiteConfirmacion;
        this.canalDirecto = canalDirecto;
        this.precioTotal = precioTotal;
        this.idPlan = idPlan;
        this.idTitular = idTitular;
        this.idIntermediario = idIntermediario;
    }

    public boolean esValido() {
        return estado != null &&
                (estado.equals("PENDIENTE") || estado.equals("CONFIRMADA")
                        || estado.equals("CANCELADA") || estado.equals("NO_SHOW"))
                && fechaCreacion != null
                && fechaLimiteConfirmacion != null
                && ("S".equals(canalDirecto) || "N".equals(canalDirecto))
                && (precioTotal == null || precioTotal.compareTo(BigDecimal.ZERO) >= 0)
                && idPlan != null
                && idTitular != null
                && (!"S".equals(canalDirecto) || idIntermediario != null)
                && (!"N".equals(canalDirecto) || idIntermediario == null);
    }

    public Long getIdReserva() { return idReserva; }
    public void setIdReserva(Long idReserva) { this.idReserva = idReserva; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getHoraEstimadaLlegada() { return horaEstimadaLlegada; }
    public void setHoraEstimadaLlegada(LocalDateTime horaEstimadaLlegada) { this.horaEstimadaLlegada = horaEstimadaLlegada; }

    public LocalDateTime getFechaLimiteConfirmacion() { return fechaLimiteConfirmacion; }
    public void setFechaLimiteConfirmacion(LocalDateTime fechaLimiteConfirmacion) { this.fechaLimiteConfirmacion = fechaLimiteConfirmacion; }

    public String getCanalDirecto() { return canalDirecto; }
    public void setCanalDirecto(String canalDirecto) { this.canalDirecto = canalDirecto; }

    public BigDecimal getPrecioTotal() { return precioTotal; }
    public void setPrecioTotal(BigDecimal precioTotal) { this.precioTotal = precioTotal; }

    public Long getIdPlan() { return idPlan; }
    public void setIdPlan(Long idPlan) { this.idPlan = idPlan; }

    public Long getIdTitular() { return idTitular; }
    public void setIdTitular(Long idTitular) { this.idTitular = idTitular; }

    public Long getIdIntermediario() { return idIntermediario; }
    public void setIdIntermediario(Long idIntermediario) { this.idIntermediario = idIntermediario; }
}
