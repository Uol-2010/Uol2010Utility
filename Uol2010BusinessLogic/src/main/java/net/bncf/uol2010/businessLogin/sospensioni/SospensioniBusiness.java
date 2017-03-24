/**
 * 
 */
package net.bncf.uol2010.businessLogin.sospensioni;

import java.lang.reflect.InvocationTargetException;
import java.util.GregorianCalendar;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;

import mx.randalf.hibernate.exception.HibernateUtilException;
import net.bncf.uol2010.businessLogin.BusinessLogic;
import net.bncf.uol2010.businessLogin.HashTable;
import net.bncf.uol2010.businessLogin.exception.BusinessLogicException;
import net.bncf.uol2010.businessLogin.utenti.UtenteBusiness;
import net.bncf.uol2010.database.schema.servizi.dao.SospensioniDAO;
import net.bncf.uol2010.database.schema.servizi.entity.Sospensioni;
import net.bncf.uol2010.database.schema.servizi.entity.Utente;

/**
 * @author massi
 *
 */
public class SospensioniBusiness extends BusinessLogic<Sospensioni, SospensioniDAO, Integer> {

	/**
	 * 
	 */
	public SospensioniBusiness() {
	}

	/* (non-Javadoc)
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#postSave(net.bncf.uol2010.businessLogin.HashTable, java.io.Serializable)
	 */
	@Override
	protected void postSave(HashTable<String, Object> dati, Sospensioni table) throws NamingException,
			HibernateUtilException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	}

	/**
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#newInstance()
	 */
	@Override
	protected Sospensioni newInstance() {
		return new Sospensioni();
	}

	/**
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#newInstanceDao()
	 */
	@Override
	protected SospensioniDAO newInstanceDao() {
		return new SospensioniDAO();
	}

	/* (non-Javadoc)
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#save(java.io.Serializable, net.bncf.uol2010.businessLogin.HashTable)
	 */
	@Override
	protected void save(Sospensioni table, HashTable<String, Object> dati)
			throws HibernateException, HibernateUtilException {
	}

	/* (non-Javadoc)
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#find(mx.randalf.hibernate.GenericHibernateDAO, net.bncf.uol2010.businessLogin.HashTable, java.util.List, int, int)
	 */
	@Override
	protected List<Sospensioni> find(SospensioniDAO tableDao, HashTable<String, Object> dati, List<Order> orders,
			int page, int pageSize) throws HibernateException, HibernateUtilException {
		List<Sospensioni> tables;

		tableDao.setPage(page);
		tableDao.setPageSize(pageSize);
		tables = tableDao.find((Utente)dati.get("idUtente"), 
				(GregorianCalendar)dati.get("data"), 
				(String)dati.get("cancellato"), 
				orders);
		return tables;
	}

	public List<Sospensioni> find(Integer idSospensione, String idUtente, 
			GregorianCalendar data, String cancellato, int page, int pageSize) 
					throws HibernateException, HibernateUtilException{
		HashTable<String, Object> dati = null;
		UtenteBusiness utenteBusiness = null;
		
		dati = new HashTable<String, Object>();
		
		if (idSospensione != null){
			dati.put("id", idSospensione);
		}
		
		if (idUtente != null){
			utenteBusiness = new UtenteBusiness();
			dati.put("idUtente", utenteBusiness.findById(idUtente));
		}
		
		if (data != null){
			dati.put("data", data);
		}
		
		if (cancellato != null){
			dati.put("cancellato", cancellato);
		}
		
		return find(dati, page, pageSize);
	}

	/* (non-Javadoc)
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#rowsCount(mx.randalf.hibernate.GenericHibernateDAO, net.bncf.uol2010.businessLogin.HashTable)
	 */
	@Override
	protected Criteria rowsCount(SospensioniDAO tableDao, HashTable<String, Object> dati) {
		return null;
	}

	/* (non-Javadoc)
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#delete(java.io.Serializable)
	 */
	@Override
	public void delete(Integer id) throws Exception {
	}

	/* (non-Javadoc)
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#toJson(java.lang.String, java.lang.Object)
	 */
	@Override
	protected String toJson(String key, Object value) throws SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, BusinessLogicException {
		return null;
	}

}
