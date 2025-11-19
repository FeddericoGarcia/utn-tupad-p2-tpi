package com.tpi.tfi.main;

import com.tpi.tfi.service.CodigoBarrasService;
import com.tpi.tfi.service.ProductoService;
import java.sql.SQLException;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppMenu {

    private static final Scanner sc = new Scanner(System.in);
    private static final ProductoService productoService = new ProductoService();
    private static final CodigoBarrasService cbService = new CodigoBarrasService();

    public static void main(String[] args) {
        int opt;
        do {
            mostrarMenu();
            opt = leerInt("Elija una opción: ");
            switch (opt) {
                case 1 -> menuProductos();
                case 2 -> menuCodigos();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opt != 0);
        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n===== APLICACIÓN TFI - Producto → CodigoBarras (1→1) =====");
        System.out.println("1. Gestión de Productos");
        System.out.println("2. Gestión de Códigos de Barras");
        System.out.println("0. Salir");
    }

    private static void menuProductos() {
        int opt;
        do {
            System.out.println("\n--- Productos ---");
            System.out.println("1. Crear producto + código (transaccional)");
            System.out.println("2. Listar todos");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Actualizar producto");
            System.out.println("5. Baja lógica producto");
            System.out.println("0. Volver");
            opt = leerInt("Opción: ");
            try {
                switch (opt) {
                    case 1 -> productoService.crearProductoConCodigoBarras();
                    case 2 -> productoService.listarProductos();
                    case 3 -> productoService.buscarPorId();
                    case 4 -> productoService.actualizar();
                    case 5 -> productoService.eliminarLogico();
                    case 0 -> {}
                    default -> System.out.println("Opción inválida.");
                }
            } catch (SQLException ex) {
                Logger.getLogger(AppMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (opt != 0);
    }

    private static void menuCodigos() {
        int opt;
        do {
            System.out.println("\n--- Códigos de Barras ---");
            System.out.println("1. Crear código (sin asociar)");
            System.out.println("2. Listar todos");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Actualizar código");
            System.out.println("5. Baja lógica código");
            System.out.println("0. Volver");
            opt = leerInt("Opción: ");
            switch (opt) {
                case 1 -> cbService.crearManual();
                case 2 -> cbService.listar();
                case 3 -> cbService.buscarPorId();
                case 4 -> cbService.actualizar();
                case 5 -> cbService.eliminarLogico();
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
                System.out.println("Número inválido.");
            }
        }
    }
}
