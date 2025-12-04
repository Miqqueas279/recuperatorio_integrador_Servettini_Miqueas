/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.farmacia.inventario;

/**
 *
 * @author PC
 */
public class Medicamento extends ProductoFarmaceutico {
    private boolean requiereReceta;

    // Constructor
    public Medicamento(String nombreComercial, String dosis, String fechaVencimiento, boolean requiereReceta)
            throws DatoInvalidoException {
        super(nombreComercial, dosis, fechaVencimiento);
        this.requiereReceta = requiereReceta;
    }

    public Medicamento(String nombreComercial, String dosis, String fechaVencimiento, String requiereRecetaStr)
            throws DatoInvalidoException {
        this(nombreComercial, dosis, fechaVencimiento, Boolean.parseBoolean(requiereRecetaStr));
    }

    @Override
    public String toFileString() {
        return String.format("M;%s;%s;%s;%b",
                getNombreComercial(),
                getDosis(),
                getFechaVencimientoString(),
                requiereReceta);
    }

    @Override
    public String toString() {
        String base = super.toString();
        String recetaInfo = requiereReceta ? " (Requiere Receta)" : " (Venta Libre)";
        return "Medicamento - " + base + recetaInfo;
    }

    public boolean isRequiereReceta() {
        return requiereReceta;
    }

    public void setRequiereReceta(boolean requiereReceta) {
        this.requiereReceta = requiereReceta;
    }
}