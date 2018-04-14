package br.com.desafioglobo.service;

import java.util.List;

public interface ICRUDService<T> {
	
	List<?> listarTodos();

    T getById(Integer id);

    T save(T domainObject);
    
    T update(Integer id, T domainObject);
    
    void delete(Integer id);

}
