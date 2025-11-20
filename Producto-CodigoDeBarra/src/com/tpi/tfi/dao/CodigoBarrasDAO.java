package com.tpi.tfi.dao;

import com.tpi.tfi.config.DatabaseConnection;
import com.tpi.tfi.entities.CodigoBarras;
import com.tpi.tfi.enums.TipoCB;
import com.tpi.tfi.exceptions.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CodigoBarrasDAO implements GenericDAO<CodigoBarras> {

    // Crear con posibilidad de setear producto_id (puede ser null)
    public Optional<Long> crear(CodigoBarras cb, Connection conn, Long productoId) throws SQLException {
        String sql = "INSERT INTO codigo_barras (tipo, valor, fecha_asignacion, observaciones, producto_id, eliminado) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cb.getTipo().name());
            ps.setString(2, cb.getValor());
            if (cb.getFechaAsignacion() != null) ps.setDate(3, Date.valueOf(cb.getFechaAsignacion()));
            else ps.setNull(3, Types.DATE);
            ps.setString(4, cb.getObservaciones());
            if (productoId == null) ps.setNull(5, Types.BIGINT);
            else ps.setLong(5, productoId);
            ps.setBoolean(6, cb.isEliminado());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return Optional.of(rs.getLong(1));
            }
        } catch (SQLException e) {  
            throw new DataAccessException("Error al acceder a la BD en CodigoBarrasDAO.obtenerPorId()", e); 
        }
        return Optional.empty();
    }
    
    /*REVISAR
    public void crear(Connection conn, CodigoBarras cb, Long idProducto) {
        String sql = "INSERT INTO codigo_barras (codigo, producto_id) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, cb.getId());
            ps.setLong(2, idProducto);

            ps.executeUpdate();

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate"))
                throw new DataAccessException("Este código de barras ya existe.", e);

            throw new DataAccessException("Error al crear código de barras.", e);
        }
    }
*/

    // Implementación de GenericDAO.crear sin Connection (no usada para operaciones transaccionales)
    @Override
    public Optional<Long> crear(CodigoBarras entity, Connection conn) throws SQLException {
        return crear(entity, conn, null);
    }

    @Override
    public Optional<CodigoBarras> obtenerPorId(Long id) {
        String sql = "SELECT * FROM codigo_barras WHERE id = ? AND eliminado = FALSE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        } catch (SQLException e) {  
            throw new DataAccessException("Error al acceder a la BD en CodigoBarrasDAO.obtenerPorId()", e); 
        }
        return Optional.empty();
    }

    // Obtener por producto_id (relación 1->1)
    public Optional<CodigoBarras> obtenerPorProductoId(Long productoId) {
        String sql = "SELECT * FROM codigo_barras WHERE producto_id = ? AND eliminado = FALSE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        } catch (SQLException e) {  
            throw new DataAccessException("Error al acceder a la BD en CodigoBarrasDAO.obtenerPorProductoId()", e); 
        }
        return Optional.empty();
    }

    @Override
    public List<CodigoBarras> obtenerTodos() {
        List<CodigoBarras> lista = new ArrayList<>();
        String sql = "SELECT * FROM codigo_barras WHERE eliminado = FALSE";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        } catch (SQLException e) {  
            throw new DataAccessException("Error al acceder a la BD en CodigoBarrasDAO.obtenerTodos()", e); 
        }
        return lista;
    }

    @Override
    public boolean actualizar(CodigoBarras cb) {
        String sql = "UPDATE codigo_barras SET tipo=?, valor=?, fecha_asignacion=?, observaciones=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cb.getTipo().name());
            ps.setString(2, cb.getValor());
            if (cb.getFechaAsignacion() != null) ps.setDate(3, Date.valueOf(cb.getFechaAsignacion()));
            else ps.setNull(3, Types.DATE);
            ps.setString(4, cb.getObservaciones());
            ps.setLong(5, cb.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {  
            throw new DataAccessException("Error al acceder a la BD en CodigoBarrasDAO.actualizar()", e); 
        }
    }

    @Override
    public boolean eliminarLogico(Long id) {
        String sql = "UPDATE codigo_barras SET eliminado = TRUE WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {  
            throw new DataAccessException("Error al acceder a la BD en CodigoBarrasDAO.eliminarLogico()", e); 
        }
    }

    private CodigoBarras map(ResultSet rs) throws SQLException {
        CodigoBarras cb = new CodigoBarras();
        cb.setId(rs.getLong("id"));
        String tipoStr = rs.getString("tipo");
        try {
            cb.setTipo(tipoStr != null ? TipoCB.valueOf(tipoStr) : null);
        } catch (IllegalArgumentException ex) {
            cb.setTipo(null);
        }
        cb.setValor(rs.getString("valor"));
        return cb;
    }
    
    public boolean existeCodigo(Connection conn, String codigo) {
        String sql = "SELECT id FROM codigo_barras WHERE codigo = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new DataAccessException("Error al verificar existencia de código de barras.", e);
        }
    }
    
}
