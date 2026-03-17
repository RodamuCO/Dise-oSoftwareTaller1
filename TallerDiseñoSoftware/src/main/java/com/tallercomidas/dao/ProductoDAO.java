package com.tallercomidas.dao;

import com.tallercomidas.model.Producto;
import com.tallercomidas.model.Producto.Categoria;
import com.tallercomidas.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del DAO para la entidad Producto.
 * Gestiona todas las operaciones CRUD sobre la tabla "productos" en SQLite.
 */
public class ProductoDAO implements CrudDAO<Producto, Integer> {

    // CREATE

    @Override
    public boolean insertar(Producto producto) throws SQLException {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, categoria, disponible) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setString(4, producto.getCategoria().name());
            ps.setInt(5, producto.isDisponible() ? 1 : 0);
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    // UPDATE

    @Override
    public boolean actualizar(Producto producto) throws SQLException {
        String sql = "UPDATE productos SET nombre=?, descripcion=?, precio=?, categoria=?, disponible=? WHERE id=?";
        Connection conn = DatabaseConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setString(4, producto.getCategoria().name());
            ps.setInt(5, producto.isDisponible() ? 1 : 0);
            ps.setInt(6, producto.getId());
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    // DELETE

    @Override
    public boolean eliminar(Integer id) throws SQLException {
        String sql = "DELETE FROM productos WHERE id=?";
        Connection conn = DatabaseConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    // READ

    @Override
    public Producto buscarPorId(Integer id) throws SQLException {
        String sql = "SELECT * FROM productos WHERE id=?";
        Connection conn = DatabaseConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Producto> listarTodos() throws SQLException {
        String sql = "SELECT * FROM productos ORDER BY categoria, nombre";
        List<Producto> productos = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }
        }
        return productos;
    }

    /**
     * Filtra productos por categoría.
     */
    public List<Producto> listarPorCategoria(Categoria categoria) throws SQLException {
        String sql = "SELECT * FROM productos WHERE categoria=? ORDER BY nombre";
        List<Producto> productos = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoria.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    productos.add(mapearProducto(rs));
                }
            }
        }
        return productos;
    }

    /**
     * Retorna sólo los productos con disponible = true.
     */
    public List<Producto> listarDisponibles() throws SQLException {
        String sql = "SELECT * FROM productos WHERE disponible=1 ORDER BY categoria, nombre";
        List<Producto> productos = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }
        }
        return productos;
    }

    // Helper

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        return new Producto(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("descripcion"),
            rs.getDouble("precio"),
            Categoria.valueOf(rs.getString("categoria")),
            rs.getInt("disponible") == 1
        );
    }
}
