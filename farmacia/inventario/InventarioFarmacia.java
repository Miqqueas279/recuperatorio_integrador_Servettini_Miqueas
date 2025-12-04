/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.farmacia.inventario;

/**
 *
 * @author PC
 */
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InventarioFarmacia {

    private final List<ProductoFarmaceutico> productos;
    private static final String NOMBRE_ARCHIVO = "inventario_don_alberto.txt";

    public InventarioFarmacia() {
        this.productos = new ArrayList<>();

        cargarDesdeArchivo();
    }

    public void agregarProducto(ProductoFarmaceutico producto) {
        productos.add(producto);
        guardarEnArchivo();
    }

    public boolean eliminarProducto(int indice) {
        if (indice >= 0 && indice < productos.size()) {
            productos.remove(indice);
            guardarEnArchivo();
            return true;
        }
        return false;
    }

    public boolean modificarProducto(int indice, ProductoFarmaceutico productoNuevo) {
        if (indice >= 0 && indice < productos.size()) {
            productos.set(indice, productoNuevo);
            guardarEnArchivo();
            return true;
        }
        return false;
    }

    public List<ProductoFarmaceutico> getProductos() {
        return productos;
    }

    public void guardarEnArchivo() {
        try (FileWriter fw = new FileWriter(NOMBRE_ARCHIVO);
             BufferedWriter bw = new BufferedWriter(fw)) {
            
            for (ProductoFarmaceutico producto : productos) {
                bw.write(producto.toFileString());
                bw.newLine();
            }
            System.out.println("Inventario guardado exitosamente en " + NOMBRE_ARCHIVO);

        } catch (IOException e) {
            System.err.println("Error al guardar el inventario: " + e.getMessage());
        }
    }

    private void cargarDesdeArchivo() {
        File archivo = new File(NOMBRE_ARCHIVO);
        if (!archivo.exists()) {
            System.out.println("Archivo de inventario no encontrado. Iniciando con inventario vacío.");
            return;
        }

        try (FileReader fr = new FileReader(archivo);
             BufferedReader br = new BufferedReader(fr)) {

            String linea;
            while ((linea = br.readLine()) != null) {
                ProductoFarmaceutico producto = parsearProducto(linea);
                if (producto != null) {
                    productos.add(producto);
                }
            }
            System.out.println("Inventario cargado exitosamente desde " + NOMBRE_ARCHIVO);

        } catch (IOException e) {
            System.err.println("Error de lectura/escritura al cargar el inventario: " + e.getMessage());
        }
    }

    private ProductoFarmaceutico parsearProducto(String linea) {

        String[] partes = linea.split(";");

        if (partes.length < 4) return null;

        String tipo = partes[0];
        String nombre = partes[1];
        String dosis = partes[2];
        String vencimiento = partes[3];

        try {
            if (tipo.equals("M") && partes.length == 5) {

                String requiereRecetaStr = partes[4];
                return new Medicamento(nombre, dosis, vencimiento, requiereRecetaStr);
            } else if (tipo.equals("S") && partes.length == 5) {

                String objetivoStr = partes[4];
                return new Suplemento(nombre, dosis, vencimiento, objetivoStr);
            }
        } catch (DatoInvalidoException e) {
            System.err.println("Error de validación al cargar producto: " + e.getMessage() + " (Línea: " + linea + ")");
        }
        return null;
    }
}