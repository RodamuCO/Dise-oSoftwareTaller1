package com.tallercomidas.menu;

import com.tallercomidas.model.Producto;
import com.tallercomidas.model.Producto.Categoria;
import com.tallercomidas.service.ProductoService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Menú de consola para la gestión de Productos.
 */
public class ProductoMenu {

    private final ProductoService productoService;
    private final Scanner scanner;

    public ProductoMenu(Scanner scanner) {
        this.productoService = new ProductoService();
        this.scanner = scanner;
    }

    public void mostrarMenu() {
        boolean salir = false;
        while (!salir) {
            System.out.println("""
                
                ╔══════════════════════════════╗
                ║       GESTIÓN DE PRODUCTOS   ║
                ╠══════════════════════════════╣
                ║  1. Agregar producto          ║
                ║  2. Listar todos              ║
                ║  3. Listar disponibles        ║
                ║  4. Listar por categoría      ║
                ║  5. Buscar por ID             ║
                ║  6. Actualizar producto       ║
                ║  7. Eliminar producto         ║
                ║  0. Volver al menú principal  ║
                ╚══════════════════════════════╝
                Opción: """);

            String opcion = scanner.nextLine().trim();
            System.out.println();

            try {
                switch (opcion) {
                    case "1" -> agregarProducto();
                    case "2" -> listarProductos();
                    case "3" -> listarDisponibles();
                    case "4" -> listarPorCategoria();
                    case "5" -> buscarPorId();
                    case "6" -> actualizarProducto();
                    case "7" -> eliminarProducto();
                    case "0" -> salir = true;
                    default  -> System.out.println("⚠  Opción no válida. Intente de nuevo.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("⚠  " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("✖  Error de base de datos: " + e.getMessage());
            }
        }
    }

    // ── Operaciones ────────────────────────────────────────────────────────────

    private void agregarProducto() throws SQLException {
        System.out.println("── Agregar nuevo producto ─────────────");
        System.out.print("Nombre      : "); String nombre      = scanner.nextLine();
        System.out.print("Descripción : "); String descripcion = scanner.nextLine();

        double precio = leerPrecio();
        Categoria cat = leerCategoria();

        boolean ok = productoService.agregarProducto(nombre, descripcion, precio, cat);
        System.out.println(ok ? "✔  Producto agregado correctamente." : "✖  No se pudo agregar el producto.");
    }

    private void listarProductos() throws SQLException {
        List<Producto> lista = productoService.listarProductos();
        if (lista.isEmpty()) System.out.println("ℹ  No hay productos registrados.");
        else {
            System.out.println("── Listado de productos (" + lista.size() + ") ──");
            lista.forEach(System.out::println);
        }
    }

    private void listarDisponibles() throws SQLException {
        List<Producto> lista = productoService.listarDisponibles();
        if (lista.isEmpty()) System.out.println("ℹ  No hay productos disponibles.");
        else {
            System.out.println("── Productos disponibles (" + lista.size() + ") ──");
            lista.forEach(System.out::println);
        }
    }

    private void listarPorCategoria() throws SQLException {
        Categoria cat = leerCategoria();
        List<Producto> lista = productoService.listarPorCategoria(cat);
        if (lista.isEmpty()) System.out.println("ℹ  No hay productos en esa categoría.");
        else lista.forEach(System.out::println);
    }

    private void buscarPorId() throws SQLException {
        System.out.print("ID del producto: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        Producto p = productoService.buscarProductoPorId(id);
        if (p != null) System.out.println(p);
        else System.out.println("ℹ  No se encontró un producto con ID " + id + ".");
    }

    private void actualizarProducto() throws SQLException {
        System.out.print("ID del producto a actualizar: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        Producto actual = productoService.buscarProductoPorId(id);
        if (actual == null) { System.out.println("ℹ  Producto no encontrado."); return; }
        System.out.println("Datos actuales:\n" + actual);

        System.out.println("Ingrese los nuevos datos (Enter para dejar igual):");

        System.out.print("Nombre      [" + actual.getNombre()      + "]: ");
        String nombre = scanner.nextLine();
        if (nombre.isBlank()) nombre = actual.getNombre();

        System.out.print("Descripción [" + actual.getDescripcion() + "]: ");
        String desc = scanner.nextLine();
        if (desc.isBlank()) desc = actual.getDescripcion();

        System.out.print("Precio      [" + actual.getPrecio()      + "]: ");
        String precioStr = scanner.nextLine().trim();
        double precio = precioStr.isBlank() ? actual.getPrecio() : Double.parseDouble(precioStr);

        System.out.print("Categoría   [" + actual.getCategoria()   + "] (Enter para mantener): ");
        String catStr = scanner.nextLine().trim();
        Categoria cat = catStr.isBlank() ? actual.getCategoria() : Categoria.valueOf(catStr.toUpperCase());

        System.out.print("Disponible  [" + (actual.isDisponible() ? "s" : "n") + "] (s/n): ");
        String dispStr = scanner.nextLine().trim().toLowerCase();
        boolean disponible = dispStr.isBlank() ? actual.isDisponible() : dispStr.equals("s");

        boolean ok = productoService.actualizarProducto(id, nombre, desc, precio, cat, disponible);
        System.out.println(ok ? "✔  Producto actualizado." : "✖  No se pudo actualizar.");
    }

    private void eliminarProducto() throws SQLException {
        System.out.print("ID del producto a eliminar: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("¿Confirmar eliminación? (s/n): ");
        String conf = scanner.nextLine().trim().toLowerCase();
        if (conf.equals("s")) {
            boolean ok = productoService.eliminarProducto(id);
            System.out.println(ok ? "✔  Producto eliminado." : "✖  No se pudo eliminar.");
        } else {
            System.out.println("ℹ  Operación cancelada.");
        }
    }

    // ── Helpers de entrada ─────────────────────────────────────────────────────

    private double leerPrecio() {
        while (true) {
            System.out.print("Precio      : ");
            try {
                double p = Double.parseDouble(scanner.nextLine().trim());
                if (p > 0) return p;
                System.out.println("⚠  El precio debe ser mayor a 0.");
            } catch (NumberFormatException e) {
                System.out.println("⚠  Ingrese un número válido.");
            }
        }
    }

    private Categoria leerCategoria() {
        while (true) {
            System.out.println("Categorías: 1=HAMBURGUESA  2=PIZZA  3=HOT_DOG  4=SALCHIPAPA");
            System.out.print("Opción     : ");
            String op = scanner.nextLine().trim();
            switch (op) {
                case "1" -> { return Categoria.HAMBURGUESA; }
                case "2" -> { return Categoria.PIZZA; }
                case "3" -> { return Categoria.HOT_DOG; }
                case "4" -> { return Categoria.SALCHIPAPA; }
                default  -> System.out.println("⚠  Seleccione una opción válida (1-4).");
            }
        }
    }
}
