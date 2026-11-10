
package model;

public class PerfilCliente {

    private Long idPerfilCliente;
    private String viajaConMascota;
    private String deseaDesayuno;
    private String observaciones;
    private Long idHuesped;
    private Long idTipoHabitacion;

    public PerfilCliente() {
    }

    public PerfilCliente(Long idPerfilCliente, String viajaConMascota,
                         String deseaDesayuno, String observaciones,
                         Long idHuesped, Long idTipoHabitacion) {

        this.idPerfilCliente = idPerfilCliente;
        this.viajaConMascota = viajaConMascota;
        this.deseaDesayuno = deseaDesayuno;
        this.observaciones = observaciones;
        this.idHuesped = idHuesped;
        this.idTipoHabitacion = idTipoHabitacion;
    }

    public boolean esValido() {
        return ("S".equals(viajaConMascota)
                || "N".equals(viajaConMascota))
                && ("S".equals(deseaDesayuno)
                || "N".equals(deseaDesayuno))
                && (observaciones == null || observaciones.length() <= 500)
                && idHuesped != null;
    }

    public Long getIdPerfilCliente() {
        return idPerfilCliente;
    }

    public void setIdPerfilCliente(Long idPerfilCliente) {
        this.idPerfilCliente = idPerfilCliente;
    }

    public String getViajaConMascota() {
        return viajaConMascota;
    }

    public void setViajaConMascota(String viajaConMascota) {
        this.viajaConMascota = viajaConMascota;
    }

    public String getDeseaDesayuno() {
        return deseaDesayuno;
    }

    public void setDeseaDesayuno(String deseaDesayuno) {
        this.deseaDesayuno = deseaDesayuno;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Long getIdHuesped() {
        return idHuesped;
    }

    public void setIdHuesped(Long idHuesped) {
        this.idHuesped = idHuesped;
    }

    public Long getIdTipoHabitacion() {
        return idTipoHabitacion;
    }

    public void setIdTipoHabitacion(Long idTipoHabitacion) {
        this.idTipoHabitacion = idTipoHabitacion;
    }
}
