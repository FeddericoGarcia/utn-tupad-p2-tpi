package com.tpi.productos.service;

import com.tpi.productos.dao.ProductoDAO;
import com.tpi.productos.model.Producto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProductoService {

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final Scanner sc = new Scanner(System.in);

    public void crearProducto() {
        System.out.println("\n--- Crear Producto ---");
        String nombre = leerString("Nombre: ");
        String descripcion = leerString("Descripción: ");
        BigDecimal precio = leerBigDecimal("Precio: ");
        int stock = leerInt("Stock: ");
        String codigo = leerString("Código de Barra: ");
        Integer idCategoria = leerNullableInt("ID categoría (ENTER para null): ");

        Producto p = new Producto(nombre, descripcion, precio, stock, codigo, idCategoria);
        productoDAO.crearProducto(p)
                .ifPresentOrElse(
                        id -> System.out.println("✅ Producto creado con ID: " + id),
                        () -> System.out.println("❌ Error al crear producto.")
                );
    }

    public void listarProductos() {
        List<Producto> lista = productoDAO.obtenerTodos();
        System.out.println("\n--- Listado de Productos ---");
        if (lista.isEmpty()) {
            System.out.println("(sin registros)");
            return;
        }
        lista.forEach(System.out::println);
    }

    public void buscarPorId() {
        int id = leerInt("ID producto: ");
        Optional<Producto> p = productoDAO.obtenerPorId(id);
        p.ifPresentOrElse(System.out::println, () -> System.out.println("No encontrado."));
    }

    public void buscarPorCodigo() {
        String codigo = leerString("Código de barra: ");
        Optional<Producto> p = productoDAO.obtenerPorCodigoBarra(codigo);
        p.ifPresentOrElse(System.out::println, () -> System.out.println("No encontrado."));
    }

    public void actualizarProducto() {
        int id = leerInt("ID a actualizar: ");
        Optional<Producto> opt = productoDAO.obtenerPorId(id);
        if (opt.isEmpty()) {
            System.out.println("Producto no existe.");
            return;
        }
        Producto p = opt.get();
        System.out.println("Dejar vacío para mantener valor actual.");

        String nombre = leerStringDefault("Nombre (" + p.getNombre() + "): ", p.getNombre());
        String descripcion = leerStringDefault("Descripción (" + p.getDescripcion() + "): ", p.getDescripcion());
        BigDecimal precio = leerBigDecimalDefault("Precio (" + p.getPrecio() + "): ", p.getPrecio());
        int stock = leerIntDefault("Stock (" + p.getStock() + "): ", p.getStock());
        String codigo = leerStringDefault("Código de Barra (" + p.getCodigoBarra() + "): ", p.getCodigoBarra());
        Integer idCategoria = leerNullableIntDefault("ID categoría (" + p.getIdCategoria() + "): ", p.getIdCategoria());

        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setPrecio(precio);
        p.setStock(stock);
        p.setCodigoBarra(codigo);
        p.setIdCategoria(idCategoria);

        boolean ok = productoDAO.actualizarProducto(p);
        System.out.println(ok ? "✅ Actualizado correctamente." : "❌ Error al actualizar.");
    }

    public void eliminarProducto() {
        int id = leerInt("ID a eliminar: ");
        boolean ok = productoDAO.eliminarProducto(id);
        System.out.println(ok ? "🗑️ Eliminado correctamente." : "❌ No se pudo eliminar (quizá no existe).");
    }

    public void decrementarStock() {
        int id = leerInt("ID producto: ");
        int cantidad = leerInt("Cantidad a decrementar: ");
        boolean ok = productoDAO.decrementarStockConTransaccion(id, cantidad);
        System.out.println(ok ? "🔁 Stock actualizado correctamente." : "❌ No se pudo decrementar (stock insuficiente o error).");
    }

    // Métodos auxiliares para lectura y validación
    private String leerString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private String leerStringDefault(String prompt, String def) {
        System.out.print(prompt);
        String s = sc.nextLine().trim();
        return s.isEmpty() ? def : s;
    }

    private int leerInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String s = sc.nextLine().trim();
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Número inválido. Intentá de nuevo.");
            }
        }
    }

    private int leerIntDefault(String prompt, int def) {
        System.out.print(prompt);
        String s = sc.nextLine().trim();
        if (s.isEmpty()) return def;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Usando valor por defecto.");
            return def;
        }
    }

    private BigDecimal leerBigDecimal(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String s = sc.nextLine().trim();
                return new BigDecimal(s);
            } catch (Exception e) {
                System.out.println("Valor decimal inválido. Intentá de nuevo (ej: 123.45).");
            }
        }
    }

    private BigDecimal leerBigDecimalDefault(String prompt, BigDecimal def) {
        System.out.print(prompt);
        String s = sc.nextLine().trim();
        if (s.isEmpty()) return def;
        try {
            return new BigDecimal(s);
        } catch (Exception e) {
            System.out.println("Entrada inválida. Usando valor por defecto.");
            return def;
        }
    }

    private Integer leerNullableInt(String prompt) {
        System.out.print(prompt);
        String s = sc.nextLine().trim();
        if (s.isEmpty()) return null;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Se dejará como NULL.");
            return null;
        }
    }

    private Integer leerNullableIntDefault(String prompt, Integer def) {
        System.out.print(prompt);
        String s = sc.nextLine().trim();
        if (s.isEmpty()) return def;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Se usa valor actual.");
            return def;
        }
    }
}
