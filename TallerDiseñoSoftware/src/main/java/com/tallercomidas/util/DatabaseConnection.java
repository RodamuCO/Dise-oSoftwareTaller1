package com.tallercomidas.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase utilitaria para gestionar la conexión a la base de datos SQLite.
 * Implementa el patrón Singleton para garantizar una única instancia de conexión.
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:tallercomidas.db";
    private static Connection instance;

    private DatabaseConnection() {}

    /**
     * Retorna la instancia única de la conexión a la base de datos.
     */
    public static Connection getInstance() throws SQLException {
        if (instance == null || instance.isClosed()) {
            instance = DriverManager.getConnection(URL);
        }
        return instance;
    }

    /**
     * Inicializa las tablas de la base de datos si no existen.
     */
    public static void inicializarBaseDatos() throws SQLException {
        Connection conn = getInstance();
        Statement stmt = conn.createStatement();

        // Tabla de clientes
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS clientes (
                id          INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre      TEXT    NOT NULL,
                email       TEXT    NOT NULL UNIQUE,
                telefono    TEXT    NOT NULL,
                direccion   TEXT    NOT NULL
            )
        """);

        // Tabla de productos
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS productos (
                id          INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre      TEXT    NOT NULL,
                descripcion TEXT    NOT NULL,
                precio      REAL    NOT NULL,
                categoria   TEXT    NOT NULL,
                disponible  INTEGER NOT NULL DEFAULT 1
            )
        """);

        stmt.close();
        System.out.println("✔  Base de datos inicializada correctamente.");
    }

    /**
     * Cierra la conexión a la base de datos.
     */
    public static void cerrarConexion() {
        try {
            if (instance != null && !instance.isClosed()) {
                instance.close();
                System.out.println("✔  Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
