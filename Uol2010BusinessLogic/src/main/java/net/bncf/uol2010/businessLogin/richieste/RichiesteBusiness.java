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
import net.bncf.uol2010.businessLogin.servizi.ServiziBusiness;
import net.bncf.uol2010.businessLogin.utenti.UtenteBusiness;
import net.bncf.uol2010.database.schema.servizi.dao.RichiesteDAO;
import net.bncf.uol2010.database.schema.servizi.entity.Richieste;
import net.bncf.uol2010.database.schema.servizi.entity.Servizi;
import net.bncf.uol2010.database.schema.servizi.entity.StatoMovimento;
import net.bncf.uol2010.database.schema.servizi.entity.Utente;

/**
 * @author massi
 *
 */
public class RichiesteBusiness extends BusinessLogic<Richieste, RichiesteDAO, Integer> {

	/**
	 * 
	 */
	public RichiesteBusiness() {
	}

	/* (non-Javadoc)
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#postSave(net.bncf.uol2010.businessLogin.HashTable, java.io.Serializable)
	 */
	@Override
	protected void postSave(HashTable<String, Object> dati, Richieste table) throws NamingException,
			HibernateUtilException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	}

	/**
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#newInstance()
	 */
	@Override
	protected Richieste newInstance() {
		return new Richieste();
	}

	/**
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#newInstanceDao()
	 */
	@Override
	protected RichiesteDAO newInstanceDao() {
		return new RichiesteDAO();
	}

	/**
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#save(java.io.Serializable, net.bncf.uol2010.businessLogin.HashTable)
	 */
	@Override
	protected void save(Richieste table, HashTable<String, Object> dati)
			throws HibernateException, HibernateUtilException {
	}

	/* (non-Javadoc)
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#find(mx.randalf.hibernate.GenericHibernateDAO, net.bncf.uol2010.businessLogin.HashTable, java.util.List, int, int)
	 */
	@Override
	protected List<Richieste> find(RichiesteDAO tableDao, HashTable<String, Object> dati, List<Order> orders, int page,
			int pageSize) throws HibernateException, HibernateUtilException {
		List<Richieste> tables;

		tableDao.setPage(page);
		tableDao.setPageSize(pageSize);
		tables = tableDao.find((Utente)dati.get("idUtente"),
				(StatoMovimento)dati.get("idStatoMovimenti"),
				(Servizi[])dati.get("idServizi"),
				orders);
		return tables;
	}

	public List<Richieste> find(String idRichieste, String idUtente, 
			String idStatoMovimenti, String[] idServizi, int page, int pageSize)
			throws HibernateException, HibernateUtilException {
		HashTable<String, Object> dati = null;
		UtenteBusiness utenteBusiness = null;
		StatoMovimentoBusiness statoMovimentoBusiness = null;
		ServiziBusiness serviziBusiness = null;
		Servizi[] servizis = null;
		
		dati = new HashTable<String, Object>();
		
		if (idRichieste != null){
			dati.put("id", idRichieste);
		}
		
		if (idUtente != null){
			utenteBusiness = new UtenteBusiness();
			dati.put("idUtente", utenteBusiness.findById(idUtente));
		}
		
		if (idStatoMovimenti != null){
			statoMovimentoBusiness = new StatoMovimentoBusiness();
			dati.put("idStatoMovimenti", statoMovimentoBusiness.findById(idStatoMovimenti));
		}
		
		if (idServizi != null){
			serviziBusiness = new ServiziBusiness();
			servizis = new Servizi[idServizi.length];
			for ( int x= 0; x<idServizi.length; x++){
				servizis[x]= serviziBusiness.findById(idServizi[x]);
			}
			dati.put("idServizi", servizis);
		}
		
		return find(dati, page, pageSize);
	}

	/* (non-Javadoc)
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#rowsCount(mx.randalf.hibernate.GenericHibernateDAO, net.bncf.uol2010.businessLogin.HashTable)
	 */
	@Override
	protected Criteria rowsCount(RichiesteDAO tableDao, HashTable<String, Object> dati) {
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
