package com.aluracursos.literatura.models;

import java.util.List;

public record DatosLibroDTO(
        String titulo,
        Long numeroDeDescargas,
        List<DatosAutor> autores
) {
}
