package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReservaHabitacion {
    private Long idReservaHabitacion;
    private LocalDate fechaEntradaReservada;
    private LocalDate fechaSalidaReservada;
    private BigDecimal precioNochePactado;
    private LocalDateTime checkinReal;
    private LocalDateTime checkoutReal;
    private Integer nochesAdicionales;
    private Long idReserva;
    private Long idHabitacion;

    public ReservaHabitacion() {
    }

    public ReservaHabitacion(Long idReservaHabitacion, LocalDate fechaEntradaReservada, LocalDate fechaSalidaReservada, BigDecimal precioNochePactado, LocalDateTime checkinReal, LocalDateTime checkoutReal, Integer nochesAdicionales, Long idReserva, Long idHabitacion) {
        this.idReservaHabitacion = idReservaHabitacion;
        this.fechaEntradaReservada = fechaEntradaReservada;
        this.fechaSalidaReservada = fechaSalidaReservada;
        this.precioNochePactado = precioNochePactado;
        this.checkinReal = checkinReal;
        this.checkoutReal = checkoutReal;
        this.nochesAdicionales = nochesAdicionales;
        this.idReserva = idReserva;
        this.idHabitacion = idHabitacion;
    }

    public Long getIdReservaHabitacion() {
        return idReservaHabitacion;
    }

    public void setIdReservaHabitacion(Long idReservaHabitacion) {
        this.idReservaHabitacion = idReservaHabitacion;
    }

    public LocalDate getFechaEntradaReservada() {
        return fechaEntradaReservada;
    }

    public void setFechaEntradaReservada(LocalDate fechaEntradaReservada) {
        this.fechaEntradaReservada = fechaEntradaReservada;
    }

    public LocalDate getFechaSalidaReservada() {
        return fechaSalidaReservada;
    }

    public void setFechaSalidaReservada(LocalDate fechaSalidaReservada) {
        this.fechaSalidaReservada = fechaSalidaReservada;
    }

    public BigDecimal getPrecioNochePactado() {
        return precioNochePactado;
    }

    public void setPrecioNochePactado(BigDecimal precioNochePactado) {
        this.precioNochePactado = precioNochePactado;
    }

    public LocalDateTime getCheckinReal() {
        return checkinReal;
    }

    public void setCheckinReal(LocalDateTime checkinReal) {
        this.checkinReal = checkinReal;
    }

    public LocalDateTime getCheckoutReal() {
        return checkoutReal;
    }

    public void setCheckoutReal(LocalDateTime checkoutReal) {
        this.checkoutReal = checkoutReal;
    }

    public Integer getNochesAdicionales() {
        return nochesAdicionales;
    }

    public void setNochesAdicionales(Integer nochesAdicionales) {
        this.nochesAdicionales = nochesAdicionales;
    }

    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public Long getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(Long idHabitacion) {
        this.idHabitacion = idHabitacion;
    }
}
