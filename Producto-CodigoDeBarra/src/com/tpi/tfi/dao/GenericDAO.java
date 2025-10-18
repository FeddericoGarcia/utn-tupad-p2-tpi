package com.tpi.tfi.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GenericDAO<T> {
    Optional<Long> crear(T entity, Connection conn) throws SQLException;
    Optional<T> obtenerPorId(Long id);
    List<T> obtenerTodos();
    boolean actualizar(T entity);
    boolean eliminarLogico(Long id);
}
