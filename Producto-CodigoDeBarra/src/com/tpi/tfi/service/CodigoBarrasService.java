package com.tpi.tfi.service;

import com.tpi.tfi.dao.CodigoBarrasDAO;
import com.tpi.tfi.entities.CodigoBarras;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CodigoBarrasService {

    private final CodigoBarrasDAO cbDao = new CodigoBarrasDAO();
    private final Scanner sc = new Scanner(System.in);

    public void crearManual() {
        System.out.println("\n--- Crear Código de Barras (manual) ---");
        String tipo = leerTipo();
        String valor = leerValor();
        LocalDate fecha = leerFecha("Fecha asignación (YYYY-MM-DD, ENTER para hoy): ", true);
        String obs = leerString("Observaciones (ENTER para none): ");

        CodigoBarras cb = new CodigoBarras(tipo, valor, fecha, obs.isBlank() ? null : obs);
        try {
            // usamos una conexión simple (no transaccional)
            var conn = com.tpi.tfi.config.DatabaseConnection.getConnection();
            try (conn) {
                cbDao.crear(cb, conn); // producto_id null
                System.out.println("✅ Código de barras creado (sin asociar a producto).");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al crear código de barras: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void listar() {
        List<CodigoBarras> lista = cbDao.obtenerTodos();
        System.out.println("\n--- Códigos de Barras ---");
        if (lista.isEmpty()) System.out.println("(sin registros)");
        else lista.forEach(System.out::println);
    }

    public void buscarPorId() {
        Long id = leerLong("ID código de barras: ");
        Optional<CodigoBarras> opt = cbDao.obtenerPorId(id);
        opt.ifPresentOrElse(System.out::println, () -> System.out.println("No encontrado."));
    }

    public void actualizar() {
        Long id = leerLong("ID a actualizar: ");
        var opt = cbDao.obtenerPorId(id);
        if (opt.isEmpty()) {
            System.out.println("No existe.");
            return;
        }
        CodigoBarras cb = opt.get();
        System.out.println("Dejar vacío para mantener valor actual.");
        String tipo = leerStringDefault("Tipo (" + cb.getTipo() + "): ", cb.getTipo());
        String valor = leerStringDefault("Valor (" + cb.getValor() + "): ", cb.getValor());
        LocalDate fecha = leerFechaDefault("Fecha asignación (" + cb.getFechaAsignacion() + "): ", cb.getFechaAsignacion());
        String obs = leerStringDefault("Observaciones (" + cb.getObservaciones() + "): ", cb.getObservaciones());
        cb.setTipo(tipo);
        cb.setValor(valor);
        cb.setFechaAsignacion(fecha);
        cb.setObservaciones(obs);
        boolean ok = cbDao.actualizar(cb);
        System.out.println(ok ? "✅ Actualizado." : "❌ Error al actualizar.");
    }

    public void eliminarLogico() {
        Long id = leerLong("ID a eliminar (baja lógica): ");
        boolean ok = cbDao.eliminarLogico(id);
        System.out.println(ok ? "🗑️ Eliminado lógicamente." : "❌ No se pudo eliminar.");
    }

    // Helpers de input
    private String leerString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private String leerStringDefault(String prompt, String def) {
        System.out.print(prompt);
        String s = sc.nextLine().trim();
        return s.isEmpty() ? def : s;
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

    private LocalDate leerFecha(String prompt, boolean allowToday) {
        System.out.print(prompt);
        String s = sc.nextLine().trim();
        if (s.isEmpty() && allowToday) return LocalDate.now();
        try {
            return LocalDate.parse(s);
        } catch (Exception e) {
            System.out.println("Formato inválido. Se usa hoy.");
            return LocalDate.now();
        }
    }

    private LocalDate leerFechaDefault(String prompt, LocalDate def) {
        System.out.print(prompt);
        String s = sc.nextLine().trim();
        if (s.isEmpty()) return def;
        try {
            return LocalDate.parse(s);
        } catch (Exception e) {
            System.out.println("Formato inválido. Se usa valor actual.");
            return def;
        }
    }

    private String leerTipo() {
        while (true) {
            System.out.print("Tipo (EAN13 / EAN8 / UPC): ");
            String t = sc.nextLine().trim().toUpperCase();
            if (t.equals("EAN13") || t.equals("EAN8") || t.equals("UPC")) return t;
            System.out.println("Tipo inválido.");
        }
    }

    private String leerValor() {
        while (true) {
            System.out.print("Valor: ");
            String v = sc.nextLine().trim();
            if (!v.isEmpty()) return v;
            System.out.println("No puede estar vacío.");
        }
    }
}
