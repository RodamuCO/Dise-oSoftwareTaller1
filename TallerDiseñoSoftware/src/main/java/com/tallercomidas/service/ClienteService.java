package com.tallercomidas.service;

import com.tallercomidas.dao.ClienteDAO;
import com.tallercomidas.model.Cliente;

import java.sql.SQLException;
import java.util.List;

/**
 * Contiene la lógica de negocio y validaciones antes de delegar al DAO.
 */
public class ClienteService {

    private final ClienteDAO clienteDAO;

    public ClienteService() {
        this.clienteDAO = new ClienteDAO();
    }

    // ── CREATE ─────────────────────────────────────────────────────────────────

    public boolean agregarCliente(String nombre, String email,
                                  String telefono, String direccion) throws SQLException {
        validarCamposCliente(nombre, email, telefono, direccion);
        Cliente cliente = new Cliente(nombre.trim(), email.trim().toLowerCase(),
                                      telefono.trim(), direccion.trim());
        return clienteDAO.insertar(cliente);
    }

    // UPDATE
    public boolean actualizarCliente(int id, String nombre, String email,
                                     String telefono, String direccion) throws SQLException {
        validarCamposCliente(nombre, email, telefono, direccion);
        Cliente existente = clienteDAO.buscarPorId(id);
        if (existente == null) {
            throw new IllegalArgumentException("No existe un cliente con ID: " + id);
        }
        existente.setNombre(nombre.trim());
        existente.setEmail(email.trim().toLowerCase());
        existente.setTelefono(telefono.trim());
        existente.setDireccion(direccion.trim());
        return clienteDAO.actualizar(existente);
    }

    // DELETE

    public boolean eliminarCliente(int id) throws SQLException {
        Cliente existente = clienteDAO.buscarPorId(id);
        if (existente == null) {
            throw new IllegalArgumentException("No existe un cliente con ID: " + id);
        }
        return clienteDAO.eliminar(id);
    }

    // READ

    public Cliente buscarClientePorId(int id) throws SQLException {
        return clienteDAO.buscarPorId(id);
    }

    public List<Cliente> listarClientes() throws SQLException {
        return clienteDAO.listarTodos();
    }

    public List<Cliente> buscarPorNombre(String nombre) throws SQLException {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de búsqueda no puede estar vacío.");
        }
        return clienteDAO.buscarPorNombre(nombre.trim());
    }

    // Validaciones

    private void validarCamposCliente(String nombre, String email,
                                      String telefono, String direccion) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("El email no tiene un formato válido.");
        if (telefono == null || telefono.isBlank())
            throw new IllegalArgumentException("El teléfono del cliente es obligatorio.");
        if (direccion == null || direccion.isBlank())
            throw new IllegalArgumentException("La dirección del cliente es obligatoria.");
    }
}
