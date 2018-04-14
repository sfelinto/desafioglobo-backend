package br.com.desafioglobo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import br.com.desafioglobo.domain.Contato;
import br.com.desafioglobo.service.IContatoService;

@Component
public class SpringJPABootstrap implements ApplicationListener<ContextRefreshedEvent> {
	
	private IContatoService contatoService;

	@Autowired
	public void setContatoService(IContatoService contatoService) {
		this.contatoService = contatoService;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		loadContatos();
	}
	
	private void loadContatos() {
		Contato contato1 = new Contato();
	    contato1.setNome("Sergio");
	    contato1.setCanal("Telefone");
	    contato1.setValor("2199999999");
	    contato1.setObservacao("Contato com Telefone Celular");
	    contatoService.save(contato1);

	    Contato contato2 = new Contato();
	    contato2.setNome("Henrique");
	    contato2.setCanal("Telefone");
	    contato2.setValor("2199999999");
	    contato2.setObservacao("Contato com Telefone Celular");
	    contatoService.save(contato2);

	    Contato contato3 = new Contato();
	    contato3.setNome("Sergio Silva");
	    contato3.setCanal("Email");
	    contato3.setValor("s@s.com");
	    contato3.setObservacao("Contato com E-mail");
	    contatoService.save(contato3);

	    Contato contato4 = new Contato();
	    contato4.setNome("Vasco da Gama");
	    contato4.setCanal("Telefone");
	    contato4.setValor("2133334444");
	    contato4.setObservacao("Contato com Telefone Fixo");
	    contatoService.save(contato4);

	    Contato contato5 = new Contato();
	    contato5.setNome("Thissiane");
	    contato5.setCanal("Telefone");
	    contato5.setValor("2199999999");
	    contato5.setObservacao("Contato com Telefone");
	    contatoService.save(contato5);

	    Contato contato6 = new Contato();
	    contato6.setNome("Brasil");
	    contato6.setCanal("Email");
	    contato6.setValor("br@br.com");
	    contato6.setObservacao("Contato com E-mail");
	    contatoService.save(contato6);
	}
	

}
