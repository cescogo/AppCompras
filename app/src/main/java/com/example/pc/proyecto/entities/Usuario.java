package com.example.pc.proyecto.entities;

/**
 * Created by lobo on 11/04/18.
 */

public class Usuario {
    
    private int id;
    private String nombre;
    private String pass;

    static public Usuario USUARIO = new Usuario();

    public Usuario(){
        id = 0;
        nombre = "";
        pass = "";
    }

    public Usuario(int id, String nombre, String pass) {
        this.id = id;
        this.nombre = nombre;
        this.pass = pass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
