
package model;

public class TelefonoAlojamiento {

    private Long idAlojamiento;
    private String telefono;

    public TelefonoAlojamiento() {
    }

    public TelefonoAlojamiento(Long idAlojamiento, String telefono) {
        this.idAlojamiento = idAlojamiento;
        this.telefono = telefono;
    }

    public boolean esValido() {
        return idAlojamiento != null
                && telefono != null
                && telefono.matches("^[0-9+]{7,15}$");
    }

    public Long getIdAlojamiento() {
        return idAlojamiento;
    }

    public void setIdAlojamiento(Long idAlojamiento) {
        this.idAlojamiento = idAlojamiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
