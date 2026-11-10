
package model;

import java.math.BigDecimal;

public class Intermediario {

    private Long idIntermediario;
    private String nombreIntermediario;
    private BigDecimal porcentajeComision;
    private String tipoCanal;
    private String emailContacto;

    public Intermediario() {
    }

    public Intermediario(Long idIntermediario,
                         String nombreIntermediario,
                         BigDecimal porcentajeComision,
                         String tipoCanal,
                         String emailContacto) {

        this.idIntermediario = idIntermediario;
        this.nombreIntermediario = nombreIntermediario;
        this.porcentajeComision = porcentajeComision;
        this.tipoCanal = tipoCanal;
        this.emailContacto = emailContacto;
    }

    public boolean esValido() {
        return nombreIntermediario != null
                && !nombreIntermediario.isBlank()
                && nombreIntermediario.length() <= 120
                && porcentajeComision != null
                && porcentajeComision.compareTo(BigDecimal.ZERO) >= 0
                && porcentajeComision.compareTo(new BigDecimal("30")) <= 0
                && tipoCanal != null
                && !tipoCanal.isBlank()
                && tipoCanal.length() <= 30
                && (emailContacto == null || emailContacto.length() <= 120);
    }

    public Long getIdIntermediario() {
        return idIntermediario;
    }

    public void setIdIntermediario(Long idIntermediario) {
        this.idIntermediario = idIntermediario;
    }

    public String getNombreIntermediario() {
        return nombreIntermediario;
    }

    public void setNombreIntermediario(String nombreIntermediario) {
        this.nombreIntermediario = nombreIntermediario;
    }

    public BigDecimal getPorcentajeComision() {
        return porcentajeComision;
    }

    public void setPorcentajeComision(BigDecimal porcentajeComision) {
        this.porcentajeComision = porcentajeComision;
    }

    public String getTipoCanal() {
        return tipoCanal;
    }

    public void setTipoCanal(String tipoCanal) {
        this.tipoCanal = tipoCanal;
    }

    public String getEmailContacto() {
        return emailContacto;
    }

    public void setEmailContacto(String emailContacto) {
        this.emailContacto = emailContacto;
    }
}
