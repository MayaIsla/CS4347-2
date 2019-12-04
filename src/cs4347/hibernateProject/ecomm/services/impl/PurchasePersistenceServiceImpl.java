/* NOTICE: All materials provided by this project, and materials derived 
 * from the project, are the property of the University of Texas. 
 * Project materials, or those derived from the materials, cannot be placed 
 * into publicly accessible locations on the web. Project materials cannot 
 * be shared with other project teams. Making project materials publicly 
 * accessible, or sharing with other project teams will result in the 
 * failure of the team responsible and any team that uses the shared materials. 
 * Sharing project materials or using shared materials will also result 
 * in the reporting of all team members for academic dishonesty. 
 */ 
 
package cs4347.hibernateProject.ecomm.services.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cs4347.hibernateProject.ecomm.entity.Customer;
import cs4347.hibernateProject.ecomm.entity.Purchase;
import cs4347.hibernateProject.ecomm.services.PurchasePersistenceService;
import cs4347.hibernateProject.ecomm.services.PurchaseSummary;
import cs4347.hibernateProject.ecomm.util.DAOException;

public class PurchasePersistenceServiceImpl implements PurchasePersistenceService
{
	@PersistenceContext 
	private EntityManager em; 
	
	public PurchasePersistenceServiceImpl(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public void create(Purchase purchase) throws SQLException, DAOException
	{
		try
		{
			em.getTransaction().begin();
			em.persist(purchase);
			em.getTransaction().commit();
		}
		catch (Exception ex)
		{
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public Purchase retrieve(Long id) throws SQLException, DAOException
	{
		try 
		{
			em.getTransaction().begin();
			Purchase pur = em.find(Purchase.class, id);
			em.getTransaction().commit();
			return pur;
		}
		catch (Exception ex)
		{
			em.getTransaction().rollback();
			throw ex;
		}
		//return null;
	}

	@Override
	public void update(Purchase purchase) throws SQLException, DAOException
	{
		try
		{
			em.getTransaction().begin();
			Purchase pur = em.find(Purchase.class,  purchase.getId());
			pur.setPurchaseAmount(purchase.getPurchaseAmount());
			pur.setPurchaseDate(purchase.getPurchaseDate());
			pur.setCustomer(purchase.getCustomer());
			pur.setProduct(purchase.getProduct());
			em.getTransaction().commit();
		}
		
		catch (Exception ex)
		{
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void delete(Long id) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			Purchase p = (Purchase)em.find(Purchase.class, id);
			em.remove(p);
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}
	

	@Override
	public List<Purchase> retrieveForCustomerID(Long customerID) throws SQLException, DAOException
	{
		if ((customerID < 0))
		{
			throw new DAOException("Sorry, Invalid ID entered ");
		}
		
		List<Purchase> purList = em.createQuery("FROM Purchase AS p WHERE p.customer.id = :CustomerID")
				.setParameter("CustomerID", customerID)
				.getResultList();
		
		return purList;
		}
		//return null;

	@Override
	public PurchaseSummary retrievePurchaseSummary(Long customerID) throws SQLException, DAOException
	{
		List<Purchase> purSum = new ArrayList<Purchase>();
		purSum = retrieveForCustomerID(customerID);
		PurchaseSummary result = new PurchaseSummary();
		double min = 9999999999.99;
		double max = 0;
		double sum = 0;
		
		for (Purchase pur : purSum) 
		{
			double Value = pur.getPurchaseAmount();
			if (Value < min)
			{
				min = Value;
			}
			
			if (Value > max)
			{
				max = Value;
			}
			
			sum +=Value;
		}
		
		double average = (sum/purSum.size());
		
		result.minPurchase = (float)min;
		result.maxPurchase = (float)max;
		result.avgPurchase = (float)average;
		
		return result;
		//return null;
	}

	@Override
	public List<Purchase> retrieveForProductID(Long productID) throws SQLException, DAOException
	{
		if((productID < 0)){
			throw new DAOException("Invalid product ID provided");
		}
		List<Purchase> purList = em.createQuery("FROM Purchase AS p WHERE p.product.id = :prodID")
				.setParameter("prodID", productID)
				.getResultList();
		
		return purList;
	}
}