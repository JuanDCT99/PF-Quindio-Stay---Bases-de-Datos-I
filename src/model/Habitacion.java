package model;

public class Habitacion {

    private Long idHabitacion;
    private String numeroHabitacion;
    private Integer capacidadMaxima;
    private String descripcion;
    private String estadoOperativo;
    private Long idTipoHabitacion;
    private Long idAlojamiento;

    public Habitacion() {
    }

    public Habitacion(Long idHabitacion, String numeroHabitacion,
                      Integer capacidadMaxima, String descripcion,
                      String estadoOperativo, Long idTipoHabitacion,
                      Long idAlojamiento) {

        this.idHabitacion = idHabitacion;
        this.numeroHabitacion = numeroHabitacion;
        this.capacidadMaxima = capacidadMaxima;
        this.descripcion = descripcion;
        this.estadoOperativo = estadoOperativo;
        this.idTipoHabitacion = idTipoHabitacion;
        this.idAlojamiento = idAlojamiento;
    }

    public boolean esValido() {
        return numeroHabitacion != null
                && !numeroHabitacion.isBlank()
                && capacidadMaxima != null
                && capacidadMaxima > 0
                && tieneEstadoValido()
                && idTipoHabitacion != null
                && idAlojamiento != null;
    }

    public boolean tieneEstadoValido() {
        return "DISPONIBLE".equals(estadoOperativo)
                || "MANTENIMIENTO".equals(estadoOperativo)
                || "FUERA_DE_SERVICIO".equals(estadoOperativo);
    }

    public boolean estaDisponible() {
        return "DISPONIBLE".equals(estadoOperativo);
    }

    public boolean tieneCapacidadPara(int cantidadPersonas) {
        return cantidadPersonas > 0
                && capacidadMaxima != null
                && cantidadPersonas <= capacidadMaxima;
    }

    public Long getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(Long idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public String getNumeroHabitacion() {
        return numeroHabitacion;
    }

    public void setNumeroHabitacion(String numeroHabitacion) {
        this.numeroHabitacion = numeroHabitacion;
    }

    public Integer getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(Integer capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstadoOperativo() {
        return estadoOperativo;
    }

    public void setEstadoOperativo(String estadoOperativo) {
        this.estadoOperativo = estadoOperativo;
    }

    public Long getIdTipoHabitacion() {
        return idTipoHabitacion;
    }

    public void setIdTipoHabitacion(Long idTipoHabitacion) {
        this.idTipoHabitacion = idTipoHabitacion;
    }

    public Long getIdAlojamiento() {
        return idAlojamiento;
    }

    public void setIdAlojamiento(Long idAlojamiento) {
        this.idAlojamiento = idAlojamiento;
    }
}