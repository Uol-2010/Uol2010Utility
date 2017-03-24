/**
 * 
 */
package net.bncf.uol2010.businessLogin.ingressi;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;

import mx.randalf.hibernate.exception.HibernateUtilException;
import net.bncf.uol2010.businessLogin.BusinessLogic;
import net.bncf.uol2010.businessLogin.HashTable;
import net.bncf.uol2010.businessLogin.exception.BusinessLogicException;
import net.bncf.uol2010.database.schema.servizi.dao.IngressiDAO;
import net.bncf.uol2010.database.schema.servizi.dao.UtenteDAO;
import net.bncf.uol2010.database.schema.servizi.entity.Ingressi;
import net.bncf.uol2010.database.schema.servizi.entity.Utente;

/**
 * @author massi
 *
 */
public class IngressiBusiness extends BusinessLogic<Ingressi, IngressiDAO, Integer> {

	private Logger log = Logger.getLogger(IngressiBusiness.class);

	@Override
	protected void postSave(HashTable<String, Object> dati, Ingressi table) throws NamingException,
			HibernateUtilException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	}

	@Override
	protected Ingressi newInstance() {
		return new Ingressi();
	}

	@Override
	protected IngressiDAO newInstanceDao() {
		return new IngressiDAO();
	}

	@Override
	protected void save(Ingressi table, HashTable<String, Object> dati)
			throws HibernateException, HibernateUtilException {
		UtenteDAO utenteDAO = null;

		if (dati.containsKey("idUtente")) {
			if (dati.get("idUtente") instanceof String){
				utenteDAO = new UtenteDAO();
				table.setIdUtente(utenteDAO.findById((String) dati.get("idUtente")));
			} else {
				table.setIdUtente((Utente) dati.get("idUtente"));
			}
		}

		if (dati.containsKey("data")) {
			table.setData((Date) dati.get("data"));
		}

		if (dati.containsKey("oraIng")) {
			table.setOraIng((Time) dati.get("oraIng"));
		}

		if (dati.containsKey("oraUsc")) {
			table.setOraUsc((Time) dati.get("oraUsc"));
		}
	}

	@Override
	protected List<Ingressi> find(IngressiDAO tableDao, HashTable<String, Object> dati, List<Order> orders, int page,
			int pageSize) throws HibernateException, HibernateUtilException {
		return null;
	}

	@Override
	protected Criteria rowsCount(IngressiDAO tableDao, HashTable<String, Object> dati) {
		return null;
	}

	@Override
	public void delete(Integer id) throws Exception {
		
	}

	@Override
	protected String toJson(String key, Object value) throws SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, BusinessLogicException {
		return null;
	}

	public boolean ingresso(String idUtente, GregorianCalendar gc) throws SQLException {
		HashTable<String, Object> dati=null;
		UtenteDAO utenteDAO = null;
		boolean esito = false;
		Date date = null;
		Time time = null;
		
		try {
			dati = new HashTable<String, Object>();

			utenteDAO = new UtenteDAO();
			dati.put("idUtente", utenteDAO.findById(idUtente));

			date = new Date(gc.getTimeInMillis());
			dati.put("data", date);

			time = new Time(gc.getTimeInMillis());
			dati.put("oraIng", time);

			esito =(save(dati)>0);
		} catch (HibernateException e) {
			throw new SQLException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new SQLException(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new SQLException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new SQLException(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			throw new SQLException(e.getMessage(), e);
		} catch (SecurityException e) {
			throw new SQLException(e.getMessage(), e);
		} catch (HibernateUtilException e) {
			throw new SQLException(e.getMessage(), e);
		} catch (NamingException e) {
			throw new SQLException(e.getMessage(), e);
		}

		return esito;
	}

	public boolean uscita(Integer id, String idUtente, GregorianCalendar gc, boolean presente) throws SQLException {
		HashTable<String, Object> dati=null;
		UtenteDAO utenteDAO = null;
		boolean esito = false;
		Date date = null;
		Time time = null;
		
		try {
			dati = new HashTable<String, Object>();

			utenteDAO = new UtenteDAO();

			
			time = new Time(gc.getTimeInMillis());
			dati.put("oraUsc", time);

			if (!presente){
				dati.put("idUtente", utenteDAO.findById(idUtente));
	
				gc.set(Calendar.HOUR_OF_DAY, 7);
				gc.set(Calendar.MINUTE, 0);
				gc.set(Calendar.SECOND, 0);
				date = new Date(gc.getTimeInMillis());
				dati.put("data", date);
	
				time = new Time(gc.getTimeInMillis());
				dati.put("oraIng", time);
			} else {
				dati.put("id", id);
			}

			esito =(save(dati)>0);
		} catch (HibernateException e) {
			throw new SQLException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new SQLException(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new SQLException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new SQLException(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			throw new SQLException(e.getMessage(), e);
		} catch (SecurityException e) {
			throw new SQLException(e.getMessage(), e);
		} catch (HibernateUtilException e) {
			throw new SQLException(e.getMessage(), e);
		} catch (NamingException e) {
			throw new SQLException(e.getMessage(), e);
		}

		return esito;
	}

	public Ingressi findPresenti(String idUtente) throws HibernateException, HibernateUtilException {
		IngressiDAO tableDao = null;
		Ingressi ingressi = null;

		try {
			tableDao = newInstanceDao();
			ingressi = tableDao.findPresenti(idUtente);
		} catch (HibernateException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (HibernateUtilException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new HibernateUtilException(e.getMessage(), e);
		}

		return ingressi;
	}

	public List<Object[]> viewIngressi() throws HibernateException, HibernateUtilException{
		IngressiDAO tableDao = null;
		List<Object[]> objects = null;

		try {
			tableDao = newInstanceDao();
			objects = tableDao.viewIngressi(new Date(new GregorianCalendar().getTimeInMillis()));
		} catch (HibernateException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (HibernateUtilException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new HibernateUtilException(e.getMessage(), e);
		}
		
		return objects;
	}

	/* (non-Javadoc)
	 * @see net.bncf.uol2010.businessLogin.BusinessLogic#genID()
	 */
	@Override
	protected Integer genID() throws HibernateException, HibernateUtilException {
		Long maxId = null;
		int result = 1;
		
		
		maxId = this.maxId();
		if (maxId ==null){
			result = 1;
		} else {
			result = (maxId.intValue()+1);
		}
		return result;
	}
}
