package br.com.desafioglobo.resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceSupport;

import br.com.desafioglobo.controller.ContatoController;
import br.com.desafioglobo.domain.Contato;

public class ContatoResource extends ResourceSupport {
	
	private Contato contato;
	
	private ContatoResource(Contato contato) {
		this.contato = contato;
		this.add(linkTo(ContatoController.class).slash(contato.getId()).withSelfRel());
		this.add(linkTo(methodOn(ContatoController.class).pesquisarByNome(null,null)).withRel("search"));
	}
	
	public static ContatoResource ofEntity(Contato contato) {
		return new ContatoResource(contato);
	}

	public Contato getContato() {
		return contato;
	}

}
