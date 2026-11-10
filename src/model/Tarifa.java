
package model;

import java.math.BigDecimal;

public class Tarifa {

    private Long idTarifa;
    private BigDecimal precioNoche;
    private Long idTipoHabitacion;
    private Long idTemporada;

    public Tarifa() {
    }

    public Tarifa(Long idTarifa, BigDecimal precioNoche,
                  Long idTipoHabitacion, Long idTemporada) {

        this.idTarifa = idTarifa;
        this.precioNoche = precioNoche;
        this.idTipoHabitacion = idTipoHabitacion;
        this.idTemporada = idTemporada;
    }

    public boolean esValido() {
        return precioNoche != null
                && precioNoche.compareTo(BigDecimal.ZERO) > 0
                && idTipoHabitacion != null
                && idTemporada != null;
    }

    public Long getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(Long idTarifa) {
        this.idTarifa = idTarifa;
    }

    public BigDecimal getPrecioNoche() {
        return precioNoche;
    }

    public void setPrecioNoche(BigDecimal precioNoche) {
        this.precioNoche = precioNoche;
    }

    public Long getIdTipoHabitacion() {
        return idTipoHabitacion;
    }

    public void setIdTipoHabitacion(Long idTipoHabitacion) {
        this.idTipoHabitacion = idTipoHabitacion;
    }

    public Long getIdTemporada() {
        return idTemporada;
    }

    public void setIdTemporada(Long idTemporada) {
        this.idTemporada = idTemporada;
    }
}
