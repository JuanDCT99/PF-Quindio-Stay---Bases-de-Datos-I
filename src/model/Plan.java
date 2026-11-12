
package model;

import java.math.BigDecimal;

public class Plan {

    private Long idPlan;
    private String nombrePlan;
    private String descripcion;
    private String incluyeDesayuno;
    private BigDecimal valorAdicionalNoche;
    private Long idAlojamiento;

    public Plan() {
    }

    public Plan(Long idPlan, String nombrePlan, String descripcion,
                String incluyeDesayuno, BigDecimal valorAdicionalNoche,
                Long idAlojamiento) {

        this.idPlan = idPlan;
        this.nombrePlan = nombrePlan;
        this.descripcion = descripcion;
        this.incluyeDesayuno = incluyeDesayuno;
        this.valorAdicionalNoche = valorAdicionalNoche;
        this.idAlojamiento = idAlojamiento;
    }

    public boolean esValido() {
        return nombrePlan != null
                && !nombrePlan.isBlank()
                && nombrePlan.length() <= 120
                && (descripcion == null || descripcion.length() <= 500)
                && ("S".equals(incluyeDesayuno)
                || "N".equals(incluyeDesayuno))
                && valorAdicionalNoche != null
                && valorAdicionalNoche.compareTo(BigDecimal.ZERO) >= 0
                && idAlojamiento != null;
    }

    public Long getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(Long idPlan) {
        this.idPlan = idPlan;
    }

    public String getNombrePlan() {
        return nombrePlan;
    }

    public void setNombrePlan(String nombrePlan) {
        this.nombrePlan = nombrePlan;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIncluyeDesayuno() {
        return incluyeDesayuno;
    }

    public void setIncluyeDesayuno(String incluyeDesayuno) {
        this.incluyeDesayuno = incluyeDesayuno;
    }

    public BigDecimal getValorAdicionalNoche() {
        return valorAdicionalNoche;
    }

    public void setValorAdicionalNoche(BigDecimal valorAdicionalNoche) {
        this.valorAdicionalNoche = valorAdicionalNoche;
    }

    public Long getIdAlojamiento() {
        return idAlojamiento;
    }

    public void setIdAlojamiento(Long idAlojamiento) {
        this.idAlojamiento = idAlojamiento;
    }
}
