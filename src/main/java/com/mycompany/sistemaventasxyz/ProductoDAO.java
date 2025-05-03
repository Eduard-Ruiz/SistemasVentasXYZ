/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaventasxyz;

import com.mycompany.sistemaventasxyz.modelo.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author RuizX
 */


public class ProductoDAO {

    // Registrar un nuevo producto
    public void registrarProducto(Producto p) {
        String sql = "INSERT INTO productos(nombre, descripcion, precio, cantidad) VALUES (?, ?, ?, ?)";
        Connection con = null;

        try {
            con = ConexionBD.conectar();
            con.setAutoCommit(false); // Iniciar transacción

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, p.getNombre());
                ps.setString(2, p.getDescripcion());
                ps.setDouble(3, p.getPrecio());
                ps.setInt(4, p.getCantidad());

                ps.executeUpdate();
                con.commit(); // Confirmar transacción

                System.out.println("✅ Producto registrado con éxito.");

            } catch (SQLException e) {
                if (con != null) {
                    con.rollback(); // Revertir cambios
                    System.out.println("❌ Transacción revertida.");
                }
                System.err.println("Error al registrar el producto: " + e.getMessage());
            }

        } catch (SQLException ex) {
            System.err.println("Error al establecer conexión: " + ex.getMessage());
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException ignored) {}
            }
        }
    }

    // Listar todos los productos
    public List<Producto> listarProductos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPrecio(rs.getDouble("precio"));
                p.setCantidad(rs.getInt("cantidad"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }

    // Actualizar stock - versión que acepta una conexión externa
    public void actualizarStock(Connection con, int idProducto, int cantidadVendida) throws SQLException {
        String sql = "UPDATE productos SET cantidad = cantidad - ? WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, cantidadVendida);
            pstmt.setInt(2, idProducto);
            pstmt.executeUpdate();
        }
    }
}




