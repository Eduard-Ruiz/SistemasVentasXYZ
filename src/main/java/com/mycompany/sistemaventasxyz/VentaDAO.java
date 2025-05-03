/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaventasxyz;
import com.mycompany.sistemaventasxyz.modelo.Venta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author RuizX
 */

public class VentaDAO {

    public void registrarVenta(Venta venta) {
        String sqlVenta = "INSERT INTO ventas(id_producto, cantidad, precio, total) VALUES (?, ?, ?, ?)";
        Connection con = null;

        try {
            con = ConexionBD.conectar();
            con.setAutoCommit(false); // Iniciar transacción

            try (PreparedStatement psVenta = con.prepareStatement(sqlVenta)) {
                psVenta.setInt(1, venta.getIdProducto());
                psVenta.setInt(2, venta.getCantidad());
                psVenta.setDouble(3, venta.getPrecio());
                psVenta.setDouble(4, venta.getTotal());

                psVenta.executeUpdate();

                // Actualizar stock del producto
                ProductoDAO productoDAO = new ProductoDAO();
                productoDAO.actualizarStock(con, venta.getIdProducto(), venta.getCantidad());

                con.commit(); // Confirmar transacción
                System.out.println("✅ Venta registrada con éxito.");

            } catch (SQLException e) {
                con.rollback(); // Revertir cambios si hay error
                System.err.println("❌ Error al registrar la venta: " + e.getMessage());
            }

        } catch (SQLException ex) {
            System.err.println("❌ Error al conectar a la base de datos: " + ex.getMessage());
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException ignored) {}
            }
        }
    }
}