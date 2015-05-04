package models.repository;

import java.util.List;

public interface CrudRepository<T> {
	
	public T findById(Long id);
	public void persist(T entity);
	public List<T> findAll();
	
}
