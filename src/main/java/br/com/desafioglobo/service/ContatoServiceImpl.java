package br.com.desafioglobo.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.com.desafioglobo.domain.Contato;
import br.com.desafioglobo.repositories.ContatoRepository;

@Service
public class ContatoServiceImpl implements IContatoService {
	
	private ContatoRepository contatoRepository;
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(ContatoServiceImpl.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	public void setContatoRepository(ContatoRepository contatoRepository) {
		this.contatoRepository = contatoRepository;
	}

	@Override
	public List<Contato> listarTodos() {
		log.debug("listarTodos Request:[{}]");
		List<Contato> contatos = new ArrayList<Contato>();
		this.contatoRepository.findAll().forEach(contatos::add);
		return contatos;
	}

	@Override
	public Contato getById(Integer id) {
		log.debug("getById Request:[{}]", id);
		return this.contatoRepository.getOne(id);
	}

	@Override
	public Contato save(Contato domainObject) {
		return this.contatoRepository.saveAndFlush(domainObject);
	}

	@Override
	public void delete(Integer id) {
		Contato contato = this.getById(id);
		this.contatoRepository.delete(contato);
	}

	@Override
	public Contato update(Integer id, Contato domainObject) {
		Contato contatoResult = this.getById(id);
		BeanUtils.copyProperties(domainObject, contatoResult, "id");
		this.contatoRepository.saveAndFlush(contatoResult);
		return contatoResult;
	}
	
	private void adicionarRestricaoDePaginacao(TypedQuery<Contato> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroResgistroDaPagina = paginaAtual * totalRegistrosPorPagina;
		query.setFirstResult(primeiroResgistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);
	}
	
	@Override
	public Page<Contato> pesquisarByNome(String nome, Pageable pageable) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Contato> criteria = builder.createQuery(Contato.class);		
		Root<Contato> root = criteria.from(Contato.class);
		Predicate[] predicate = where(nome, builder, root);
		criteria.where(predicate);
		criteria.orderBy(builder.asc(root.get("id")));
		TypedQuery<Contato> query = entityManager.createQuery(criteria);
		
		adicionarRestricaoDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(nome)) ;
	}
	
	private Predicate[] where(String nome, CriteriaBuilder builder, Root<Contato> root) {
		List<Predicate> predicates = new ArrayList<>();	
		if (!StringUtils.isEmpty(nome)) {
			predicates.add(builder.like(
				builder.lower(root.get("nome")), nome.toLowerCase() + "%" ));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	private Long total(String nome) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Contato> root = criteria.from(Contato.class);
		
		Predicate[] predicates = where(nome, builder, root);
		criteria.where(predicates);	
		criteria.select(builder.count(root));
		return entityManager.createQuery(criteria).getSingleResult();
	}
}
