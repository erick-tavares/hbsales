package br.com.hbsis.linhacategoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ILinhaCategoriaRepository extends JpaRepository<LinhaCategoria, Long> {
    Optional<LinhaCategoria> findByCodigo(String codigo);
}
