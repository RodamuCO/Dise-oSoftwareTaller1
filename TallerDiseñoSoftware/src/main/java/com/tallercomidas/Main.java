package com.tallercomidas;

import com.tallercomidas.menu.ClienteMenu;
import com.tallercomidas.menu.ProductoMenu;
import com.tallercomidas.util.DatabaseConnection;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("""
            ╔═══════════════════════════════════════════════════╗
            ║       SISTEMA DE GESTIÓN – COMIDAS RÁPIDAS        ║
            ║              Taller Métricas de Software          ║
            ╚═══════════════════════════════════════════════════╝
            """);

        try {
            DatabaseConnection.inicializarBaseDatos();
        } catch (SQLException e) {
            System.err.println("✖  No se pudo inicializar la base de datos: " + e.getMessage());
            System.err.println("   Asegúrese de que el driver sqlite-jdbc.jar esté en el classpath.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        ClienteMenu  clienteMenu  = new ClienteMenu(scanner);
        ProductoMenu productoMenu = new ProductoMenu(scanner);

        boolean salir = false;
        while (!salir) {
            System.out.println("""
                
                ╔══════════════════════════════════╗
                ║         MENÚ PRINCIPAL            ║
                ╠══════════════════════════════════╣
                ║  1. Gestión de Clientes           ║
                ║  2. Gestión de Productos          ║
                ║  0. Salir                         ║
                ╚══════════════════════════════════╝
                Opción: """);

            String opcion = scanner.nextLine().trim();
            System.out.println();

            switch (opcion) {
                case "1" -> clienteMenu.mostrarMenu();
                case "2" -> productoMenu.mostrarMenu();
                case "0" -> salir = true;
                default  -> System.out.println("⚠  Opción no válida.");
            }
        }

        scanner.close();
        DatabaseConnection.cerrarConexion();
        System.out.println("\n¡Hasta luego! 👋");
    }
}
