package com.tallercomidas.service;

import com.tallercomidas.dao.ProductoDAO;
import com.tallercomidas.model.Producto;
import com.tallercomidas.model.Producto.Categoria;

import java.sql.SQLException;
import java.util.List;

/**
 * Capa de servicio para la entidad Producto.
 * Contiene la lógica de negocio y validaciones antes de delegar al DAO.
 */
public class ProductoService {

    private final ProductoDAO productoDAO;

    public ProductoService() {
        this.productoDAO = new ProductoDAO();
    }

    // ── CREATE ─────────────────────────────────────────────────────────────────

    public boolean agregarProducto(String nombre, String descripcion,
                                   double precio, Categoria categoria) throws SQLException {
        validarCamposProducto(nombre, descripcion, precio);
        Producto producto = new Producto(nombre.trim(), descripcion.trim(), precio, categoria);
        return productoDAO.insertar(producto);
    }

    // ── UPDATE ─────────────────────────────────────────────────────────────────

    public boolean actualizarProducto(int id, String nombre, String descripcion,
                                      double precio, Categoria categoria,
                                      boolean disponible) throws SQLException {
        validarCamposProducto(nombre, descripcion, precio);
        Producto existente = productoDAO.buscarPorId(id);
        if (existente == null) {
            throw new IllegalArgumentException("No existe un producto con ID: " + id);
        }
        existente.setNombre(nombre.trim());
        existente.setDescripcion(descripcion.trim());
        existente.setPrecio(precio);
        existente.setCategoria(categoria);
        existente.setDisponible(disponible);
        return productoDAO.actualizar(existente);
    }

    // ── DELETE ─────────────────────────────────────────────────────────────────

    public boolean eliminarProducto(int id) throws SQLException {
        Producto existente = productoDAO.buscarPorId(id);
        if (existente == null) {
            throw new IllegalArgumentException("No existe un producto con ID: " + id);
        }
        return productoDAO.eliminar(id);
    }

    // ── READ ───────────────────────────────────────────────────────────────────

    public Producto buscarProductoPorId(int id) throws SQLException {
        return productoDAO.buscarPorId(id);
    }

    public List<Producto> listarProductos() throws SQLException {
        return productoDAO.listarTodos();
    }

    public List<Producto> listarPorCategoria(Categoria categoria) throws SQLException {
        return productoDAO.listarPorCategoria(categoria);
    }

    public List<Producto> listarDisponibles() throws SQLException {
        return productoDAO.listarDisponibles();
    }

    // ── Validaciones ───────────────────────────────────────────────────────────

    private void validarCamposProducto(String nombre, String descripcion, double precio) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        if (descripcion == null || descripcion.isBlank())
            throw new IllegalArgumentException("La descripción del producto es obligatoria.");
        if (precio <= 0)
            throw new IllegalArgumentException("El precio debe ser mayor a 0.");
    }
}
