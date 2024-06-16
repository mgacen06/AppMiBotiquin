package com.example.bottonnavapp.Modelo;

public class Medicamento {

    private String IdUsuario;
    private String NombreMedicamento;
    private boolean ConReceta;
    private String FechaCaducidad;
    private int CantidadDosis;
    // Añadir un campo para el ID del documento
    private String id;




    public Medicamento() {
    }

    public Medicamento(String idUsuario, String nombreMedicamento, boolean conReceta, String fechaCaducidad, int cantidadDosis) {
        CantidadDosis = cantidadDosis;
        FechaCaducidad = fechaCaducidad;
        NombreMedicamento = nombreMedicamento;
        ConReceta = conReceta;
        IdUsuario = idUsuario;
    }

    public int getCantidadDosis() {
        return CantidadDosis;
    }

    public void setCantidadDosis(int cantidadDosis) {
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

    public boolean isConReceta() {
        return ConReceta;
    }

    public void setConReceta(boolean conReceta) {
        ConReceta = conReceta;
    }

    public String getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
