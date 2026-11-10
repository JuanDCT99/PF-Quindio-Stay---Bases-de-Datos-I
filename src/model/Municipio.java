package model;

public class Municipio {
    private Long idMunicipio;
    private String nombreMunicipio;
    private String departamento;

    public Municipio() {
    }

    public Municipio(Long idMunicipio, String nombreMunicipio, String departamento) {
        this.idMunicipio = idMunicipio;
        this.nombreMunicipio = nombreMunicipio;
        this.departamento = departamento;
    }

    public Long getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Long idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public String getNombreMunicipio() {
        return nombreMunicipio;
    }

    public void setNombreMunicipio(String nombreMunicipio) {
        this.nombreMunicipio = nombreMunicipio;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
}
