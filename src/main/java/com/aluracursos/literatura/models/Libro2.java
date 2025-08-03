package com.aluracursos.literatura.models;

import jakarta.persistence.*;

import java.util.*;
import java.util.stream.Collectors;


@Entity
@Table(name = "libro2")
public class Libro2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,nullable = false)
    private String titulo;


    private Long numeroDeDescargas;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinTable(
            name ="libros_autores",
            joinColumns = @JoinColumn (name="libro_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id",referencedColumnName = "id"),
            uniqueConstraints =@UniqueConstraint(columnNames = {"libro_id","autor_id"})

    )
    private Set<Autor2> autores = new HashSet<>();


    @ManyToMany( cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name ="libros_idiomas",
            joinColumns = @JoinColumn (name="libro_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "idioma_id",referencedColumnName = "id"),
    uniqueConstraints =@UniqueConstraint(columnNames = {"libro_id","idioma_id"})

    )
    private Set<Idiomas> idiomas = new HashSet<>();


    public Libro2() {
    }

    public Libro2(String titulo, Long numeroDeDescargas) {
        this.titulo = titulo;
        this.numeroDeDescargas = numeroDeDescargas;
    }


    public Libro2(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.numeroDeDescargas = datosLibro.numeroDeDescargas();

    }
    public Long getNumeroDeDescargas() {
        return numeroDeDescargas;
    }
    public Long getId() {
        return id;
    }
    public Set<Idiomas> getIdiomas() {
        return idiomas;
    }
    public Set<Autor2> getAutores() {
        return autores;
    }
    public String getTitulo() {
        return titulo;
    }



    public void setIdiomas(Set<Idiomas> idiomas) {
        this.idiomas = idiomas;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setAutores(Set<Autor2> autores) {
        autores.forEach(a->a.setLibro(this));
        this.autores = autores;

    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setNumeroDeDescargas(Long numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }
    public void setAutor(Autor2 autor2) {
        this.autores.add(autor2);
    }

    @Override
    public String toString() {
        return "---------------Libro-------------\n" +
                "Titulo: " + titulo + '\n' +
                "Autor(es): " +autores.stream().map(a->a.getnombre()).collect(Collectors.toSet()) +'\n'+
                "Idioma(s): " + idiomas.stream().map(i->i.getIdioma()).collect(Collectors.toSet())+'\n' +
                "NumeroDeDescargas: " + numeroDeDescargas+'\n' +
                "--------------------------------";
    }

    @Override
    public boolean equals(Object o) {

        if (this==o) return  true;
        if(!(o instanceof Libro2)) return false;
        Libro2 libro2 = (Libro2) o;
        return Objects.equals(id,libro2.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


