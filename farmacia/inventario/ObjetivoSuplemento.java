/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.farmacia.inventario;

/**
 *
 * @author PC
 */
public enum ObjetivoSuplemento {
    VITAMINAS("Vitaminas"),
    DEPORTIVO("Deportivo"),
    PROTEINAS("Proteínas"),
    ACIDOS_GRASOS("Ácidos grasos");

    private final String nombreVisible;

    ObjetivoSuplemento(String nombreVisible) {
        this.nombreVisible = nombreVisible;
    }

    /**
     * Devuelve el nombre amigable para la interfaz.
     * @return El nombre visible del objetivo.
     */
    public String getNombreVisible() {
        return nombreVisible;
    }

    /**
     * Sobrescribe toString para usar el nombre visible.
     */
    @Override
    public String toString() {
        return nombreVisible;
    }
}