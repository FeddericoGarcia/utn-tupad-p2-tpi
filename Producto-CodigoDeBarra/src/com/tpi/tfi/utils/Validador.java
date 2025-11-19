package com.tpi.tfi.utils;

public class Validador {

    public static String validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }

        nombre = nombre.trim().toUpperCase();

        if (nombre.length() < 3) {
            throw new IllegalArgumentException("El nombre debe tener al menos 3 caracteres.");
        }

        return nombre;
    }

    public static double validarPrecio(String precioStr) {
        try {
            double precio = Double.parseDouble(precioStr);
            if (precio <= 0) {
                throw new IllegalArgumentException("El precio debe ser mayor a cero.");
            }
            return precio;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El precio debe ser numérico.");
        }
    }

    public static String validarCodigoBarras(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código de barras no puede estar vacío.");
        }

        codigo = codigo.trim();

        if (!codigo.matches("\\d{13}")) {
            throw new IllegalArgumentException("El código de barras debe contener exactamente 13 dígitos numéricos.");
        }

        return codigo;
    }

}
