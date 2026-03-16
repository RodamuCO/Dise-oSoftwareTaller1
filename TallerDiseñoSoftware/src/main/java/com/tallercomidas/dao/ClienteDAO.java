package com.tallercomidas.dao;

import com.tallercomidas.model.Cliente;
import com.tallercomidas.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements CrudDAO<Cliente, Integer> {

    // ── CREATE ─────────────────────────────────────────────────────────────────

    @Override
    public boolean insertar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (nombre, email, telefono, direccion) VALUES (?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getEmail());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getDireccion());
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    // ── UPDATE ─────────────────────────────────────────────────────────────────

    @Override
    public boolean actualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nombre=?, email=?, telefono=?, direccion=? WHERE id=?";
        Connection conn = DatabaseConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getEmail());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getDireccion());
            ps.setInt(5, cliente.getId());
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    // ── DELETE ─────────────────────────────────────────────────────────────────

    @Override
    public boolean eliminar(Integer id) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id=?";
        Connection conn = DatabaseConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    // ── READ ───────────────────────────────────────────────────────────────────

    @Override
    public Cliente buscarPorId(Integer id) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE id=?";
        Connection conn = DatabaseConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Cliente> listarTodos() throws SQLException {
        String sql = "SELECT * FROM clientes ORDER BY id";
        List<Cliente> clientes = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
        }
        return clientes;
    }

    /**
     * Busca clientes cuyo nombre contenga el texto indicado (búsqueda parcial).
     */
    public List<Cliente> buscarPorNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE LOWER(nombre) LIKE LOWER(?) ORDER BY nombre";
        List<Cliente> clientes = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + nombre + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clientes.add(mapearCliente(rs));
                }
            }
        }
        return clientes;
    }

    // ── Helper ─────────────────────────────────────────────────────────────────

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        return new Cliente(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("email"),
            rs.getString("telefono"),
            rs.getString("direccion")
        );
    }
}
