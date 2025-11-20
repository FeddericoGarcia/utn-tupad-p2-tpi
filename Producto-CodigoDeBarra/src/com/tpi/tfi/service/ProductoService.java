package com.tpi.tfi.service;

import com.tpi.tfi.config.DatabaseConnection;
import com.tpi.tfi.dao.CodigoBarrasDAO;
import com.tpi.tfi.dao.ProductoDAO;
import com.tpi.tfi.entities.CodigoBarras;
import com.tpi.tfi.entities.Producto;
import com.tpi.tfi.enums.TipoCB;
import com.tpi.tfi.exceptions.DataAccessException;
import com.tpi.tfi.utils.Validador;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

public class ProductoService {

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final CodigoBarrasDAO cbDAO = new CodigoBarrasDAO();
    private final Scanner sc = new Scanner(System.in);

    // Operación transaccional: crear producto y asociar codigo de barras (en la misma transacción)
    public void crearProductoConCodigoBarras() throws SQLException {
        System.out.println("\n--- Crear Producto con Código de Barras (Transaccional) ---");

        // Datos del producto
        String nombre = leerString("Nombre: ");
        String marca = leerString("Marca: ");
        String categoria = leerString("Categoría: ");
        double precio = leerDouble("Precio: ");
        Double peso = leerNullableDouble("Peso (ENTER para null): ");

        // Datos del código de barras
        System.out.println("Seleccione tipo de código:");
        System.out.println("1) EAN-13");
        System.out.println("2) EAN-8");
        System.out.println("3) UPC-A");
        String opt = leerString("\n:").trim();
        TipoCB tipo;
        switch(opt) {
            case "1": tipo = TipoCB.EAN_13; break;
            case "2": tipo = TipoCB.EAN_8; break;
            case "3": tipo = TipoCB.UPC_A; break;
            default: throw new IllegalArgumentException("Tipo inválido");
        }
        String valor = leerString("Valor del código: ");
        Validador.validarCodigoOBLIGATORIO(valor, tipo);

        Producto p = new Producto(nombre, marca, categoria, precio, peso);
        CodigoBarras cb = new CodigoBarras(tipo, valor, java.time.LocalDate.now(), null);

        try (Connection conn = DatabaseConnection.getConnection()) {
            try {
                conn.setAutoCommit(false);
                
                if (cbDAO.existeCodigo(conn, valor)) throw new IllegalArgumentException("Código duplicado.");
                // operaciones
                Long idProd = productoDAO.crear(p, conn)
                        .orElseThrow(() -> new DataAccessException("No se pudo crear el producto", null));

                Long idCb = cbDAO.crear(cb, conn, idProd)
                        .orElseThrow(() -> new DataAccessException("No se pudo crear el código de barras", null));

                conn.commit();
                System.out.println("Operación exitosa.");
            } catch (Exception e) {
                try {
                    conn.rollback();
                    System.out.println("Se realizó un rollback debido a un error.");
                } catch (SQLException ex) {
                    throw new DataAccessException("Error crítico: no se pudo revertir la transacción", ex);
                }
                throw e; // vuelve a lanzar para Service/Controller
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (DataAccessException e) {
            System.out.println("Error de acceso a datos: " + e.getMessage());
        }
    }

    public void listarProductos() {
        try {
            List<Producto> lista = productoDAO.obtenerTodos();
            System.out.println("\n--- Productos ---");
            if (lista.isEmpty()) System.out.println("(sin registros)");
            else {
                for (Producto p : lista) {
                    System.out.println(p);
                    if (p.getCodigoBarras() != null) System.out.println("   -> " + p.getCodigoBarras());
                }
            }
        } catch (DataAccessException e) {
            System.out.println("Error al listar productos. Detalle: " + e.getMessage());
        }
    }

    public void buscarPorId() {
        Long id = leerLong("ID producto: ");
        try{
            Optional<Producto> opt = productoDAO.obtenerPorId(id);
            opt.ifPresentOrElse(
                    p -> {
                        System.out.println(p);
                        if (p.getCodigoBarras() != null);
                    },
                    () -> System.out.println("No encontrado.")
            );
        } catch (DataAccessException e) {
            System.out.println("Error al buscar el producto por id. Detalle: " + e.getMessage());
        }
    }

    public void actualizar() {
        Long id = leerLong("ID a actualizar: ");
        try {
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
            System.out.println(ok ? "Actualizado." : "Error al actualizar.");
        } catch (DataAccessException e) {
            System.out.println("Error al actualizar producto. Detalle: " + e.getMessage());
        }
    }

    public void eliminarLogico() {
        Long id = leerLong("ID a eliminar (baja lógica): ");
        try {
            if (!Objects.equals(id, "")){
                boolean ok = productoDAO.eliminarLogico(id);
                System.out.println(ok ? "Eliminado lógicamente." : "No se pudo eliminar.");
            }
        } catch (DataAccessException e) {
            System.out.println("Error al eliminar producto. Detalle: " + e.getMessage());
        }
        
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
