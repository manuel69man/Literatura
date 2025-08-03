package com.aluracursos.literatura.models;

import jakarta.persistence.*;

import java.util.*;
import java.util.stream.Collectors;


@Entity
@Table(name = "autor2")
public class Autor2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nombre="";
    private  Integer anoDeNacimiento;
    private Integer anoDeMuerte;
    @ManyToMany(mappedBy = "autores")
    private Set<Libro2> libros = new HashSet<>();

    public Autor2() {
    }

    public Autor2(String nombre, Integer anoDeNacimiento, Integer anoDeMuerte) {
        this.nombre = nombre;
        this.anoDeNacimiento = anoDeNacimiento;
        this.anoDeMuerte = anoDeMuerte;
    }

    public Long getId() {
        return id;
    }
    public Set<Libro2> getLibros() {
        return libros;
    }
    public String getnombre() {
        return nombre;
    }
    public Integer getAnoDeNacimiento() {
        return anoDeNacimiento;
    }
    public Integer getAnoDeMuerte() {
        return anoDeMuerte;
    }
    public void setnombre(String nombre) {
        this.nombre = nombre;
    }
    public void setAnoDeNacimiento(Integer anoDeNacimiento) {
        this.anoDeNacimiento = anoDeNacimiento;
    }
    public void setAnoDeMuerte(Integer anoDeMuerte) {
        this.anoDeMuerte = anoDeMuerte;
    }
    public void setLibros(Set<Libro2> libros) {
        libros.forEach(l->l.setAutor(this));
        this.libros = libros;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setLibro(Libro2 libro2) {
        this.libros.add(libro2);
    }

    @Override
    public String toString() {
        if (this != null) {

            return "Autor: " + nombre + '\n' +
                    "Año de Nacimiento: " + anoDeNacimiento + '\n' +
                    "Año de Muerte: " + anoDeMuerte + '\n'+ //+
                    "Libros Publicados: "+getLibros().stream().map(l->l.getTitulo()).collect(Collectors.toSet())+'\n'
                    ;
        } else return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this==o) return  true;
        if(!(o instanceof Autor2)) return false;
        Autor2 autor2 = (Autor2) o;
        return Objects.equals(id,autor2.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

