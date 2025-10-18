package com.tpi.tfi.entities;

public class Producto {

    private Long id;
    private boolean eliminado;
    private String nombre;
    private String marca;
    private String categoria;
    private double precio;
    private Double peso;
    private CodigoBarras codigoBarras; // Relación 1→1 unidireccional

    public Producto() {}

    public Producto(String nombre, String marca, String categoria, double precio, Double peso) {
        this.nombre = nombre;
        this.marca = marca;
        this.categoria = categoria;
        this.precio = precio;
        this.peso = peso;
        this.eliminado = false;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public CodigoBarras getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(CodigoBarras codigoBarras) { this.codigoBarras = codigoBarras; }

    @Override
    public String toString() {
        return String.format(
                "[ID:%d] %s (%s) - $%.2f - Cat:%s - Peso:%.2f %s",
                id, nombre, marca, precio, categoria, (peso != null ? peso : 0.0),
                (eliminado ? "(ELIMINADO)" : "")
        );
    }
}
