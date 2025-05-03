/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaventasxyz.modelo;

/**
 *
 * @author RuizX
 */


public class Venta {
    private int id;
    private int idProducto;
    private int cantidad;
    private double precio;
    private double total;
    private String fecha;

    // Constructor vacío
    public Venta() {}

    // Constructor completo
    public Venta(int id, int idProducto, int cantidad, double precio, double total) {
        this.id = id;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.total = total;
        this.fecha = "CURDATE()";
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    @Override
    public String toString() {
        return "Venta{" +
                "id=" + id +
                ", idProducto=" + idProducto +
                ", cantidad=" + cantidad +
                ", precio=" + precio +
                ", total=" + total +
                ", fecha='" + fecha + '\'' +
                '}';
    }
}