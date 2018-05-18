package com.example.pc.proyecto.entities;

import java.io.Serializable;

/**
 * Created by pc on 28/3/2018.
 */

public class Producto implements Serializable {
    String nombre,categoria,foto;
    int precio;
    private static Producto instance = null;

    public Producto(String nombre, String categoria, int precio) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
    }
    public static Producto getInstance() {
        if(instance == null) {instance = new Producto(); }
        return instance;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Producto() {
    }
}
