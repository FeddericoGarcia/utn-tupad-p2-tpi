package com.tpi.tfi.utils;

import com.tpi.tfi.enums.TipoCB;

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

    public static boolean esCodigoValidoSegunTipo(String codigo, TipoCB tipo) {
        if (codigo == null || tipo == null) return false;
        return tipo.validarCodigo(codigo);
    }

    public static void validarCodigoOBLIGATORIO(String codigo, TipoCB tipo) {
        if (!esCodigoValidoSegunTipo(codigo, tipo)) {
            throw new IllegalArgumentException(
                "El código no es válido para el tipo " + tipo.getNombre() +
                ". Debe tener " + tipo.getLongitud() + " dígitos numéricos."
            );
        }
    }

}
