package com.tpi.productos.service;

import com.tpi.productos.dao.CategoriaDAO;
import com.tpi.productos.model.Categoria;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CategoriaService {
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final Scanner sc = new Scanner(System.in);

    public void crearCategoria() {
        System.out.println("\n--- Crear Categoría ---");
        String nombre = leerString("Nombre: ");
        String descripcion = leerString("Descripción: ");
        Categoria c = new Categoria(nombre, descripcion);
        categoriaDAO.crearCategoria(c)
                .ifPresentOrElse(
                        id -> System.out.println("✅ Categoría creada con ID: " + id),
                        () -> System.out.println("❌ Error al crear categoría.")
                );
    }

    public void listarCategorias() {
        System.out.println("\n--- Listado de Categorías ---");
        List<Categoria> lista = categoriaDAO.obtenerTodas();
        if (lista.isEmpty()) System.out.println("(sin registros)");
        else lista.forEach(System.out::println);
    }

    public void buscarPorId() {
        int id = leerInt("ID categoría: ");
        Optional<Categoria> c = categoriaDAO.obtenerPorId(id);
        c.ifPresentOrElse(System.out::println, () -> System.out.println("No encontrada."));
    }

    public void actualizarCategoria() {
        int id = leerInt("ID a actualizar: ");
        Optional<Categoria> opt = categoriaDAO.obtenerPorId(id);
        if (opt.isEmpty()) {
            System.out.println("Categoría no encontrada.");
            return;
        }
        Categoria c = opt.get();
        System.out.println("Dejar vacío para mantener valor actual.");
        String nombre = leerStringDefault("Nombre (" + c.getNombre() + "): ", c.getNombre());
        String descripcion = leerStringDefault("Descripción (" + c.getDescripcion() + "): ", c.getDescripcion());
        c.setNombre(nombre);
        c.setDescripcion(descripcion);
        boolean ok = categoriaDAO.actualizarCategoria(c);
        System.out.println(ok ? "✅ Actualizado correctamente." : "❌ Error al actualizar.");
    }

    public void eliminarCategoria() {
        int id = leerInt("ID a eliminar: ");
        boolean ok = categoriaDAO.eliminarCategoria(id);
        System.out.println(ok ? "🗑️ Eliminado correctamente." : "❌ No se pudo eliminar.");
    }

    // Métodos auxiliares
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
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Número inválido. Intentá de nuevo.");
            }
        }
    }
}
