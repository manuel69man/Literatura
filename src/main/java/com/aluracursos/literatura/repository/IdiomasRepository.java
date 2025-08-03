package com.aluracursos.literatura.repository;

import com.aluracursos.literatura.models.Idiomas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdiomasRepository extends JpaRepository<Idiomas,Long> {

    Optional<Idiomas> findByIdioma(String idioma);
}
