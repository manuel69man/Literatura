package com.aluracursos.literatura.repository;

import com.aluracursos.literatura.models.Libro2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Libro2Repository extends JpaRepository <Libro2,Long> {
  Optional<Libro2> findByTitulo(String titulo);
  @Query (value="select titulo from libro2 inner join libros_idiomas on libro2.id=libros_idiomas.libro_id where libros_idiomas.idioma_id= :idiomaId",nativeQuery = true)
  List<String> findAllByLanguage(@Param("idiomaId") Long idiomaId);
}
