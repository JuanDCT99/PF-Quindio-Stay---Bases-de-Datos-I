
package model;

public class Reporte {

    private Long idReporte;
    private String nombreReporte;
    private String nivel;
    private String preguntaNegocio;

    public Reporte() {
    }

    public Reporte(Long idReporte, String nombreReporte,
                   String nivel, String preguntaNegocio) {

        this.idReporte = idReporte;
        this.nombreReporte = nombreReporte;
        this.nivel = nivel;
        this.preguntaNegocio = preguntaNegocio;
    }

    public boolean esValido() {
        return nombreReporte != null && !nombreReporte.isBlank()
                && nombreReporte.length() <= 120
                && ("SIMPLE".equals(nivel)
                || "INTERMEDIO".equals(nivel)
                || "AVANZADO".equals(nivel))
                && preguntaNegocio != null
                && !preguntaNegocio.isBlank()
                && preguntaNegocio.length() <= 500;
    }

    public Long getIdReporte() { return idReporte; }
    public void setIdReporte(Long idReporte) { this.idReporte = idReporte; }

    public String getNombreReporte() { return nombreReporte; }
    public void setNombreReporte(String nombreReporte) { this.nombreReporte = nombreReporte; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public String getPreguntaNegocio() { return preguntaNegocio; }
    public void setPreguntaNegocio(String preguntaNegocio) { this.preguntaNegocio = preguntaNegocio; }
}
