
package com.tpi.tfi.enums;

public enum TipoCB {
    
    EAN_13("EAN-13", 13, "European Article Number - 13 dígitos"),
    EAN_8("EAN-8", 8, "European Article Number - 8 dígitos"),
    UPC_A("UPC-A", 12, "Universal Product Code - 12 dígitos");

    private final String nombre;
    private final int longitud;
    private final String descripcion;

    TipoCB(String nombre, int longitud, String descripcion) {
        this.nombre = nombre;
        this.longitud = longitud;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public int getLongitud() {
        return longitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean validarSoloNumeros(String codigo) {
        return codigo != null && codigo.matches("\\d+");
    }

    public boolean validarLongitud(String codigo) {
        return codigo != null && codigo.length() == this.longitud;
    }

    public boolean validarCodigo(String codigo) {
        return validarSoloNumeros(codigo) && validarLongitud(codigo);
    }
}
