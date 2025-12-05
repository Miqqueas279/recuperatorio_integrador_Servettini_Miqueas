/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.farmacia.inventario;
/**
 *
 * @author PC
 */
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FarmaciaAppFX extends Application {

    private InventarioFarmacia inventario;
    private ObservableList<ProductoFarmaceutico> listaObs;
    private ListView<ProductoFarmaceutico> listaView; 
    private TextField txtNombre = new TextField();
    private TextField txtDosis = new TextField();
    private TextField txtVenc = new TextField();

    private RadioButton rbMed = new RadioButton("Medicamento");
    private RadioButton rbSup = new RadioButton("Suplemento");
    private CheckBox chkReceta = new CheckBox("Requiere Receta");
    private ComboBox<ObjetivoSuplemento> cmbObj;

    @Override
    public void start(Stage primaryStage) {

        inventario = new InventarioFarmacia();
        listaObs = FXCollections.observableArrayList(inventario.getProductos());
        
        primaryStage.setTitle("Inventario Farmacia Don Alberto");

        listaView = new ListView<>(listaObs);
        listaView.setPrefHeight(200);

        VBox formBox = new VBox(8);
        formBox.setPadding(new Insets(10));

        formBox.getChildren().add(crearFila("Nombre Comercial:", txtNombre));
        formBox.getChildren().add(crearFila("Dosis (ej: 600 mg):", txtDosis));
        formBox.getChildren().add(crearFila("Vencimiento (DD/MM/AAAA):", txtVenc));

        ToggleGroup grupoTipo = new ToggleGroup();
        rbMed.setToggleGroup(grupoTipo);
        rbSup.setToggleGroup(grupoTipo);
        rbMed.setSelected(true);
        
        HBox hbTipo = new HBox(15, new Label("Tipo de Producto:"), rbMed, rbSup);
        hbTipo.setAlignment(Pos.CENTER_LEFT);
        formBox.getChildren().add(hbTipo);

        VBox vbMed = new VBox(chkReceta);

        cmbObj = new ComboBox<>(FXCollections.observableArrayList(ObjetivoSuplemento.values()));
        cmbObj.setValue(ObjetivoSuplemento.VITAMINAS);
        HBox hbSup = new HBox(5, new Label("Objetivo:"), cmbObj);
        hbSup.setAlignment(Pos.CENTER_LEFT);

        formBox.getChildren().addAll(vbMed, hbSup);

        vbMed.setVisible(true);
        hbSup.setVisible(false);
        
        grupoTipo.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            boolean esMed = newVal == rbMed;
            vbMed.setVisible(esMed);
            hbSup.setVisible(!esMed);
        });

        Button btnAdd = new Button("Agregar Producto");
        Button btnMod = new Button("Modificar Seleccionado");
        Button btnDel = new Button("Eliminar Producto");
        Button btnLimpiar = new Button("Borrar Formulario"); // Nuevo botón
        
        HBox hbBtns = new HBox(10, btnAdd, btnMod, btnDel, btnLimpiar); // Añadido el botón aquí
        hbBtns.setAlignment(Pos.CENTER);

        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        root.getChildren().addAll(
                new Label("** Inventario Actual de Don Alberto **"),
                listaView,
                new Separator(),
                new Label("** Ingreso y Modificación **"),
                formBox,
                new Separator(),
                hbBtns
        );

        conectarEventos(btnAdd, btnMod, btnDel, btnLimpiar);
        conectarSeleccionLista();

        Scene scene = new Scene(root, 500, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox crearFila(String etiqueta, TextField campo) {
        Label lbl = new Label(etiqueta);
        lbl.setPrefWidth(150);
        HBox hbox = new HBox(5, lbl, campo);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    private void conectarEventos(Button btnAdd, Button btnMod, Button btnDel, Button btnLimpiar) {

        btnAdd.setOnAction(e -> {
            try {
                ProductoFarmaceutico nuevo = crearProductoDesdeFormulario();
                inventario.agregarProducto(nuevo); 
                refrescarListaYMostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto agregado.");
            } catch (DatoInvalidoException ex) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Validación", ex.getMessage());
            }
        });

        btnMod.setOnAction(e -> {
            int indice = listaView.getSelectionModel().getSelectedIndex();
            if (indice != -1) {
                try {
                    ProductoFarmaceutico modificado = crearProductoDesdeFormulario(); 
                    inventario.modificarProducto(indice, modificado);
                    refrescarListaYMostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto modificado.");
                } catch (DatoInvalidoException ex) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error de Validación", ex.getMessage());
                }
            } else {
                mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Selecciona un producto para modificar.");
            }
        });

        btnDel.setOnAction(e -> {
            int indice = listaView.getSelectionModel().getSelectedIndex();
            if (indice != -1) {
                inventario.eliminarProducto(indice);
                refrescarListaYMostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto eliminado.");
            } else {
                mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Selecciona un producto para eliminar.");
            }
        });

        btnLimpiar.setOnAction(e -> limpiarCampos());
    }

    private void conectarSeleccionLista() {
        listaView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {

                txtNombre.setText(newVal.getNombreComercial());
                txtDosis.setText(newVal.getDosis());
                txtVenc.setText(newVal.getFechaVencimientoString());

                if (newVal instanceof Medicamento) {
                    rbMed.setSelected(true);
                    Medicamento med = (Medicamento) newVal;
                    chkReceta.setSelected(med.isRequiereReceta());
                } else if (newVal instanceof Suplemento) {
                    rbSup.setSelected(true);
                    Suplemento sup = (Suplemento) newVal;
                    cmbObj.setValue(sup.getObjetivo());
                }
            }
        });
    }

    private ProductoFarmaceutico crearProductoDesdeFormulario()
            throws DatoInvalidoException {
        
        String nombre = txtNombre.getText();
        String dosis = txtDosis.getText();
        String vencimiento = txtVenc.getText();
        
        if (rbMed.isSelected()) {
            boolean receta = chkReceta.isSelected();
            return new Medicamento(nombre, dosis, vencimiento, receta);
        } else {

            ObjetivoSuplemento objetivo = cmbObj.getValue();
            if (objetivo == null) { 
                throw new DatoInvalidoException("¡Falta seleccionar el Objetivo del Suplemento!");
            }
            return new Suplemento(nombre, dosis, vencimiento, objetivo);
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtDosis.clear();
        txtVenc.clear(); 
        chkReceta.setSelected(false);
        rbMed.setSelected(true); 
        cmbObj.setValue(ObjetivoSuplemento.VITAMINAS);
    }

    private void refrescarListaYMostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {

        listaObs.setAll(inventario.getProductos());

        limpiarCampos();
        
        mostrarAlerta(tipo, titulo, mensaje);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
