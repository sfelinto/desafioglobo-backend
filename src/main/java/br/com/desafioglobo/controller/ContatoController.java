package br.com.desafioglobo.controller;

import static java.util.stream.Collectors.toList;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.desafioglobo.domain.Contato;
import br.com.desafioglobo.resource.ContatoResource;
import br.com.desafioglobo.service.IContatoService;

@RestController
@RequestMapping("/contatos")
public class ContatoController {
	
	private IContatoService contatoService;
	
	@Autowired
	public void setContatoService(IContatoService contatoService) {
		this.contatoService = contatoService;
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resources<ContatoResource>> listarTodos() {
		return new ResponseEntity<Resources<ContatoResource>>(
				new Resources<ContatoResource>(contatoService.listarTodos()
													.stream()
													.map(contato -> ContatoResource.ofEntity((Contato) contato))
													.collect(toList())),
				HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ContatoResource> getContatoById(@PathVariable Integer id) {
		return new ResponseEntity<ContatoResource>(
				ContatoResource.ofEntity(contatoService.getById(id)),
				HttpStatus.OK);
	}
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ContatoResource> createContato(@RequestBody Contato contato) {
		return new ResponseEntity<ContatoResource>(
				ContatoResource.ofEntity(contatoService.save(contato)),
				HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ContatoResource> updateContato(@PathVariable Integer id, @Valid @RequestBody Contato contato) {
		Contato contatoAtualizado = contatoService.update(id, contato);
		return new ResponseEntity<ContatoResource>(
				ContatoResource.ofEntity(contatoAtualizado),
				HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteContato(@PathVariable Integer id) {
		contatoService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/search")
	public Page<Contato> pesquisarByNome(@RequestParam(value = "nome") String nome, Pageable pageable)  {
		return contatoService.pesquisarByNome(nome, pageable);
	}
}
