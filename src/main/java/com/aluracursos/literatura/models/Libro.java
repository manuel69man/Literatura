package com.aluracursos.literatura.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Idiomas> idiomas;
    private Long numeroDeDescargas;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Autor> autores;

    public Libro() {
    }

    public Libro(String titulo, Long numeroDeDescargas) {
        this.titulo = titulo;
        this.numeroDeDescargas = numeroDeDescargas;
    }


    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.numeroDeDescargas = datosLibro.numeroDeDescargas();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        autores.forEach(a->a.setLibro(this));
        this.autores = autores;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }



    public List<Idiomas> getidiomas() {
        return  idiomas;
    }

    public void setidiomas(List<Idiomas>  idiomas) {
        idiomas.forEach(i->i.setLibro(this));
        this.idiomas = idiomas;
    }

    public Long getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Long numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    @Override
    public String toString() {
        String listaDeAutores= autores.stream().map(Autor::getnombre).collect(Collectors.joining());
        String listaDeIdiomas = idiomas.stream().map(Idiomas::getIdioma).toList()
                .stream().distinct().collect(Collectors.joining());
        return "---------------Libro-------------\n" +
                "Titulo: " + titulo + '\n' +
                "Autores: " + listaDeAutores + '\n' +
                "Idiomas=: " + listaDeIdiomas+ '\n' +
                "NumeroDeDescargas: " + numeroDeDescargas+'\n' +
                "--------------------------------";
    }
}
