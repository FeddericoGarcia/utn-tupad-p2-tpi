package com.tpi.tfi.service;

import com.tpi.tfi.config.DatabaseConnection;
import com.tpi.tfi.dao.CodigoBarrasDAO;
import com.tpi.tfi.dao.ProductoDAO;
import com.tpi.tfi.entities.CodigoBarras;
import com.tpi.tfi.entities.Producto;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProductoService {

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final CodigoBarrasDAO cbDAO = new CodigoBarrasDAO();
    private final Scanner sc = new Scanner(System.in);

    // Operación transaccional: crear producto y asociar codigo de barras (en la misma transacción)
    public void crearProductoConCodigoBarras() {
        System.out.println("\n--- Crear Producto con Código de Barras (Transaccional) ---");

        // Datos del producto
        String nombre = leerString("Nombre: ");
        String marca = leerString("Marca: ");
        String categoria = leerString("Categoría: ");
        double precio = leerDouble("Precio: ");
        Double peso = leerNullableDouble("Peso (ENTER para null): ");

        // Datos del código de barras
        String tipo = leerString("Tipo (EAN13/EAN8/UPC): ").toUpperCase();
        String valor = leerString("Valor del código: ");

        Producto p = new Producto(nombre, marca, categoria, precio, peso);
        CodigoBarras cb = new CodigoBarras(tipo, valor, java.time.LocalDate.now(), null);

        try (Connection conn = DatabaseConnection.getConnection()) {
            try {
                conn.setAutoCommit(false);
                // 1) crear producto
                Optional<Long> idProdOpt = productoDAO.crear(p, conn);
                if (idProdOpt.isEmpty()) throw new RuntimeException("No se pudo crear producto.");
                Long idProd = idProdOpt.get();
                p.setId(idProd);

                // 2) crear codigo de barras asignando producto_id
                Optional<Long> idCbOpt = cbDAO.crear(cb, conn, idProd);
                if (idCbOpt.isEmpty()) throw new RuntimeException("No se pudo crear código de barras.");
                Long idCb = idCbOpt.get();
                cb.setId(idCb);

                // commit
                conn.commit();
                System.out.println("✅ Producto y Código de Barras creados correctamente. IDs: Producto=" + idProd + " CB=" + idCb);
            } catch (Exception e) {
                conn.rollback();
                System.out.println("❌ Ocurrió un error. Se hizo rollback. " + e.getMessage());
                e.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception ex) {
            System.out.println("❌ Error de conexión: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void listarProductos() {
        List<Producto> lista = productoDAO.obtenerTodos();
        System.out.println("\n--- Productos ---");
        if (lista.isEmpty()) System.out.println("(sin registros)");
        else {
            for (Producto p : lista) {
                System.out.println(p);
                if (p.getCodigoBarras() != null) System.out.println("   -> " + p.getCodigoBarras());
            }
        }
    }

    public void buscarPorId() {
        Long id = leerLong("ID producto: ");
        Optional<Producto> opt = productoDAO.obtenerPorId(id);
        opt.ifPresentOrElse(
                p -> {
                    System.out.println(p);
                    if (p.getCodigoBarras() != null);
                },
                () -> System.out.println("No encontrado.")
        );
    }

    public void actualizar() {
        Long id = leerLong("ID a actualizar: ");
        var opt = productoDAO.obtenerPorId(id);
        if (opt.isEmpty()) {
            System.out.println("No existe.");
            return;
        }
        Producto p = opt.get();
        System.out.println("Dejar vacío para mantener valor actual.");
        String nombre = leerStringDefault("Nombre (" + p.getNombre() + "): ", p.getNombre());
        String marca = leerStringDefault("Marca (" + p.getMarca() + "): ", p.getMarca());
        String categoria = leerStringDefault("Categoría (" + p.getCategoria() + "): ", p.getCategoria());
        Double precio = leerDoubleDefault("Precio (" + p.getPrecio() + "): ", p.getPrecio());
        Double peso = leerNullableDoubleDefault("Peso (" + p.getPeso() + "): ", p.getPeso());

        p.setNombre(nombre);
        p.setMarca(marca);
        p.setCategoria(categoria);
        p.setPrecio(precio);
        p.setPeso(peso);

        boolean ok = productoDAO.actualizar(p);
        System.out.println(ok ? "✅ Actualizado." : "❌ Error al actualizar.");
    }

    public void eliminarLogico() {
        Long id = leerLong("ID a eliminar (baja lógica): ");
        boolean ok = productoDAO.eliminarLogico(id);
        System.out.println(ok ? "🗑️ Eliminado lógicamente." : "❌ No se pudo eliminar.");
    }

    // ---------- helpers de lectura ----------
    private String leerString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private String leerStringDefault(String prompt, String def) {
        System.out.print(prompt);
        String s = sc.nextLine().trim();
        return s.isEmpty() ? def : s;
    }

    private double leerDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Número inválido.");
            }
        }
    }

    private Double leerNullableDouble(String prompt) {
        System.out.print(prompt);
        String s = sc.nextLine().trim();
        if (s.isEmpty()) return null;
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            System.out.println("Entrada inválida. Se deja NULL.");
            return null;
        }
    }

    private Double leerDoubleDefault(String prompt, Double def) {
        System.out.print(prompt);
        String s = sc.nextLine().trim();
        if (s.isEmpty()) return def;
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            System.out.println("Entrada inválida. Usando valor por defecto.");
            return def;
        }
    }

    private Double leerNullableDoubleDefault(String prompt, Double def) {
        System.out.print(prompt);
        String s = sc.nextLine().trim();
        if (s.isEmpty()) return def;
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            System.out.println("Entrada inválida. Usando valor actual.");
            return def;
        }
    }

    private Long leerLong(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Long.parseLong(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Entrada inválida.");
            }
        }
    }
}
