/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaventasxyz;
import com.mycompany.sistemaventasxyz.modelo.Producto;
import com.mycompany.sistemaventasxyz.modelo.Venta;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
/**
 *
 * @author RuizX
 */

public class VentanaPrincipal extends JFrame {

    private JTable tablaProductos;
    private JTextField txtCantidad;
    private JButton btnRegistrarVenta;

    public VentanaPrincipal() {
        initComponents();
        cargarProductos();  // Cargar productos al iniciar
    }

    private void initComponents() {
        // Configuración básica de la ventana
        setTitle("Sistema de Gestión de Ventas - XYZ");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel superior para formulario
        JPanel panelFormulario = new JPanel(new FlowLayout());
        txtCantidad = new JTextField(10); // Campo para cantidad
        btnRegistrarVenta = new JButton("Registrar Venta");

        // Etiqueta informativa
        panelFormulario.add(new JLabel("Cantidad:"));
        panelFormulario.add(txtCantidad);
        panelFormulario.add(btnRegistrarVenta);

        // Tabla de productos
        tablaProductos = new JTable();
        JScrollPane scrollPane = new JScrollPane(tablaProductos);

        // Agregar componentes a la ventana
        add(panelFormulario, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Acción del botón
        btnRegistrarVenta.addActionListener(this::btnRegistrarVentaActionPerformed);
    }

    private void btnRegistrarVentaActionPerformed(ActionEvent evt) {
        registrarVenta();
    }

    private void cargarProductos() {
        ProductoDAO dao = new ProductoDAO();
        List<Producto> lista = dao.listarProductos();

        DefaultTableModel modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new Object[]{"ID", "Nombre", "Descripción", "Precio", "Cantidad"});

        for (Producto p : lista) {
            modelo.addRow(new Object[]{
                p.getId(), p.getNombre(), p.getDescripcion(), p.getPrecio(), p.getCantidad()
            });
        }

        tablaProductos.setModel(modelo);
    }

    private void registrarVenta() {
        try {
            int filaSeleccionada = tablaProductos.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un producto.");
                return;
            }

            int idProducto = (int) tablaProductos.getValueAt(filaSeleccionada, 0);
            String cantidadStr = txtCantidad.getText().trim();

            if (cantidadStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese una cantidad válida.");
                return;
            }

            int cantidad = Integer.parseInt(cantidadStr);
            double precio = Double.parseDouble(tablaProductos.getValueAt(filaSeleccionada, 3).toString());
            int stockDisponible = (int) tablaProductos.getValueAt(filaSeleccionada, 4);

            // Validaciones
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.");
                return;
            }

            if (cantidad > stockDisponible) {
                JOptionPane.showMessageDialog(this,
                        "Stock insuficiente. Solo hay " + stockDisponible + " unidades disponibles.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Calcular total
            double total = precio * cantidad;

            // Mostrar mensaje de confirmación
            int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está seguro de vender " + cantidad + " unidad(es) de este producto?\n" +
                            "Precio unitario: $" + precio + "\n" +
                            "Total: $" + total,
                    "Confirmar Venta",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                // Registrar venta
                Venta venta = new Venta();
                venta.setIdProducto(idProducto);
                venta.setCantidad(cantidad);
                venta.setPrecio(precio);
                venta.setTotal(total);

                VentaDAO ventaDAO = new VentaDAO();
                ventaDAO.registrarVenta(venta);

                JOptionPane.showMessageDialog(this, "Venta registrada correctamente.");
                cargarProductos(); // Actualizar tabla
                txtCantidad.setText(""); // Limpiar campo
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: Ingrese una cantidad numérica válida.");
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar la venta: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}