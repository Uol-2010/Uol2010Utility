/**
 * 
 */
package net.bncf.uol2010.businessLogin.richieste;

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
import net.bncf.uol2010.database.schema.servizi.dao.StatoMovimentoDAO;
import net.bncf.uol2010.database.schema.servizi.entity.StatoMovimento;

/**
 * @author massi
 *
 */
public class StatoMovimentoBusiness extends BusinessLogic<StatoMovimento, StatoMovimentoDAO, String> {

	/**
	 * 
	 */
	public StatoMovimentoBusiness() {
	}

	/**
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#postSave(net.bncf.uol2010.businessLogin.HashTable, java.io.Serializable)
	 */
	@Override
	protected void postSave(HashTable<String, Object> dati, StatoMovimento table) throws NamingException,
			HibernateUtilException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	}

	/**
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#newInstance()
	 */
	@Override
	protected StatoMovimento newInstance() {
		return new StatoMovimento();
	}

	/**
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#newInstanceDao()
	 */
	@Override
	protected StatoMovimentoDAO newInstanceDao() {
		return new StatoMovimentoDAO();
	}

	/* (non-Javadoc)
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#save(java.io.Serializable, net.bncf.uol2010.businessLogin.HashTable)
	 */
	@Override
	protected void save(StatoMovimento table, HashTable<String, Object> dati)
			throws HibernateException, HibernateUtilException {
	}

	/* (non-Javadoc)
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#find(mx.randalf.hibernate.GenericHibernateDAO, net.bncf.uol2010.businessLogin.HashTable, java.util.List, int, int)
	 */
	@Override
	protected List<StatoMovimento> find(StatoMovimentoDAO tableDao, HashTable<String, Object> dati, List<Order> orders,
			int page, int pageSize) throws HibernateException, HibernateUtilException {
		return null;
	}

	/* (non-Javadoc)
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#rowsCount(mx.randalf.hibernate.GenericHibernateDAO, net.bncf.uol2010.businessLogin.HashTable)
	 */
	@Override
	protected Criteria rowsCount(StatoMovimentoDAO tableDao, HashTable<String, Object> dati) {
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
