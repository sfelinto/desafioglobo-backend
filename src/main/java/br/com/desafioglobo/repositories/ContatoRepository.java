package br.com.desafioglobo.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import br.com.desafioglobo.domain.Contato;
import br.com.desafioglobo.domain.ContatoFiltro;

@RepositoryRestResource
public interface ContatoRepository extends JpaRepository<Contato, Integer> {
	
	Page<Contato> findByNome(ContatoFiltro contatoFiltro, Pageable pageable);

}
