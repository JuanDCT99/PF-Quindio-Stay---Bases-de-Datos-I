
package model;

public class Ocupacion {

    private Long idReservaHabitacion;
    private Long idHuesped;
    private String rol;

    public Ocupacion() {
    }

    public Ocupacion(Long idReservaHabitacion, Long idHuesped, String rol) {
        this.idReservaHabitacion = idReservaHabitacion;
        this.idHuesped = idHuesped;
        this.rol = rol;
    }

    public boolean esValido() {
        return idReservaHabitacion != null
                && idHuesped != null
                && ("TITULAR".equals(rol) || "ACOMPANANTE".equals(rol));
    }

    public Long getIdReservaHabitacion() { return idReservaHabitacion; }
    public void setIdReservaHabitacion(Long idReservaHabitacion) { this.idReservaHabitacion = idReservaHabitacion; }

    public Long getIdHuesped() { return idHuesped; }
    public void setIdHuesped(Long idHuesped) { this.idHuesped = idHuesped; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
