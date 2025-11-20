package com.tpi.tfi.service;

import com.tpi.tfi.config.DatabaseConnection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.sql.Connection;

import com.tpi.tfi.dao.CodigoBarrasDAO;
import com.tpi.tfi.entities.CodigoBarras;
import com.tpi.tfi.enums.TipoCB;
import com.tpi.tfi.exceptions.DataAccessException;
import com.tpi.tfi.utils.Validador;

public class CodigoBarrasService {

    private final CodigoBarrasDAO cbDao = new CodigoBarrasDAO();
    private final Scanner sc = new Scanner(System.in);
    
    public void crearManual() {
            System.out.println("\n--- Crear Código de Barras (manual) ---");
            System.out.println("Seleccione tipo de código:");
            System.out.println("1) EAN-13");
            System.out.println("2) EAN-8");
            System.out.println("3) UPC-A");
            String opt = sc.nextLine().trim();
            TipoCB tipo = elegirTipo(opt);
            String valor = leerValor();
            LocalDate fecha = leerFecha("Fecha asignación (YYYY-MM-DD, ENTER para hoy): ", true);
            String obs = leerString("Observaciones (ENTER para null): ");

            // VALIDACIÓN
            Validador.validarCodigoOBLIGATORIO(valor, tipo);

            CodigoBarras cb = new CodigoBarras(
                tipo,
                valor,
                fecha,
                obs.isBlank() ? null : obs
            );

            try (var conn = com.tpi.tfi.config.DatabaseConnection.getConnection()) {
                cbDao.crear(cb, conn); // producto_id null
                System.out.println("Código de barras creado (sin asociar a producto).");
            } catch (SQLException | DataAccessException e) {
                System.out.println("Error al crear código de barras. Detalle: " + e.getMessage());
            }
    }

    public void listar() {
        try {
            List<CodigoBarras> lista = cbDao.obtenerTodos();
            System.out.println("\n--- Códigos de Barras ---");
            if (lista.isEmpty()) System.out.println("(sin registros)");
            else lista.forEach(System.out::println);
        } catch (DataAccessException e) {
            System.out.println("Error al listar códigos de barras. Detalle: " + e.getMessage());
        }
    }

    public void buscarPorId() {
        try{
            Long id = leerLong("ID código de barras: ");
            try (Connection conn = DatabaseConnection.getConnection()) {

                Optional<CodigoBarras> opt = cbDao.obtenerPorId(conn, id);

                opt.ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("No encontrado.")
                );
            }

        } catch (DataAccessException e) {
            System.out.println("Error al buscar por ID. Detalle: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
        }
    }
    
    public void buscarPorValor() {
        try{
            Long valor = leerLong("Valor del código de barras: ");
            try (Connection conn = DatabaseConnection.getConnection()) {

                Optional<CodigoBarras> opt = cbDao.obtenerPorValor(conn, valor);

                opt.ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("No encontrado.")
                );
            }

        } catch (DataAccessException e) {
            System.out.println("Error al buscar por ID. Detalle: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
        }
    }

    public void actualizar() {
        try {
            Long id = leerLong("ID a actualizar: ");
            var opt = cbDao.obtenerPorId(id);
            if (opt.isEmpty()) {
                System.out.println("No existe ID.");
                return;
            }
            CodigoBarras cb = opt.get();
            System.out.println("");
            String tipoStr = leerStringDefault("Tipo : ", cb.getTipo().name());
            TipoCB tipo = TipoCB.valueOf(tipoStr);
            String valor = leerStringDefault("Valor : ", cb.getValor());
            LocalDate fecha = leerFechaDefault("Fecha asignación (" + cb.getFechaAsignacion() + "): ", cb.getFechaAsignacion());
            String obs = leerStringDefault("Observaciones (" + cb.getObservaciones() + "): ", cb.getObservaciones());
            cb.setTipo(tipo);
            cb.setValor(valor);
            cb.setFechaAsignacion(fecha);
            cb.setObservaciones(obs);
            boolean ok = cbDao.actualizar(cb);
            System.out.println(ok ? "Actualizado." : "Error al actualizar.");
        } catch (DataAccessException e) {
            System.out.println("Error al actualizar código de barras. Detalle: " + e.getMessage());
        }
    }

    public void eliminarLogico() {
        try {
            Long id = leerLong("ID a eliminar (baja lógica): ");
            boolean ok = cbDao.eliminarLogico(id);
            System.out.println(ok ? "Eliminado lógicamente." : "No se pudo eliminar.");
        } catch (DataAccessException e) {
            System.out.println("Error al eliminar código de barras. Detalle: " + e.getMessage());
        }
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

    private TipoCB elegirTipo(String opt) {
        switch (opt) {
            case "1": return TipoCB.EAN_13;
            case "2": return TipoCB.EAN_8;
            case "3": return TipoCB.UPC_A;
            default: 
                System.out.println("Opción inválida. Se usa EAN-13.");
                return TipoCB.EAN_13;
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
