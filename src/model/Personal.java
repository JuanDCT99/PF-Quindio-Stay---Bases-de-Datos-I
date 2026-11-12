
package model;

import java.math.BigDecimal;

public class Personal {

    private Long idPersonal;
    private String nombre;
    private String funcion;
    private String cargo;
    private BigDecimal salario;
    private String horarioTrabajo;
    private Long idAlojamiento;

    public Personal() {
    }

    public Personal(Long idPersonal, String nombre, String funcion, String cargo,
                    BigDecimal salario, String horarioTrabajo, Long idAlojamiento) {

        this.idPersonal = idPersonal;
        this.nombre = nombre;
        this.funcion = funcion;
        this.cargo = cargo;
        this.salario = salario;
        this.horarioTrabajo = horarioTrabajo;
        this.idAlojamiento = idAlojamiento;
    }

    public boolean esValido() {
        return nombre != null && !nombre.isBlank() && nombre.length() <= 120
                && funcion != null && !funcion.isBlank() && funcion.length() <= 100
                && cargo != null && !cargo.isBlank() && cargo.length() <= 60
                && salario != null && salario.compareTo(BigDecimal.ZERO) >= 0
                && horarioTrabajo != null && !horarioTrabajo.isBlank()
                && horarioTrabajo.length() <= 100
                && idAlojamiento != null;
    }

    public Long getIdPersonal() { return idPersonal; }
    public void setIdPersonal(Long idPersonal) { this.idPersonal = idPersonal; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getFuncion() { return funcion; }
    public void setFuncion(String funcion) { this.funcion = funcion; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }

    public String getHorarioTrabajo() { return horarioTrabajo; }
    public void setHorarioTrabajo(String horarioTrabajo) { this.horarioTrabajo = horarioTrabajo; }

    public Long getIdAlojamiento() { return idAlojamiento; }
    public void setIdAlojamiento(Long idAlojamiento) { this.idAlojamiento = idAlojamiento; }
}
