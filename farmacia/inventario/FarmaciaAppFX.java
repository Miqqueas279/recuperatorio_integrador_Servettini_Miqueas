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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FarmaciaAppFX extends Application {

    private InventarioFarmacia inventario;
    private ObservableList<ProductoFarmaceutico> productosObservable;
    private ListView<ProductoFarmaceutico> listaProductos;

    @Override
    public void start(Stage primaryStage) {
        // Inicialización
        inventario = new InventarioFarmacia();
        productosObservable = FXCollections.observableArrayList(inventario.getProductos());
        
        primaryStage.setTitle("FARMACIA - Inventario Don Alberto");
        
        listaProductos = new ListView<>(productosObservable);
        listaProductos.setPrefHeight(250);

        GridPane formPane = crearFormulario();

        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        root.getChildren().addAll(
                new Label("Lista de Productos Farmacéuticos:"),
                listaProductos,
                new Separator(),
                new Label("Agregar / Modificar Producto:"),
                formPane
        );

        Scene scene = new Scene(root, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane crearFormulario() {
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));

        TextField txtNombre = new TextField();
        TextField txtDosis = new TextField();
        TextField txtVencimiento = new TextField();

        grid.add(new Label("Nombre Comercial:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("Dosis:"), 0, 1);
        grid.add(txtDosis, 1, 1);
        grid.add(new Label("Fecha Venc. (DD/MM/AAAA):"), 0, 2);
        grid.add(txtVencimiento, 1, 2);

        ToggleGroup tipoProductoGroup = new ToggleGroup();
        RadioButton rbMedicamento = new RadioButton("Medicamento");
        RadioButton rbSuplemento = new RadioButton("Suplemento");
        rbMedicamento.setToggleGroup(tipoProductoGroup);
        rbSuplemento.setToggleGroup(tipoProductoGroup);
        rbMedicamento.setSelected(true);

        grid.add(new Label("Tipo de Producto:"), 0, 3);
        grid.add(rbMedicamento, 1, 3);
        grid.add(rbSuplemento, 2, 3);

        CheckBox chkReceta = new CheckBox("Requiere Receta");
        VBox vbMedicamento = new VBox(chkReceta);
        vbMedicamento.setVisible(true);

        ComboBox<ObjetivoSuplemento> cmbObjetivo = new ComboBox<>(
                FXCollections.observableArrayList(ObjetivoSuplemento.values()));
        cmbObjetivo.setValue(ObjetivoSuplemento.VITAMINAS);
        VBox vbSuplemento = new VBox(cmbObjetivo);
        vbSuplemento.setVisible(false);

        grid.add(vbMedicamento, 1, 4, 2, 1); 
        grid.add(vbSuplemento, 1, 4, 2, 1); 

        
        tipoProductoGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == rbMedicamento) {
                vbMedicamento.setVisible(true);
                vbSuplemento.setVisible(false);
            } else {
                vbMedicamento.setVisible(false);
                vbSuplemento.setVisible(true);
            }
        });

     
        Button btnAgregar = new Button("Agregar Producto");
        Button btnModificar = new Button("Modificar Seleccionado");
        Button btnEliminar = new Button("Eliminar Seleccionado");

        grid.add(btnAgregar, 0, 5);
        grid.add(btnModificar, 1, 5);
        grid.add(btnEliminar, 2, 5);




        btnAgregar.setOnAction(e -> {
            try {
                ProductoFarmaceutico nuevo = crearProductoDesdeFormulario(
                        rbMedicamento.isSelected(), txtNombre.getText(), txtDosis.getText(),
                        txtVencimiento.getText(), chkReceta.isSelected(), cmbObjetivo.getValue());
                
                inventario.agregarProducto(nuevo);
                productosObservable.setAll(inventario.getProductos());
                limpiarCampos(txtNombre, txtDosis, txtVencimiento, chkReceta);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto agregado correctamente.");

            } catch (DatoInvalidoException ex) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Validación", ex.getMessage());
            }
        });

        btnEliminar.setOnAction(e -> {
            int selectedIndex = listaProductos.getSelectionModel().getSelectedIndex();
            if (selectedIndex != -1) {
                inventario.eliminarProducto(selectedIndex);
                productosObservable.setAll(inventario.getProductos());
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto eliminado correctamente.");
            } else {
                mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Seleccione un producto para eliminar.");
            }
        });

        btnModificar.setOnAction(e -> {
            int selectedIndex = listaProductos.getSelectionModel().getSelectedIndex();
            if (selectedIndex != -1) {
                try {
                    ProductoFarmaceutico modificado = crearProductoDesdeFormulario(
                            rbMedicamento.isSelected(), txtNombre.getText(), txtDosis.getText(),
                            txtVencimiento.getText(), chkReceta.isSelected(), cmbObjetivo.getValue());
                    
                    inventario.modificarProducto(selectedIndex, modificado);
                    productosObservable.setAll(inventario.getProductos()); // Refresca la lista
                    limpiarCampos(txtNombre, txtDosis, txtVencimiento, chkReceta);
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto modificado correctamente.");
                    
                } catch (DatoInvalidoException ex) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error de Validación", ex.getMessage());
                }
            } else {
                mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Seleccione un producto para modificar.");
            }
        });

        listaProductos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cargarDatosEnFormulario(newVal, txtNombre, txtDosis, txtVencimiento, 
                                        rbMedicamento, rbSuplemento, chkReceta, cmbObjetivo);
            }
        });

        return grid;
    }

    private ProductoFarmaceutico crearProductoDesdeFormulario(boolean esMedicamento, String nombre, String dosis,
                                                             String vencimiento, boolean receta,
                                                             ObjetivoSuplemento objetivo)
            throws DatoInvalidoException {
        if (esMedicamento) {
            return new Medicamento(nombre, dosis, vencimiento, receta);
        } else {
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

    private void limpiarCampos(TextField txtNombre, TextField txtDosis, TextField txtVencimiento, CheckBox chkReceta) {
        txtNombre.clear();
        txtDosis.clear();
        txtVencimiento.clear();
        chkReceta.setSelected(false);
    }
    
    private void cargarDatosEnFormulario(ProductoFarmaceutico producto, TextField txtNombre, TextField txtDosis, 
                                         TextField txtVencimiento, RadioButton rbMedicamento, RadioButton rbSuplemento, 
                                         CheckBox chkReceta, ComboBox<ObjetivoSuplemento> cmbObjetivo) {
        
        txtNombre.setText(producto.getNombreComercial());
        txtDosis.setText(producto.getDosis());
        txtVencimiento.setText(producto.getFechaVencimientoString());
        
        if (producto instanceof Medicamento) {
            Medicamento med = (Medicamento) producto;
            rbMedicamento.setSelected(true);
            chkReceta.setSelected(med.isRequiereReceta());
        } else if (producto instanceof Suplemento) {
            Suplemento sup = (Suplemento) producto;
            rbSuplemento.setSelected(true);
            cmbObjetivo.setValue(sup.getObjetivo());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}