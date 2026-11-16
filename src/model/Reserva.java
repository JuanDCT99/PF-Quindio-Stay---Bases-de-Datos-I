package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public class Reserva {

    public static final String PENDIENTE = "PENDIENTE";
    public static final String CONFIRMADA = "CONFIRMADA";
    public static final String CANCELADA = "CANCELADA";

    private static final Set<String> ESTADOS_VALIDOS = Set.of(
            PENDIENTE,
            CONFIRMADA,
            CANCELADA
    );

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
                   LocalDateTime horaEstimadaLlegada,
                   LocalDateTime fechaLimiteConfirmacion,
                   String canalDirecto, BigDecimal precioTotal,
                   Long idPlan, Long idTitular, Long idIntermediario) {

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

    /**
     * Valida las condiciones propias de una reserva.
     * Las reglas que requieren consultar habitaciones, ocupaciones
     * u otras reservas se validarán desde los servicios.
     */
    public boolean esValido() {
        return tieneEstadoValido()
                && tieneFechaCreacionValida()
                && tieneCanalConsistente()
                && tienePrecioTotalValido()
                && tieneReferenciasObligatorias()
                && tieneHoraLlegadaValida();
    }

    public boolean tieneEstadoValido() {
        return estado != null && ESTADOS_VALIDOS.contains(estado);
    }

    public boolean tieneFechaCreacionValida() {
        return fechaCreacion != null;
    }

    /**
     * una reserva confirmada debe tener hora estimada de llegada.
     */
    public boolean tieneHoraLlegadaValida() {
        return !CONFIRMADA.equals(estado) || horaEstimadaLlegada != null;
    }

    /**
     * canalDirecto = "S" si y solo si idIntermediario es nulo.
     */
    public boolean tieneCanalConsistente() {
        if (canalDirecto == null) {
            return false;
        }

        if ("S".equals(canalDirecto)) {
            return idIntermediario == null;
        }

        if ("N".equals(canalDirecto)) {
            return idIntermediario != null;
        }

        return false;
    }

    public boolean tienePrecioTotalValido() {
        return precioTotal == null
                || precioTotal.compareTo(BigDecimal.ZERO) >= 0;
    }

    public boolean tieneReferenciasObligatorias() {
        return idPlan != null && idTitular != null;
    }

    /**
     * Comprueba si la reserva pendiente ya superó su plazo
     * de confirmación. Se recibe la fecha actual para facilitar
     * las pruebas y evitar depender directamente del reloj del sistema.
     */
    public boolean estaVencida(LocalDateTime fechaActual) {
        if (fechaActual == null
                || !PENDIENTE.equals(estado)
                || fechaLimiteConfirmacion == null) {
            return false;
        }

        return fechaActual.isAfter(fechaLimiteConfirmacion);
    }

    /**
     * RN-08:
     * cancela una reserva pendiente cuyo plazo de confirmación venció.
     *
     * Devuelve true si se realizó el cambio de estado.
     */
    public boolean cancelarSiEstaVencida(LocalDateTime fechaActual) {
        if (!estaVencida(fechaActual)) {
            return false;
        }

        estado = CANCELADA;
        return true;
    }

    /**
     * Indica si la reserva puede pasar de PENDIENTE a CONFIRMADA.
     * La verificación de que tenga al menos una habitación corresponde
     * a ReservaService, porque esta clase no contiene esa colección.
     */
    public boolean puedeConfirmarse(LocalDateTime fechaActual) {
        if (!PENDIENTE.equals(estado) || fechaActual == null) {
            return false;
        }

        if (fechaLimiteConfirmacion != null
                && fechaActual.isAfter(fechaLimiteConfirmacion)) {
            return false;
        }

        return tieneHoraLlegadaValida()
                && tieneCanalConsistente()
                && tieneReferenciasObligatorias();
    }

    /**
     * Cambia el estado a CONFIRMADA si se cumplen las validaciones
     * propias de la reserva.
     *
     * La existencia de habitaciones debe comprobarse previamente
     * desde el servicio de reservas.
     */
    public boolean confirmar(LocalDateTime fechaActual) {
        if (!puedeConfirmarse(fechaActual)) {
            return false;
        }

        estado = CONFIRMADA;
        return true;
    }

    /**
     * Permite cancelar una reserva pendiente o confirmada.
     * No permite cancelar una reserva que ya esté cancelada.
     */
    public boolean cancelar() {
        if (!PENDIENTE.equals(estado) && !CONFIRMADA.equals(estado)) {
            return false;
        }

        estado = CANCELADA;
        return true;
    }

    // Getters y setters

    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getHoraEstimadaLlegada() {
        return horaEstimadaLlegada;
    }

    public void setHoraEstimadaLlegada(LocalDateTime horaEstimadaLlegada) {
        this.horaEstimadaLlegada = horaEstimadaLlegada;
    }

    public LocalDateTime getFechaLimiteConfirmacion() {
        return fechaLimiteConfirmacion;
    }

    public void setFechaLimiteConfirmacion(LocalDateTime fechaLimiteConfirmacion) {
        this.fechaLimiteConfirmacion = fechaLimiteConfirmacion;
    }

    public String getCanalDirecto() {
        return canalDirecto;
    }

    public void setCanalDirecto(String canalDirecto) {
        this.canalDirecto = canalDirecto;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public Long getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(Long idPlan) {
        this.idPlan = idPlan;
    }

    public Long getIdTitular() {
        return idTitular;
    }

    public void setIdTitular(Long idTitular) {
        this.idTitular = idTitular;
    }

    public Long getIdIntermediario() {
        return idIntermediario;
    }

    public void setIdIntermediario(Long idIntermediario) {
        this.idIntermediario = idIntermediario;
    }
}