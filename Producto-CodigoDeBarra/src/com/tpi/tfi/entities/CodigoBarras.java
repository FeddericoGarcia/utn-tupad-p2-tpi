package com.tpi.tfi.entities;

import java.time.LocalDate;

public class CodigoBarras {

    private Long id;
    private boolean eliminado;
    private String tipo; // EAN13, EAN8, UPC
    private String valor;
    private LocalDate fechaAsignacion;
    private String observaciones;

    public CodigoBarras() {}

    public CodigoBarras(String tipo, String valor, LocalDate fechaAsignacion, String observaciones) {
        this.tipo = tipo;
        this.valor = valor;
        this.fechaAsignacion = fechaAsignacion;
        this.observaciones = observaciones;
        this.eliminado = false;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }

    public LocalDate getFechaAsignacion() { return fechaAsignacion; }
    public void setFechaAsignacion(LocalDate fechaAsignacion) { this.fechaAsignacion = fechaAsignacion; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    @Override
    public String toString() {
        return String.format("[ID:%d] %s - %s (%s) %s", id, tipo, valor, fechaAsignacion,
                (eliminado ? "(ELIMINADO)" : ""));
    }
}
