package com.tpi.tfi.main;

import com.tpi.tfi.service.CodigoBarrasService;
import com.tpi.tfi.service.ProductoService;

import java.util.Scanner;

public class AppMenu {

    private static final Scanner sc = new Scanner(System.in);
    private static final ProductoService productoService = new ProductoService();
    private static final CodigoBarrasService cbService = new CodigoBarrasService();

    public static void main(String[] args) {
        while (true) {
            try {
                System.out.println("");
                System.out.println("""
                                   Trabajo Práctico Integrador de la materia Programación II
                                    de la Universidad Tecnológica Nacional (UTN)""");
                System.out.println("\n===============================");
                System.out.println("""
                                   Integrantes: 
                                   - Federico Garcia Bengolea
                                   - Alan N. Jofre
                                   - Jazmin Herrera
                                   - Akier Aguirrezabala""");
                System.out.println("\n===============================");
                System.out.println("""
                                    Sistema de gestión de Productos 
                                        y Códigos de Barra""");
                System.out.println("\n===============================");
                System.out.println("        MENU PRINCIPAL        ");
                System.out.println("===============================");
                System.out.println("1. Gestión de Productos");
                System.out.println("2. Gestión de Códigos de Barras");
                System.out.println("0. Salir");
                System.out.print("Opción: ");

                String opcion = sc.nextLine().trim();

                switch (opcion) {
                    case "1" -> menuProductos();
                    case "2" -> menuCodigos();
                    case "0" -> {
                        System.out.println("Saliendo del sistema...");
                        return;
                    }
                    default -> System.out.println("Opción inválida.");
                }

            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
            }
        }
    }

    // MENU DE PRODUCTOS
    private static void menuProductos() {
        while (true) {
            try {
                System.out.println("\n---------- MENÚ PRODUCTOS ----------");
                System.out.println("1. Crear Producto con Código");
                System.out.println("2. Listar Productos");
                System.out.println("3. Actualizar ");
                System.out.println("4. Eliminar ");
                System.out.println("5. Buscar por ID");
                System.out.println("0. Volver");
                System.out.print("Opción: ");

                String op = sc.nextLine().trim();

                switch (op) {
                    case "1" -> productoService.crearProductoConCodigoBarras();
                    case "2" -> productoService.listarProductos();
                    case "3" -> productoService.actualizar(); 
                    case "4" -> productoService.eliminarLogico();
                    case "5" -> productoService.buscarPorId();
                    case "0" -> { return; }
                    default -> System.out.println("Opción inválida.");
                }

            } catch (Exception e) {
                System.out.println("Error en menú Productos: " + e.getMessage());
            }
        }
    }

    // MENU DE CODIGO DE BARRAS
    private static void menuCodigos() {
        while (true) {
            try {
                System.out.println("\n------ MENÚ CÓDIGO DE BARRAS ------");
                System.out.println("1. Crear Código de Barras");
                System.out.println("2. Listar Códigos de Barras");
                System.out.println("3. Actualizar  ");
                System.out.println("4. Eliminar  ");
                System.out.println("5. Buscar por ID");
                System.out.println("0. Volver");
                System.out.print("Opción: ");

                String op = sc.nextLine().trim();

                switch (op) {
                    case "1" -> cbService.crearManual();
                    case "2" -> cbService.listar();
                    case "3" -> cbService.actualizar();
                    case "4" -> cbService.eliminarLogico();
                    case "5" -> cbService.buscarPorId();
                    case "0" -> { return; }
                    default -> System.out.println("Opción inválida.");
                }

            } catch (Exception e) {
                System.out.println("Error en menú Códigos: " + e.getMessage());
            }
        }
    }
}