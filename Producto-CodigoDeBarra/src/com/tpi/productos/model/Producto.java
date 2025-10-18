package com.tpi.productos.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Producto {
    private Integer idProducto;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int stock;
    private String codigoBarra;
    private Integer idCategoria; // FK

    public Producto() {}

    // Constructor sin id (para inserción)
    public Producto(String nombre, String descripcion, BigDecimal precio, 
            int stock, String codigoBarra, Integer idCategoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.codigoBarra = codigoBarra;
        this.idCategoria = idCategoria;
    }

    public Producto(Integer idProducto, String nombre, String descripcion, 
            BigDecimal precio, int stock, String codigoBarra, Integer idCategoria) {
        this(nombre, descripcion, precio, stock, codigoBarra, idCategoria);
        this.idProducto = idProducto;
    }

    // getters/setters
    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getCodigoBarra() { return codigoBarra; }
    public void setCodigoBarra(String codigoBarra) { this.codigoBarra = codigoBarra; }

    public Integer getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }

    @Override
    public String toString() {
        return "Producto{" +
                "idProducto=" + idProducto +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", stock=" + stock +
                ", codigoBarra='" + codigoBarra + '\'' +
                ", idCategoria=" + idCategoria +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Producto producto = (Producto) o;
        return Objects.equals(codigoBarra, producto.codigoBarra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoBarra);
    }
}
