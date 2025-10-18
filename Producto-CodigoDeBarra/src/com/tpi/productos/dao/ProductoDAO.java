package com.tpi.productos.dao;

import com.tpi.productos.DBConnection.DBConnection;
import com.tpi.productos.model.Producto;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoDAO {

    // CREATE
    public Optional<Integer> crearProducto(Producto p) {
        String sql = "INSERT INTO producto (nombre, descripcion, precio, "
                + "stock, codigo_barra, id_categoria) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setBigDecimal(3, p.getPrecio());
            ps.setInt(4, p.getStock());
            ps.setString(5, p.getCodigoBarra());
            if (p.getIdCategoria() == null) ps.setNull(6, Types.INTEGER);
            else ps.setInt(6, p.getIdCategoria());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                return Optional.empty();
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return Optional.of(rs.getInt(1));
                }
            }
        } catch (SQLIntegrityConstraintViolationException ex) {
            System.err.println("Violación de constraint: " + ex.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // READ por id
    public Optional<Producto> obtenerPorId(int id) {
        String sql = "SELECT * FROM producto WHERE id_producto = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearProducto(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // READ por codigo de barra
    public Optional<Producto> obtenerPorCodigoBarra(String codigo) {
        String sql = "SELECT * FROM producto WHERE codigo_barra = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearProducto(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // READ ALL
    public List<Producto> obtenerTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM producto ORDER BY id_producto";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearProducto(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // UPDATE
    public boolean actualizarProducto(Producto p) {
        String sql = "UPDATE producto SET nombre = ?, descripcion = ?, precio = ?, "
                + "stock = ?, codigo_barra = ?, id_categoria = ? WHERE id_producto = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setBigDecimal(3, p.getPrecio());
            ps.setInt(4, p.getStock());
            ps.setString(5, p.getCodigoBarra());
            if (p.getIdCategoria() == null) ps.setNull(6, Types.INTEGER);
            else ps.setInt(6, p.getIdCategoria());
            ps.setInt(7, p.getIdProducto());
            int r = ps.executeUpdate();
            return r > 0;
        } catch (SQLIntegrityConstraintViolationException ex) {
            System.err.println("Violación de constraint: " + ex.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // DELETE
    public boolean eliminarProducto(int id) {
        String sql = "DELETE FROM producto WHERE id_producto = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int r = ps.executeUpdate();
            return r > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Ejemplo de operación con transacción: decrementar stock de un producto (p. ej. venta)
    // Retorna true si se pudo decrementar; false si no hay stock suficiente o error.
    public boolean decrementarStockConTransaccion(int idProducto, int cantidad) {
        String selectSql = "SELECT stock FROM producto WHERE id_producto = ? FOR UPDATE";
        String updateSql = "UPDATE producto SET stock = ? WHERE id_producto = ?";

        int maxRetries = 2;
        int attempts = 0;

        while (attempts <= maxRetries) {
            attempts++;
            try (Connection conn = DBConnection.getConnection()) {
                try {
                    conn.setAutoCommit(false);
                    try (PreparedStatement psSelect = conn.prepareStatement(selectSql)) {
                        psSelect.setInt(1, idProducto);
                        try (ResultSet rs = psSelect.executeQuery()) {
                            if (rs.next()) {
                                int stock = rs.getInt("stock");
                                if (stock < cantidad) {
                                    conn.rollback();
                                    return false; // no hay stock suficiente
                                }
                                int nuevoStock = stock - cantidad;
                                try (PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {
                                    psUpdate.setInt(1, nuevoStock);
                                    psUpdate.setInt(2, idProducto);
                                    psUpdate.executeUpdate();
                                }
                                conn.commit();
                                return true;
                            } else {
                                conn.rollback();
                                return false; // producto no encontrado
                            }
                        }
                    }
                } catch (SQLException e) {
                    // chequeo simple para deadlock / lock wait (MySQL SQLState 40001 o error 1213)
                    try {
                        conn.rollback();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    String sqlState = e.getSQLState();
                    int errorCode = e.getErrorCode();
                    if ("40001".equals(sqlState) || errorCode == 1213) {
                        // retry con backoff
                        try {
                            Thread.sleep(100L * attempts);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                        continue; // reintentar
                    } else {
                        e.printStackTrace();
                        return false;
                    }
                } finally {
                    try {
                        conn.setAutoCommit(true);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_producto");
        String nombre = rs.getString("nombre");
        String descripcion = rs.getString("descripcion");
        BigDecimal precio = rs.getBigDecimal("precio");
        int stock = rs.getInt("stock");
        String codigo = rs.getString("codigo_barra");
        int idCat = rs.getInt("id_categoria");
        Integer idCategoria = rs.wasNull() ? null : idCat;
        return new Producto(id, nombre, descripcion, precio, stock, codigo, idCategoria);
    }
}
