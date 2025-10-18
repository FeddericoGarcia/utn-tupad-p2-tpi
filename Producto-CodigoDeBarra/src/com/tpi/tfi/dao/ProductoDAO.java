package com.tpi.tfi.dao;

import com.tpi.tfi.config.DatabaseConnection;
import com.tpi.tfi.entities.CodigoBarras;
import com.tpi.tfi.entities.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoDAO implements GenericDAO<Producto> {

    private final CodigoBarrasDAO codigoBarrasDAO = new CodigoBarrasDAO();

    // Crear producto (usa Connection externo para transacciones)
    @Override
    public Optional<Long> crear(Producto p, Connection conn) throws SQLException {
        String sql = "INSERT INTO producto (eliminado, nombre, marca, categoria, precio, peso) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setBoolean(1, p.isEliminado());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getMarca());
            ps.setString(4, p.getCategoria());
            ps.setDouble(5, p.getPrecio());
            if (p.getPeso() != null) ps.setDouble(6, p.getPeso());
            else ps.setNull(6, Types.DOUBLE);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return Optional.of(rs.getLong(1));
            }
        }
        return Optional.empty();
    }

    // Implementación GenericDAO.crear con Connection param (obligatorio en interfaz)
    // already matching override

    @Override
    public Optional<Producto> obtenerPorId(Long id) {
        String sql = "SELECT * FROM producto WHERE id = ? AND eliminado = FALSE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Producto p = map(rs);
                    // Obtener el CodigoBarras asociado (si existe)
                    codigoBarrasDAO.obtenerPorProductoId(p.getId()).ifPresent(p::setCodigoBarras);
                    return Optional.of(p);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    @Override
    public List<Producto> obtenerTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM producto WHERE eliminado = FALSE ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Producto p = map(rs);
                codigoBarrasDAO.obtenerPorProductoId(p.getId()).ifPresent(p::setCodigoBarras);
                lista.add(p);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public boolean actualizar(Producto p) {
        String sql = "UPDATE producto SET nombre=?, marca=?, categoria=?, precio=?, peso=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getMarca());
            ps.setString(3, p.getCategoria());
            ps.setDouble(4, p.getPrecio());
            if (p.getPeso() != null) ps.setDouble(5, p.getPeso());
            else ps.setNull(5, Types.DOUBLE);
            ps.setLong(6, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean eliminarLogico(Long id) {
        String sql = "UPDATE producto SET eliminado = TRUE WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private Producto map(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getLong("id"));
        p.setEliminado(rs.getBoolean("eliminado"));
        p.setNombre(rs.getString("nombre"));
        p.setMarca(rs.getString("marca"));
        p.setCategoria(rs.getString("categoria"));
        p.setPrecio(rs.getDouble("precio"));
        double pesoVal = rs.getDouble("peso");
        if (!rs.wasNull()) p.setPeso(pesoVal);
        return p;
    }
}
