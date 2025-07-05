package com.aluracursos.literatura.models;

import com.fasterxml.jackson.annotation.JsonAlias;

public record DatosAutor(
        @JsonAlias("name")  String nombre,
        @JsonAlias("birth_year")  Integer anoDeNacimiento,
        @JsonAlias("death_year") Integer anoDeMuerte) {
}
