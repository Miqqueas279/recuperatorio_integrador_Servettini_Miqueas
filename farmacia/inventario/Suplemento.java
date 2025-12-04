/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.farmacia.inventario;

/**
 *
 * @author PC
 */
public class Suplemento extends ProductoFarmaceutico {
    private ObjetivoSuplemento objetivo;

    public Suplemento(String nombreComercial, String dosis, String fechaVencimiento, ObjetivoSuplemento objetivo)
            throws DatoInvalidoException {
        super(nombreComercial, dosis, fechaVencimiento);
        if (objetivo == null) {
            throw new DatoInvalidoException("Debe especificar un objetivo para el suplemento.");
        }
        this.objetivo = objetivo;
    }

    public Suplemento(String nombreComercial, String dosis, String fechaVencimiento, String objetivoStr)
            throws DatoInvalidoException {
        super(nombreComercial, dosis, fechaVencimiento);
        try {

            this.objetivo = ObjetivoSuplemento.valueOf(objetivoStr.toUpperCase().replace(' ', '_').replace('Á', 'A'));
        } catch (IllegalArgumentException e) {
            throw new DatoInvalidoException("El objetivo del suplemento cargado desde archivo no es válido.");
        }
    }

    @Override
    public String toFileString() {
        return String.format("S;%s;%s;%s;%s",
                getNombreComercial(),
                getDosis(),
                getFechaVencimientoString(),
                objetivo.name());
    }

    @Override
    public String toString() {
        String base = super.toString();
        return "Suplemento - " + base + String.format(" (Objetivo: %s)", objetivo.getNombreVisible());
    }

    public ObjetivoSuplemento getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(ObjetivoSuplemento objetivo) throws DatoInvalidoException {
        if (objetivo == null) {
            throw new DatoInvalidoException("El objetivo no puede ser nulo.");
        }
        this.objetivo = objetivo;
    }
}
