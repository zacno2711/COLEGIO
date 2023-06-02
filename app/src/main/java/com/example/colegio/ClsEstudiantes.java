package com.example.colegio;

public class ClsEstudiantes {
    private String carnet;
    private String nombre;
    private String carrera;
    private String semestre;
    private String activo;

    public ClsEstudiantes(String carnet, String nombre, String carrera, String semestre, String activo) {
        this.carnet = carnet;
        this.nombre = nombre;
        this.carrera = carrera;
        this.semestre = semestre;
        this.activo = activo;
    }

    public ClsEstudiantes() {
    }

    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }
}
