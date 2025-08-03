package com.aluracursos.literatura.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "idiomas")
public class Idiomas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String idioma;

    @ManyToMany(mappedBy = "idiomas")
    private Set<Libro2> libros;


    public Set<Libro2> getLibros() {
        return libros;
    }

    public void setLibros(Set<Libro2> libros) {
        this.libros = libros;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Idiomas() {
    }

    public Idiomas(String idioma) {
        this.idioma = idioma;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    @Override
    public String toString() {
        return getIdioma();
    }
}

