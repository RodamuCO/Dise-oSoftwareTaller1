package com.tallercomidas.model;

/**
 * Clase modelo que representa un Producto del menú de la empresa de comidas rápidas.
 */
public class Producto {

    /** Categorías de productos disponibles en el sistema. */
    public enum Categoria {
        HAMBURGUESA,
        PIZZA,
        HOT_DOG,
        SALCHIPAPA;

        /** Devuelve la categoría formateada para mostrar en consola. */
        public String etiqueta() {
            return switch (this) {
                case HAMBURGUESA -> "🍔 Hamburguesa";
                case PIZZA       -> "🍕 Pizza";
                case HOT_DOG     -> "🌭 Hot Dog";
                case SALCHIPAPA  -> "🍟 Salchipapa";
            };
        }
    }

    private int       id;
    private String    nombre;
    private String    descripcion;
    private double    precio;
    private Categoria categoria;
    private boolean   disponible;

    // Constructores

    /** Constructor para crear un producto nuevo (sin ID). */
    public Producto(String nombre, String descripcion, double precio, Categoria categoria) {
        this.nombre      = nombre;
        this.descripcion = descripcion;
        this.precio      = precio;
        this.categoria   = categoria;
        this.disponible  = true;
    }

    /** Constructor completo (usado al recuperar registros de la BD). */
    public Producto(int id, String nombre, String descripcion,
                    double precio, Categoria categoria, boolean disponible) {
        this.id          = id;
        this.nombre      = nombre;
        this.descripcion = descripcion;
        this.precio      = precio;
        this.categoria   = categoria;
        this.disponible  = disponible;
    }

    // Getters y Setters
    public int getId()                       { return id; }
    public void setId(int id)                { this.id = id; }

    public String getNombre()                { return nombre; }
    public void setNombre(String n)          { this.nombre = n; }

    public String getDescripcion()           { return descripcion; }
    public void setDescripcion(String d)     { this.descripcion = d; }

    public double getPrecio()                { return precio; }
    public void setPrecio(double p)          { this.precio = p; }

    public Categoria getCategoria()          { return categoria; }
    public void setCategoria(Categoria c)    { this.categoria = c; }

    public boolean isDisponible()            { return disponible; }
    public void setDisponible(boolean disp)  { this.disponible = disp; }

    // toString

    @Override
    public String toString() {
        return String.format(
            "┌─ Producto #%d ─────────────────────────\n" +
            "│  Nombre     : %s\n" +
            "│  Descripción: %s\n" +
            "│  Precio     : $%.2f\n" +
            "│  Categoría  : %s\n" +
            "│  Disponible : %s\n" +
            "└───────────────────────────────────────",
            id, nombre, descripcion, precio,
            categoria.etiqueta(),
            disponible ? "Sí" : "No"
        );
    }
}
