package com.example.lab03.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sede")
public class Sedes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int idsede;

    @Column(name = "nombre", nullable = false)
    private String nombresede;

    @Column(name = "direccion")
    private String direccionsede;

    @Column(name = "telefono")
    private String telefonosede;

    public int getIdsede() { return idsede; }

    public void setIdsede(int idsede) {
        this.idsede = idsede;
    }

    public String getNombresede() {
        return nombresede;
    }

    public void setNombresede(String nombresede) {
        this.nombresede = nombresede;
    }

    public String getDireccionsede() {
        return direccionsede;
    }

    public void setDireccionsede(String direccionsede) {
        this.direccionsede = direccionsede;
    }

    public String getTelefonosede() {
        return telefonosede;
    }

    public void setTelefonosede(String telefonosede) {
        this.telefonosede = telefonosede;
    }
}

