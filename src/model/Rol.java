
package model;

public class Rol {

    private Long idRol;
    private String nombreRol;
    private String descripcion;

    public Rol() {
    }

    public Rol(Long idRol, String nombreRol, String descripcion) {
        this.idRol = idRol;
        this.nombreRol = nombreRol;
        this.descripcion = descripcion;
    }

    public boolean esValido() {
        return nombreRol != null && !nombreRol.isBlank()
                && nombreRol.length() <= 30
                && (descripcion == null || descripcion.length() <= 200);
    }

    public Long getIdRol() { return idRol; }
    public void setIdRol(Long idRol) { this.idRol = idRol; }

    public String getNombreRol() { return nombreRol; }
    public void setNombreRol(String nombreRol) { this.nombreRol = nombreRol; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
