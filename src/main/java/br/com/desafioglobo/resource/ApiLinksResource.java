package br.com.desafioglobo.resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceSupport;

import br.com.desafioglobo.controller.ContatoController;

public class ApiLinksResource extends ResourceSupport {

	public ApiLinksResource() {
		this.add(linkTo(ContatoController.class).withRel("contatos"));
		this.add(linkTo(methodOn(ContatoController.class).pesquisarByNome(null, null)).withRel("search"));
	}
	
}