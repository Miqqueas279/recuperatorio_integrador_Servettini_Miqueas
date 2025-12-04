/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.farmacia.inventario;

/**
 *
 * @author PC
 */
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public abstract class ProductoFarmaceutico {
    // Atributos comunes (Encapsulamiento)
    private String nombreComercial;
    private String dosis;
    private LocalDate fechaVencimiento;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Constructor
    public ProductoFarmaceutico(String nombreComercial, String dosis, String fechaVencimiento)
            throws DatoInvalidoException {
        // Validación en el constructor
        if (nombreComercial == null || nombreComercial.trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre comercial no puede estar vacío.");
        }
        if (dosis == null || dosis.trim().isEmpty()) {
            throw new DatoInvalidoException("La dosis no puede estar vacía.");
        }
        
        this.nombreComercial = nombreComercial.trim();
        this.dosis = dosis.trim();
        this.setFechaVencimiento(fechaVencimiento); // Llama al setter con validación
    }

    // Método abstracto (Polimorfismo) para obtener la representación en archivo
    public abstract String toFileString();

    // Método para obtener los datos básicos en formato de cadena
    @Override
    public String toString() {
        return String.format("%s - Dosis: %s - Vencimiento: %s",
                nombreComercial, dosis, getFechaVencimientoString());
    }

    // --- Getters y Setters ---

    public String getNombreComercial() {
        return nombreComercial;
    }

    // No se necesita un setter para nombreComercial si no se puede modificar
    // después de la creación, pero se agrega por si se quiere permitir la
    // modificación a futuro (Principio de Abierto/Cerrado - OCP)

    public String getDosis() {
        return dosis;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public String getFechaVencimientoString() {
        if (fechaVencimiento == null) {
            return "";
        }
        return fechaVencimiento.format(FORMATTER);
    }

    public void setFechaVencimiento(String fechaVencimientoStr) throws DatoInvalidoException {
        if (fechaVencimientoStr == null || fechaVencimientoStr.trim().isEmpty()) {
            throw new DatoInvalidoException("La fecha de vencimiento no puede estar vacía.");
        }
        try {
            LocalDate fecha = LocalDate.parse(fechaVencimientoStr.trim(), FORMATTER);
            if (fecha.isBefore(LocalDate.now())) {
                throw new DatoInvalidoException("La fecha de vencimiento no puede ser anterior a la fecha actual.");
            }
            this.fechaVencimiento = fecha;
        } catch (DateTimeParseException e) {
            throw new DatoInvalidoException(
                    "Formato de fecha de vencimiento inválido. Use el formato DD/MM/AAAA.");
        }
    }
}