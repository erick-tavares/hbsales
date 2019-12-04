package br.com.hbsis.linhacategoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILinhaCategoriaRepository extends JpaRepository<LinhaCategoria, Long> {
}
