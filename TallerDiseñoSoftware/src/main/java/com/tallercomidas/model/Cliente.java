package com.tallercomidas.model;

/**
 * Clase modelo que representa un Cliente de la empresa de comidas rápidas.
 * Aplica encapsulamiento mediante atributos privados y métodos getter/setter.
 */
public class Cliente {

    private int    id;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;

    // ── Constructores ──────────────────────────────────────────────────────────

    /** Constructor para crear un cliente nuevo (sin ID, lo asigna la BD). */
    public Cliente(String nombre, String email, String telefono, String direccion) {
        this.nombre    = nombre;
        this.email     = email;
        this.telefono  = telefono;
        this.direccion = direccion;
    }

    /** Constructor completo (usado al recuperar registros de la BD). */
    public Cliente(int id, String nombre, String email, String telefono, String direccion) {
        this.id        = id;
        this.nombre    = nombre;
        this.email     = email;
        this.telefono  = telefono;
        this.direccion = direccion;
    }

    // ── Getters y Setters ──────────────────────────────────────────────────────

    public int getId()                  { return id; }
    public void setId(int id)           { this.id = id; }

    public String getNombre()           { return nombre; }
    public void setNombre(String n)     { this.nombre = n; }

    public String getEmail()            { return email; }
    public void setEmail(String e)      { this.email = e; }

    public String getTelefono()         { return telefono; }
    public void setTelefono(String t)   { this.telefono = t; }

    public String getDireccion()        { return direccion; }
    public void setDireccion(String d)  { this.direccion = d; }

    // ── toString ───────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return String.format(
            "┌─ Cliente #%d ─────────────────────────\n" +
            "│  Nombre   : %s\n" +
            "│  Email    : %s\n" +
            "│  Teléfono : %s\n" +
            "│  Dirección: %s\n" +
            "└───────────────────────────────────────",
            id, nombre, email, telefono, direccion
        );
    }
}
