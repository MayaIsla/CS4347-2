package cs4347.hibernateProject.ecomm.services.impl;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;


import cs4347.hibernateProject.ecomm.services.ProductPersistenceService;
import cs4347.hibernateProject.ecomm.util.DAOException;
import cs4347.hibernateProject.ecomm.entity.Customer;
import cs4347.hibernateProject.ecomm.entity.Product;

public class ProductPersistenceServiceImpl implements ProductPersistenceService
{
	@PersistenceContext//create entity manager.
	public EntityManager em;
	
	public ProductPersistenceServiceImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public void create(Product product) throws SQLException, DAOException
	{
		try
		{
			em.getTransaction().begin();//transaction driven action from CRUD implementation
			em.persist(product);
			em.getTransaction().commit();
		}

		catch (Exception ex)
		{
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override //eclipse driven response
	public Product retrieve (Long id) throws SQLException, DAOException
	{
		try
		{
			em.getTransaction().begin();
			Product prod = em.find(Product.class, id);
			em.getTransaction().commit();
			return prod;
		}

		catch (Exception ex)
		{
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void update (Product product) throws SQLException, DAOException
	{
		try
		{
			em.getTransaction().begin();
			Product p = em.find(Product.class, product.getId());
			p.setProdName(product.getProdName());
			p.setProdDescription(product.getProdDescription());
			em.getTransaction().commit();
		}

		catch (Exception ex)
		{
			em.getTransaction().rollback(); //cascades deletes all the following SQL commits.
			throw ex;
		}
	}

	@Override
	public void delete (Long id) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			Product p = (Product)em.find(Product.class, id);
			em.remove(p);
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public Product retrieveByUPC(String upc) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		Product prod = (Product)em.createQuery("FROM Product AS p WHERE p.prodUPC = :productUPC")
				.setParameter("productUPC", upc)
				.getSingleResult();
		em.getTransaction().commit();
		return prod;
	}

	@Override
	public List<Product> retrieveByCategory(int category) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		List<Product> prod = (List<Product>)em.createQuery("FROM Product AS p WHERE p.prodCategory = :productCategory")
				.setParameter("productCategory", category)
				.getResultList();
		em.getTransaction().commit();
		return prod;
	}
}
