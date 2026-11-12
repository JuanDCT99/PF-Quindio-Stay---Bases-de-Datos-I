
package model;

public class TipoHabitacion {

    private Long idTipoHabitacion;
    private String nombreTipo;
    private String descripcion;
    private Integer capacidadEstandar;

    public TipoHabitacion() {
    }

    public TipoHabitacion(Long idTipoHabitacion, String nombreTipo,
                          String descripcion, Integer capacidadEstandar) {
        this.idTipoHabitacion = idTipoHabitacion;
        this.nombreTipo = nombreTipo;
        this.descripcion = descripcion;
        this.capacidadEstandar = capacidadEstandar;
    }

    public boolean esValido() {
        return nombreTipo != null
                && !nombreTipo.isBlank()
                && capacidadEstandar != null
                && capacidadEstandar > 0;
    }

    public Long getIdTipoHabitacion() {
        return idTipoHabitacion;
    }

    public void setIdTipoHabitacion(Long idTipoHabitacion) {
        this.idTipoHabitacion = idTipoHabitacion;
    }

    public String getNombreTipo() {
        return nombreTipo;
    }

    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCapacidadEstandar() {
        return capacidadEstandar;
    }

    public void setCapacidadEstandar(Integer capacidadEstandar) {
        this.capacidadEstandar = capacidadEstandar;
    }
}
