package br.com.desafioglobo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.desafioglobo.domain.Contato;

public interface IContatoService extends ICRUDService<Contato> {
	
	Page<Contato> pesquisarByNome(String nome, Pageable pageable);

}
