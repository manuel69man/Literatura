package com.aluracursos.literatura.models;

import jakarta.persistence.*;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private  Integer anoDeNacimiento;
    private Integer anoDeMuerte;
    @ManyToOne
    private Libro libro;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }
/*
    @OneToMany(mappedBy = "autores", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> librosPublicados;
*/

    public Autor(String nombre, Integer anoDeNacimiento, Integer anoDeMuerte/*, List<Libro> librosPublicados*/) {
        this.nombre = nombre;
        this.anoDeNacimiento = anoDeNacimiento;
        this.anoDeMuerte = anoDeMuerte;
    //    this.librosPublicados = librosPublicados;
    }

    public Autor() {
    }

    public String getnombre() {
        return nombre;
    }

    public void setnombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getAnoDeNacimiento() {
        return anoDeNacimiento;
    }

    public void setAnoDeNacimiento(Integer anoDeNacimiento) {
        this.anoDeNacimiento = anoDeNacimiento;
    }

    public Integer getAnoDeMuerte() {
        return anoDeMuerte;
    }

    public void setAnoDeMuerte(Integer anoDeMuerte) {
        this.anoDeMuerte = anoDeMuerte;
    }

}
