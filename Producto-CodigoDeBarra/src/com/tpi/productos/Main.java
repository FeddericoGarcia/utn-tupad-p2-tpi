package com.tpi.productos;

import com.tpi.productos.service.CategoriaService;
import com.tpi.productos.service.ProductoService;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final ProductoService productoService = new ProductoService();
    private static final CategoriaService categoriaService = new CategoriaService();

    public static void main(String[] args) {
        int opt;
        do {
            mostrarMenu();
            opt = leerInt("Elija una opción: ");
            switch (opt) {
                case 1 -> menuProductos();
                case 2 -> menuCategorias();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opt != 0);
        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n===== SISTEMA DE GESTIÓN =====");
        System.out.println("1. Gestión de Productos");
        System.out.println("2. Gestión de Categorías");
        System.out.println("0. Salir");
    }

    // Submenú Productos
    private static void menuProductos() {
        int opt;
        do {
            System.out.println("\n--- Gestión de Productos ---");
            System.out.println("1. Crear producto");
            System.out.println("2. Listar todos");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Buscar por Código de Barra");
            System.out.println("5. Actualizar producto");
            System.out.println("6. Eliminar producto");
            System.out.println("7. Decrementar stock (transacción)");
            System.out.println("0. Volver");
            opt = leerInt("Elija una opción: ");
            switch (opt) {
                case 1 -> productoService.crearProducto();
                case 2 -> productoService.listarProductos();
                case 3 -> productoService.buscarPorId();
                case 4 -> productoService.buscarPorCodigo();
                case 5 -> productoService.actualizarProducto();
                case 6 -> productoService.eliminarProducto();
                case 7 -> productoService.decrementarStock();
                case 0 -> {}
                default -> System.out.println("Opción inválida.");
            }
        } while (opt != 0);
    }

    // Submenú Categorías
    private static void menuCategorias() {
        int opt;
        do {
            System.out.println("\n--- Gestión de Categorías ---");
            System.out.println("1. Crear categoría");
            System.out.println("2. Listar todas");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Actualizar categoría");
            System.out.println("5. Eliminar categoría");
            System.out.println("0. Volver");
            opt = leerInt("Elija una opción: ");
            switch (opt) {
                case 1 -> categoriaService.crearCategoria();
                case 2 -> categoriaService.listarCategorias();
                case 3 -> categoriaService.buscarPorId();
                case 4 -> categoriaService.actualizarCategoria();
                case 5 -> categoriaService.eliminarCategoria();
                case 0 -> {}
                default -> System.out.println("Opción inválida.");
            }
        } while (opt != 0);
    }

    private static int leerInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Número inválido. Intentá de nuevo.");
            }
        }
    }
}

