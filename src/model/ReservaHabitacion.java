package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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

    public ReservaHabitacion(Long idReservaHabitacion,
                             LocalDate fechaEntradaReservada,
                             LocalDate fechaSalidaReservada,
                             BigDecimal precioNochePactado,
                             LocalDateTime checkinReal,
                             LocalDateTime checkoutReal,
                             Integer nochesAdicionales,
                             Long idReserva,
                             Long idHabitacion) {

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

    /**
     * Valida las condiciones propias de la relación
     * entre una reserva y una habitación.
     *
     * Las reglas que necesitan consultar otras reservas,
     * habitaciones o alojamientos se validan desde servicios.
     */
    public boolean esValido() {
        return tieneFechasReservadasValidas()
                && tienePrecioPactadoValido()
                && tieneNochesAdicionalesValidas()
                && tieneReferenciasValidas()
                && tieneCheckinValido()
                && tieneCheckoutValido();
    }

    /**
     * RN-02:
     * La estancia debe tener al menos una noche.
     * La fecha de salida debe ser posterior a la entrada.
     */
    public boolean tieneFechasReservadasValidas() {
        return fechaEntradaReservada != null
                && fechaSalidaReservada != null
                && fechaSalidaReservada.isAfter(fechaEntradaReservada);
    }

    /**
     * El precio pactado por noche debe ser mayor que cero.
     */
    public boolean tienePrecioPactadoValido() {
        return precioNochePactado != null
                && precioNochePactado.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Las noches adicionales no pueden ser negativas.
     */
    public boolean tieneNochesAdicionalesValidas() {
        return nochesAdicionales != null
                && nochesAdicionales >= 0;
    }

    /**
     * La relación debe apuntar a una reserva y a una habitación.
     */
    public boolean tieneReferenciasValidas() {
        return idReserva != null
                && idHabitacion != null;
    }

    /**
     * Si existe un check-in real, no puede ser anterior
     * al inicio de la fecha de entrada reservada.
     *
     * La autorización del check-in según el estado de la reserva
     * corresponde a ReservaService.
     */
    public boolean tieneCheckinValido() {
        if (checkinReal == null) {
            return true;
        }

        if (fechaEntradaReservada == null) {
            return false;
        }

        return !checkinReal.isBefore(
                fechaEntradaReservada.atStartOfDay()
        );
    }

    /**
     * Si existe un checkout real:
     * - debe existir un check-in real;
     * - debe ser posterior al check-in.
     */
    public boolean tieneCheckoutValido() {
        if (checkoutReal == null) {
            return true;
        }

        return checkinReal != null
                && checkoutReal.isAfter(checkinReal);
    }

    /**
     * Calcula la cantidad de noches reservadas.
     *
     * La fecha de salida no se cuenta como noche ocupada.
     *
     * Ejemplo:
     * Entrada: 10 de noviembre.
     * Salida: 13 de noviembre.
     * Resultado: 3 noches.
     *
     * Devuelve -1 si las fechas no son válidas.
     */
    public long calcularNochesReservadas() {
        if (!tieneFechasReservadasValidas()) {
            return -1;
        }

        return ChronoUnit.DAYS.between(
                fechaEntradaReservada,
                fechaSalidaReservada
        );
    }

    // Getters y setters

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