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

    // Constructor
    public Suplemento(String nombreComercial, String dosis, String fechaVencimiento, ObjetivoSuplemento objetivo)
            throws DatoInvalidoException {
        super(nombreComercial, dosis, fechaVencimiento);
        if (objetivo == null) {
            throw new DatoInvalidoException("Debe especificar un objetivo para el suplemento.");
        }
        this.objetivo = objetivo;
    }

    /**
     * Constructor alternativo para carga desde archivo.
     */
    public Suplemento(String nombreComercial, String dosis, String fechaVencimiento, String objetivoStr)
            throws DatoInvalidoException {
        super(nombreComercial, dosis, fechaVencimiento);
        try {
            // Convierte el String de archivo al enum
            this.objetivo = ObjetivoSuplemento.valueOf(objetivoStr.toUpperCase().replace(' ', '_').replace('Á', 'A'));
        } catch (IllegalArgumentException e) {
            throw new DatoInvalidoException("El objetivo del suplemento cargado desde archivo no es válido.");
        }
    }

    // Implementación del método abstracto (Polimorfismo)
    @Override
    public String toFileString() {
        // Prefijo 'S' para identificar el tipo al guardar
        return String.format("S;%s;%s;%s;%s",
                getNombreComercial(),
                getDosis(),
                getFechaVencimientoString(),
                objetivo.name()); // Guardamos el nombre del enum para facilitar la carga
    }

    @Override
    public String toString() {
        String base = super.toString();
        return "Suplemento - " + base + String.format(" (Objetivo: %s)", objetivo.getNombreVisible());
    }

    // --- Getter y Setter específico ---
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