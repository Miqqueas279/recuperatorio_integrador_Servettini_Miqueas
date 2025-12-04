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

    public String getNombreVisible() {
        return nombreVisible;
    }

    @Override
    public String toString() {
        return nombreVisible;
    }
}
