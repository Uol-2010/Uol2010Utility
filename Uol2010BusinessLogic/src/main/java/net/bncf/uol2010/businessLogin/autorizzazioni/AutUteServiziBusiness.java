/**
 * 
 */
package net.bncf.uol2010.businessLogin.autorizzazioni;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;

import mx.randalf.hibernate.exception.HibernateUtilException;
import net.bncf.uol2010.businessLogin.BusinessLogic;
import net.bncf.uol2010.businessLogin.HashTable;
import net.bncf.uol2010.businessLogin.exception.BusinessLogicException;
import net.bncf.uol2010.database.schema.servizi.dao.AutUteServiziDAO;
import net.bncf.uol2010.database.schema.servizi.entity.AutUteServizi;
import net.bncf.uol2010.database.schema.servizi.entity.AutorizzazioniUte;

/**
 * @author massi
 *
 */
public class AutUteServiziBusiness extends BusinessLogic<AutUteServizi, AutUteServiziDAO, String> {

	/**
	 * 
	 */
	public AutUteServiziBusiness() {
	}

	/* (non-Javadoc)
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#postSave(net.bncf.uol2010.businessLogin.HashTable, java.io.Serializable)
	 */
	@Override
	protected void postSave(HashTable<String, Object> dati, AutUteServizi table) throws NamingException,
			HibernateUtilException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	}

	/* (non-Javadoc)
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#newInstance()
	 */
	@Override
	protected AutUteServizi newInstance() {
		return new AutUteServizi();
	}

	/* (non-Javadoc)
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#newInstanceDao()
	 */
	@Override
	protected AutUteServiziDAO newInstanceDao() {
		return new AutUteServiziDAO();
	}

	/* (non-Javadoc)
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#save(java.io.Serializable, net.bncf.uol2010.businessLogin.HashTable)
	 */
	@Override
	protected void save(AutUteServizi table, HashTable<String, Object> dati)
			throws HibernateException, HibernateUtilException {
	}

	/* (non-Javadoc)
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#find(mx.randalf.hibernate.GenericHibernateDAO, net.bncf.uol2010.businessLogin.HashTable, java.util.List, int, int)
	 */
	@Override
	protected List<AutUteServizi> find(AutUteServiziDAO tableDao, HashTable<String, Object> dati, List<Order> orders,
			int page, int pageSize) throws HibernateException, HibernateUtilException {
		List<AutUteServizi> tables;

		tableDao.setPage(page);
		tableDao.setPageSize(pageSize);
		tables = tableDao.find((AutorizzazioniUte)dati.get("idAutorizzazioniUtente"),
				orders);
		return tables;
	}

	public List<AutUteServizi> find(AutorizzazioniUte idAutorizzazioniUtente, int page, int pageSize) 
					throws HibernateException, HibernateUtilException{
		HashTable<String, Object> dati = null;
		
		dati = new HashTable<String, Object>();
		
		if (idAutorizzazioniUtente != null){
			dati.put("idAutorizzazioniUtente", idAutorizzazioniUtente);
		}
		
		return find(dati, page, pageSize);
	}

	/* (non-Javadoc)
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#rowsCount(mx.randalf.hibernate.GenericHibernateDAO, net.bncf.uol2010.businessLogin.HashTable)
	 */
	@Override
	protected Criteria rowsCount(AutUteServiziDAO tableDao, HashTable<String, Object> dati) {
		return null;
	}

	/* (non-Javadoc)
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#delete(java.io.Serializable)
	 */
	@Override
	public void delete(String id) throws Exception {
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
