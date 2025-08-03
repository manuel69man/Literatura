package com.aluracursos.literatura.repository;

import com.aluracursos.literatura.models.Autor2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Autor2Repository extends JpaRepository<Autor2,Long> {
    Optional<Autor2> findByNombre(String getnombre);
}
