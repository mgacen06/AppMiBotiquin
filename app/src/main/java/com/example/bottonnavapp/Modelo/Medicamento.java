package com.example.bottonnavapp.Modelo;

public class Medicamento {

    private String CantidadDosis;
    private String FechaCaducidad;
    private String NombreMedicamento;
    private String Uso;
    private String ConReceta;
    private String IdUsuario;

    public Medicamento(String cantidadDosis, String fechaCaducidad, String nombreMedicamento, String uso, String conReceta, String idUsuario) {
        CantidadDosis = cantidadDosis;
        FechaCaducidad = fechaCaducidad;
        NombreMedicamento = nombreMedicamento;
        Uso = uso;
        ConReceta = conReceta;
        IdUsuario = idUsuario;
    }

    public String getCantidadDosis() {
        return CantidadDosis;
    }

    public void setCantidadDosis(String cantidadDosis) {
        CantidadDosis = cantidadDosis;
    }

    public String getFechaCaducidad() {
        return FechaCaducidad;
    }

    public void setFechaCaducidad(String fechaCaducidad) {
        FechaCaducidad = fechaCaducidad;
    }

    public String getNombreMedicamento() {
        return NombreMedicamento;
    }

    public void setNombreMedicamento(String nombreMedicamento) {
        NombreMedicamento = nombreMedicamento;
    }

    public String getUso() {
        return Uso;
    }

    public void setUso(String uso) {
        Uso = uso;
    }

    public String getConReceta() {
        return ConReceta;
    }

    public void setConReceta(String conReceta) {
        ConReceta = conReceta;
    }

    public String getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        IdUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return NombreMedicamento;
    }
}
