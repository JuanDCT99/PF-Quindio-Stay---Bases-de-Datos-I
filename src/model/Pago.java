
package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pago {

    private Long idPago;
    private BigDecimal monto;
    private String medioPago;
    private LocalDateTime fechaPago;
    private Long idReserva;

    public Pago() {
    }

    public Pago(Long idPago, BigDecimal monto, String medioPago,
                LocalDateTime fechaPago, Long idReserva) {

        this.idPago = idPago;
        this.monto = monto;
        this.medioPago = medioPago;
        this.fechaPago = fechaPago;
        this.idReserva = idReserva;
    }

    public boolean esValido() {
        return monto != null
                && monto.compareTo(BigDecimal.ZERO) > 0
                && medioPago != null && !medioPago.isBlank()
                && medioPago.length() <= 25
                && fechaPago != null
                && idReserva != null;
    }

    public Long getIdPago() { return idPago; }
    public void setIdPago(Long idPago) { this.idPago = idPago; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public String getMedioPago() { return medioPago; }
    public void setMedioPago(String medioPago) { this.medioPago = medioPago; }

    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }

    public Long getIdReserva() { return idReserva; }
    public void setIdReserva(Long idReserva) { this.idReserva = idReserva; }
}
