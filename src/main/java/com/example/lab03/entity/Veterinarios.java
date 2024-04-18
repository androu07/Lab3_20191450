package com.example.lab03.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "veterinario")
public class Veterinarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "correo")
    private String correo;

    @Column(name = "sedeid")
    private String sedeid;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getSedeid() {
        return sedeid;
    }

    public void setSedeid(String sedeid) { this.sedeid = sedeid; }
}
