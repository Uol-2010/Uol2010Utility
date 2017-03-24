/**
 * 
 */
package net.bncf.uol2010.businessLogin.varchi;

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
import net.bncf.uol2010.database.schema.servizi.dao.ConfVarchiDAO;
import net.bncf.uol2010.database.schema.servizi.entity.ConfVarchi;

/**
 * @author massi
 *
 */
public class ConfVarchiBusiness extends BusinessLogic<ConfVarchi, ConfVarchiDAO, Integer> {

	/**
	 * 
	 */
	public ConfVarchiBusiness() {
	}

	/* (non-Javadoc)
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#postSave(net.bncf.uol2010.businessLogin.HashTable, java.io.Serializable)
	 */
	@Override
	protected void postSave(HashTable<String, Object> dati, ConfVarchi table) throws NamingException,
			HibernateUtilException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	}

	/* (non-Javadoc)
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#newInstance()
	 */
	@Override
	protected ConfVarchi newInstance() {
		return new ConfVarchi();
	}

	/* (non-Javadoc)
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#newInstanceDao()
	 */
	@Override
	protected ConfVarchiDAO newInstanceDao() {
		return new ConfVarchiDAO();
	}

	/* (non-Javadoc)
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#save(java.io.Serializable, net.bncf.uol2010.businessLogin.HashTable)
	 */
	@Override
	protected void save(ConfVarchi table, HashTable<String, Object> dati)
			throws HibernateException, HibernateUtilException {
	}

	/**
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#find(mx.randalf.hibernate.GenericHibernateDAO, net.bncf.uol2010.businessLogin.HashTable, java.util.List, int, int)
	 */
	@Override
	protected List<ConfVarchi> find(ConfVarchiDAO tableDao, HashTable<String, Object> dati, List<Order> orders,
			int page, int pageSize) throws HibernateException, HibernateUtilException {
		List<ConfVarchi> tables;

		tableDao.setPage(page);
		tableDao.setPageSize(pageSize);
		tables = tableDao.find((String)dati.get("tipoVarco"),
				orders);
		return tables;
	}

	public List<ConfVarchi> find(Integer idConfVarchi, String tipoVarco, int page, int pageSize) 
					throws HibernateException, HibernateUtilException{
		HashTable<String, Object> dati = null;
		
		dati = new HashTable<String, Object>();
		
		if (idConfVarchi != null){
			dati.put("id", idConfVarchi);
		}
		
		if (tipoVarco != null){
			dati.put("tipoVarco", tipoVarco);
		}
		
		return find(dati, page, pageSize);
	}

	/* (non-Javadoc)
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#rowsCount(mx.randalf.hibernate.GenericHibernateDAO, net.bncf.uol2010.businessLogin.HashTable)
	 */
	@Override
	protected Criteria rowsCount(ConfVarchiDAO tableDao, HashTable<String, Object> dati) {
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
