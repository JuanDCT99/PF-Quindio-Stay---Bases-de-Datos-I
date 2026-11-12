
package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Sancion {

    private Long idSancion;
    private String tipoSancion;
    private String descripcion;
    private LocalDateTime fechaSancion;
    private BigDecimal valor;
    private Long idReserva;
    private Long idServicio;

    public Sancion() {
    }

    public Sancion(Long idSancion, String tipoSancion, String descripcion,
                   LocalDateTime fechaSancion, BigDecimal valor,
                   Long idReserva, Long idServicio) {

        this.idSancion = idSancion;
        this.tipoSancion = tipoSancion;
        this.descripcion = descripcion;
        this.fechaSancion = fechaSancion;
        this.valor = valor;
        this.idReserva = idReserva;
        this.idServicio = idServicio;
    }

    public boolean esValido() {
        return tipoSancion != null && !tipoSancion.isBlank()
                && tipoSancion.length() <= 30
                && descripcion != null && !descripcion.isBlank()
                && descripcion.length() <= 500
                && fechaSancion != null
                && valor != null && valor.compareTo(BigDecimal.ZERO) > 0
                && idReserva != null;
    }

    public Long getIdSancion() { return idSancion; }
    public void setIdSancion(Long idSancion) { this.idSancion = idSancion; }

    public String getTipoSancion() { return tipoSancion; }
    public void setTipoSancion(String tipoSancion) { this.tipoSancion = tipoSancion; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFechaSancion() { return fechaSancion; }
    public void setFechaSancion(LocalDateTime fechaSancion) { this.fechaSancion = fechaSancion; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public Long getIdReserva() { return idReserva; }
    public void setIdReserva(Long idReserva) { this.idReserva = idReserva; }

    public Long getIdServicio() { return idServicio; }
    public void setIdServicio(Long idServicio) { this.idServicio = idServicio; }
}
