package com.tallercomidas.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz genérica que define las operaciones CRUD estándar.
 */
public interface CrudDAO<T, ID> {

    /**
     * Inserta una nueva entidad en la base de datos.
     * @param entidad objeto a persistir
     * @return true si la operación fue exitosa
     */
    boolean insertar(T entidad) throws SQLException;

    /**
     * Actualiza una entidad existente en la base de datos.
     * @param entidad objeto con los datos actualizados (debe contener ID válido)
     * @return true si la operación fue exitosa
     */
    boolean actualizar(T entidad) throws SQLException;

    /**
     * Elimina una entidad de la base de datos por su ID.
     * @param id identificador del registro a eliminar
     * @return true si la operación fue exitosa
     */
    boolean eliminar(ID id) throws SQLException;

    /**
     * Busca una entidad por su ID.
     * @param id identificador del registro
     * @return la entidad encontrada, o null si no existe
     */
    T buscarPorId(ID id) throws SQLException;

    /**
     * Retorna todos los registros de la entidad.
     * @return lista de entidades
     */
    List<T> listarTodos() throws SQLException;
}
