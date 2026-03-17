package com.tallercomidas.menu;

import com.tallercomidas.model.Cliente;
import com.tallercomidas.service.ClienteService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Menú de consola para la gestión de Clientes.
 */
public class ClienteMenu {

    private final ClienteService clienteService;
    private final Scanner scanner;

    public ClienteMenu(Scanner scanner) {
        this.clienteService = new ClienteService();
        this.scanner = scanner;
    }

    /** Muestra el submenú de clientes y procesa la opción elegida. */
    public void mostrarMenu() {
        boolean salir = false;
        while (!salir) {
            System.out.println("""
                
                ╔══════════════════════════════╗
                ║       GESTIÓN DE CLIENTES    ║
                ╠══════════════════════════════╣
                ║  1. Agregar cliente           ║
                ║  2. Listar todos              ║
                ║  3. Buscar por ID             ║
                ║  4. Buscar por nombre         ║
                ║  5. Actualizar cliente        ║
                ║  6. Eliminar cliente          ║
                ║  0. Volver al menú principal  ║
                ╚══════════════════════════════╝
                Opción: """);

            String opcion = scanner.nextLine().trim();
            System.out.println();

            try {
                switch (opcion) {
                    case "1" -> agregarCliente();
                    case "2" -> listarClientes();
                    case "3" -> buscarPorId();
                    case "4" -> buscarPorNombre();
                    case "5" -> actualizarCliente();
                    case "6" -> eliminarCliente();
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

    // Operaciones

    private void agregarCliente() throws SQLException {
        System.out.println("── Agregar nuevo cliente ──────────────");
        System.out.print("Nombre    : "); String nombre    = scanner.nextLine();
        System.out.print("Email     : "); String email     = scanner.nextLine();
        System.out.print("Teléfono  : "); String telefono  = scanner.nextLine();
        System.out.print("Dirección : "); String direccion = scanner.nextLine();

        boolean ok = clienteService.agregarCliente(nombre, email, telefono, direccion);
        System.out.println(ok ? "✔  Cliente agregado correctamente." : "✖  No se pudo agregar el cliente.");
    }

    private void listarClientes() throws SQLException {
        List<Cliente> lista = clienteService.listarClientes();
        if (lista.isEmpty()) {
            System.out.println("ℹ  No hay clientes registrados.");
        } else {
            System.out.println("── Listado de clientes (" + lista.size() + ") ──");
            lista.forEach(System.out::println);
        }
    }

    private void buscarPorId() throws SQLException {
        System.out.print("ID del cliente: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        Cliente c = clienteService.buscarClientePorId(id);
        if (c != null) System.out.println(c);
        else System.out.println("ℹ  No se encontró un cliente con ID " + id + ".");
    }

    private void buscarPorNombre() throws SQLException {
        System.out.print("Nombre a buscar: ");
        String nombre = scanner.nextLine();
        List<Cliente> resultado = clienteService.buscarPorNombre(nombre);
        if (resultado.isEmpty()) {
            System.out.println("ℹ  No se encontraron clientes con ese nombre.");
        } else {
            resultado.forEach(System.out::println);
        }
    }

    private void actualizarCliente() throws SQLException {
        System.out.print("ID del cliente a actualizar: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        Cliente actual = clienteService.buscarClientePorId(id);
        if (actual == null) { System.out.println("ℹ  Cliente no encontrado."); return; }
        System.out.println("Datos actuales:\n" + actual);

        System.out.println("Ingrese los nuevos datos (Enter para dejar igual):");
        System.out.print("Nombre    [" + actual.getNombre()    + "]: ");
        String nombre = scanner.nextLine();
        if (nombre.isBlank()) nombre = actual.getNombre();

        System.out.print("Email     [" + actual.getEmail()     + "]: ");
        String email = scanner.nextLine();
        if (email.isBlank()) email = actual.getEmail();

        System.out.print("Teléfono  [" + actual.getTelefono()  + "]: ");
        String telefono = scanner.nextLine();
        if (telefono.isBlank()) telefono = actual.getTelefono();

        System.out.print("Dirección [" + actual.getDireccion() + "]: ");
        String direccion = scanner.nextLine();
        if (direccion.isBlank()) direccion = actual.getDireccion();

        boolean ok = clienteService.actualizarCliente(id, nombre, email, telefono, direccion);
        System.out.println(ok ? "✔  Cliente actualizado." : "✖  No se pudo actualizar.");
    }

    private void eliminarCliente() throws SQLException {
        System.out.print("ID del cliente a eliminar: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("¿Confirmar eliminación? (s/n): ");
        String conf = scanner.nextLine().trim().toLowerCase();
        if (conf.equals("s")) {
            boolean ok = clienteService.eliminarCliente(id);
            System.out.println(ok ? "✔  Cliente eliminado." : "✖  No se pudo eliminar.");
        } else {
            System.out.println("ℹ  Operación cancelada.");
        }
    }
}
