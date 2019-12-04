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

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cs4347.hibernateProject.ecomm.entity.Customer;
import cs4347.hibernateProject.ecomm.services.CustomerPersistenceService;
import cs4347.hibernateProject.ecomm.util.DAOException;

public class CustomerPersistenceServiceImpl implements CustomerPersistenceService
{
	@PersistenceContext 
	private EntityManager em; 
	
	public CustomerPersistenceServiceImpl(EntityManager em) {
		this.em = em;
	}
	
	/***/
	@Override
	public void create(Customer customer) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			em.persist(customer);
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}
 
	@Override
	public Customer retrieve(Long id) 
	{
		try {
			em.getTransaction().begin();
			Customer customer = em.find(Customer.class, id);
			em.getTransaction().commit();
			return customer;
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void update(Customer customer) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			Customer customer2 = em.find(Customer.class, customer.getId());
			customer2.setFirstName(customer.getFirstName());
			customer2.setLastName(customer.getLastName());
			customer2.setGender(customer.getGender());
			customer2.setEmail(customer.getEmail());
			customer2.setAddress(customer.getAddress());
			customer2.setCreditCard(customer.getCreditCard());
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void delete(Long id) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			Customer customer = (Customer)em.find(Customer.class, id);
			em.remove(customer);
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public List<Customer> retrieveByZipCode(String zipCode) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		List<Customer> customer = (List<Customer>)em.createQuery("FROM Customer AS c WHERE c.address.zipcode = :zipCode")
				.setParameter("zipCode", zipCode)
				.getResultList();
		em.getTransaction().commit();
		return customer;
	}

	@Override
	public List<Customer> retrieveByDOB(Date startDate, Date endDate) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		List<Customer> customer = (List<Customer>)em.createQuery("FROM Customer AS c WHERE c.dob BETWEEN :startDate AND :endDate")
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getResultList();
		em.getTransaction().commit();
		return customer;
	}
}
