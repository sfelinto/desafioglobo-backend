package br.com.desafioglobo;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.desafioglobo.controller.ContatoController;
import br.com.desafioglobo.domain.Contato;
import br.com.desafioglobo.resource.ContatoResource;
import br.com.desafioglobo.service.ContatoServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DesafioGloboApiApplication.class)
@WebAppConfiguration
public class ContatoControllerTest {
	
	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets/index/v1");
	
//	@Mock
//    private ContatoServiceImpl contatoService;

    @InjectMocks
    private ContatoController contatoController;
	
	private MockMvc mockMvc;
	
	@SuppressWarnings("rawtypes")
	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			  MediaType.APPLICATION_JSON.getSubtype(),
			  Charset.forName("utf8"));
	
	@Autowired
    private WebApplicationContext context;
	
	@MockBean
	private ContatoServiceImpl contatoService;

	private RestDocumentationResultHandler document;

	@Before
	public void setup() throws Exception {
		
		// Prepare Spring Restdocs with default directory and preprocessors
		this.document = document("{class-name}/{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
						.apply(documentationConfiguration(this.restDocumentation))
						.alwaysDo(MockMvcResultHandlers.print())
						.build();
	}
	
	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {

		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().orElse(null);

		assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
		
	}
	
	@SuppressWarnings("unchecked")
	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
	
	
	@Test
	public void contextLoads() throws Exception {}
	
	@Test
	public void controllerLink() throws Exception {
		
		Link link = linkTo(ContatoController.class).withRel("contatos");
		assertThat(link.getRel(), is("contatos"));
		assertThat(link.getHref(), endsWith("/contatos"));
		
	}
	
	@Test
	public void sitesApiURI() throws Exception {
		Contato contato = new Contato();
		contato.setId(1);
		ContatoResource contatoResource = ContatoResource.ofEntity(contato);
		Link link = contatoResource.getId();
		
		assertThat(link.getRel(), is(Link.REL_SELF ));
		assertThat(link.getHref(), endsWith("/contatos/1"));
	}
	
	@Test
    public void testList() throws Exception {
		
		Contato contato1 = new Contato();
	    contato1.setNome("Sergio");
	    contato1.setCanal("Telefone");
	    contato1.setValor("2199999999");
	    contato1.setObservacao("Contato com Telefone Celular");

	    Contato contato2 = new Contato();
	    contato2.setNome("Henrique");
	    contato2.setCanal("Telefone");
	    contato2.setValor("2199999999");
	    contato2.setObservacao("Contato com Telefone Celular");
		
        List<Contato> contatos = new ArrayList<>();
        contatos.add(contato1);
        contatos.add(contato2);

        //when(contatoService.listarTodos()).thenReturn((List) contatos);
        given(this.contatoService.listarTodos()).willReturn(contatos);
        
       //REST API assert
  		mockMvc.perform(get("/contatos"))
  				.andExpect(status().isOk())
  				.andExpect(content().contentType(contentType))
  				.andExpect(jsonPath("_embedded.contatoResources", hasSize(2)))
  				.andExpect(jsonPath("_embedded.contatoResources[0].contato.nome", is(contato1.getNome())))
  				.andExpect(jsonPath("_embedded.contatoResources[0].contato.canal", is(contato1.getCanal())))
  				.andExpect(jsonPath("_embedded.contatoResources[0].contato.valor", is(contato1.getValor())))
  				.andExpect(jsonPath("_embedded.contatoResources[0].contato.observacao", is(contato1.getObservacao())))
  				.andExpect(jsonPath("_embedded.contatoResources[1].contato.nome", is(contato2.getNome())))
  				.andExpect(jsonPath("_embedded.contatoResources[1].contato.canal", is(contato2.getCanal())))
  				.andExpect(jsonPath("_embedded.contatoResources[1].contato.valor", is(contato2.getValor())))
  				.andExpect(jsonPath("_embedded.contatoResources[1].contato.observacao", is(contato2.getObservacao())));
    }
	
	@Test
	public void getContatoById() throws Exception {
		
		Integer contatoId = 1;
		
		Contato contato1 = new Contato();
		contato1.setId(contatoId);
	    contato1.setNome("Sergio");
	    contato1.setCanal("Telefone");
	    contato1.setValor("2199999999");
	    contato1.setObservacao("Contato com Telefone Celular");

		 given(this.contatoService.getById(contatoId)).willReturn(contato1);
		
		//REST API assert
		this.mockMvc.perform(get("/api/v1/contatos/{id}", contatoId).contextPath("/api/v1")
				.contentType(contentType))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("contato.nome", is("Sergio")))
				.andExpect(jsonPath("contato.canal", is("Telefone")))
				.andExpect(jsonPath("contato.valor", is("2199999999")))
				.andExpect(jsonPath("contato.observacao", is("Contato com Telefone Celular")))
				
				// Request specific restdocs configuration
				.andDo(this.document.document(pathParameters(
												parameterWithName("id").description("Contato ID"))
											,responseFields(
												fieldWithPath("contato.nome").description("Nome do Contato"),
												fieldWithPath("contato.canal").description("Canal de Comunicacao com o Contato"),
												fieldWithPath("contato.valor").description("Tipo de Comunicacao associada ao Contato"),
												fieldWithPath("contato.observacao").description("Observacoes"),
												fieldWithPath("_links").type(JsonFieldType.OBJECT).description("Contatos URI´s"),
												fieldWithPath("_links.self.href").description("Contato self reference URI"),
												fieldWithPath("_links.search.templated").ignored(),
												fieldWithPath("_links.search.href").description("Link de Pesquisa dos Contatos"))));
		
	}
}
