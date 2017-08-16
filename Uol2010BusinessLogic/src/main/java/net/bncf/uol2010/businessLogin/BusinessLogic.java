/**
 * 
 */
package net.bncf.uol2010.businessLogin;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import mx.randalf.configuration.exception.ConfigurationException;
import mx.randalf.hibernate.FactoryDAO;
import mx.randalf.hibernate.GenericHibernateDAO;
import mx.randalf.hibernate.exception.HibernateUtilException;
import net.bncf.uol2010.businessLogin.exception.BusinessLogicException;

/**
 * @author massi
 * 
 */
@SuppressWarnings("rawtypes")
public abstract class BusinessLogic<T extends Serializable, D extends GenericHibernateDAO, ID extends Serializable> {

	private Logger log = Logger.getLogger(BusinessLogic.class);

	private List<Order> order = null;

	/**
	 * 
	 */
	public BusinessLogic() {
	}

	/**
	 * Metodo per la ricerca
	 * 
	 * @param dati
	 *            Lista dei dati da ricercare
	 * @param page
	 *            Pagina da ricercare
	 * @param pageSize
	 *            Record per pagine
	 * @return Lista dei record trovati
	 * @throws HibernateException
	 *             Eccezioni di Hibernate
	 * @throws NamingException
	 *             Eccezioni di Naming
	 * @throws ConfigurationException
	 *             Eccezioni di Configurazione
	 */
	@SuppressWarnings("unchecked")
	public List<T> find(HashTable<String, Object> dati, int page, int pageSize)
			throws HibernateException, HibernateUtilException {
		D tableDao = null;
		T table = null;
		List<T> tables = null;
		List<Order> orders = null;

		try {
			tableDao = newInstanceDao();
			orders = setOrder();

			if (dati == null || dati.size() == 0) {
				tables = tableDao.findAll(orders, page, pageSize);
			} else if (dati.containsKey("id")) {
				table = (T) tableDao.findById((ID) dati.get("id"));
				if (table != null) {
					tables = new Vector<T>();
					tables.add(table);
				}
			} else {
				tables = find(tableDao, dati, orders, page, pageSize);
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}

		return tables;
	}

	public Long rowsCount(HashTable<String, Object> dati) throws HibernateException, HibernateUtilException {
		D tableDao = null;
		Long result = new Long("0");
		Criteria crit = null;
		try {

			tableDao = newInstanceDao();
			tableDao.beginTransaction();

			crit = rowsCount(tableDao, dati);

			crit.setProjection(Projections.rowCount());
			result = (Long) crit.uniqueResult();
			tableDao.commitTransaction();
		} catch (HibernateException e) {
			tableDao.rollbackTransaction();
			log.error(e.getMessage(), e);
			throw e;
		} catch (HibernateUtilException e) {
			tableDao.rollbackTransaction();
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			tableDao.rollbackTransaction();
			log.error(e.getMessage(), e);
			throw new HibernateUtilException(e.getMessage(), e);
		}
		return result;
	}

	public Long maxId() throws HibernateException, HibernateUtilException {
		D tableDao = null;
		Long result = new Long("0");
		Criteria crit = null;
		Object maxId = null;
		try {

			tableDao = newInstanceDao();
			tableDao.beginTransaction();

			crit = tableDao.createCriteria();
			crit.setProjection(Projections.max("id"));
			maxId = crit.uniqueResult();
			if (maxId != null){
				result = new Long(maxId+"");
			}
			tableDao.commitTransaction();
		} catch (HibernateException e) {
			tableDao.rollbackTransaction();
			log.error(e.getMessage(), e);
			throw e;
		} catch (HibernateUtilException e) {
			tableDao.rollbackTransaction();
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			tableDao.rollbackTransaction();
			log.error(e.getMessage(), e);
			throw new HibernateUtilException(e.getMessage(), e);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public T findById(ID id) throws HibernateException, HibernateUtilException {
		D tableDao = null;
		T result = null;

		try {
			tableDao = newInstanceDao();
			result = (T) tableDao.getById(id);
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}

		return result;
	}

	public void addOrder(String key, String verso) {
		if (order == null) {
			order = new Vector<Order>();
		}
		if (verso.trim().equalsIgnoreCase("ASC")) {
			order.add(Order.asc(key.trim()));
		} else {
			order.add(Order.desc(key.trim()));
		}
	}

	protected List<Order> setOrder() {
		return order;
	}

	public ID save(HashTable<String, Object> dati)
			throws HibernateException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, HibernateUtilException, NamingException {
		return save(dati, false);
	}

	@SuppressWarnings("unchecked")
	public ID save(HashTable<String, Object> dati, boolean forceSave)
			throws HibernateException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, HibernateUtilException, NamingException {
		D tableDao = null;
		T table = null;
		ID id = null;
		ID result = null;

		try {
			FactoryDAO.beginTransaction();
			tableDao = newInstanceDao();
			if (!dati.containsKey("id")) {
				id = genID();
				table = newInstance();
				if (id instanceof String) {
					table.getClass().getMethod("setId", String.class).invoke(table, id);
				} else if (id instanceof Integer) {
					table.getClass().getMethod("setId", Integer.class).invoke(table, id);
				}
			} else {
				table = (T) tableDao.getById((ID) dati.get("id"));
				if (table == null) {
					forceSave = true;
					id = (ID) dati.get("id");
					table = newInstance();
					if (id instanceof String) {
						table.getClass().getMethod("setId", String.class).invoke(table, id);
					} else if (id instanceof Integer) {
						table.getClass().getMethod("setId", Integer.class).invoke(table, id);
					}
				}
			}

			save(table, dati);

			if (dati.containsKey("id") && !forceSave) {
				tableDao.update(table);
			} else {
				tableDao.save(table);
			}

			FactoryDAO.commitTransaction(true);
			postSave(dati, table);

			result = (table == null ? null : (ID) table.getClass().getMethod("getId").invoke(table));
		} catch (HibernateException e) {
			e.printStackTrace();
			FactoryDAO.rollbackTransaction(true);
			log.error(e.getMessage(), e);
			throw e;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			FactoryDAO.rollbackTransaction(true);
			log.error(e.getMessage(), e);
			throw e;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			FactoryDAO.rollbackTransaction(true);
			log.error(e.getMessage(), e);
			throw e;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			FactoryDAO.rollbackTransaction(true);
			log.error(e.getMessage(), e);
			throw e;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			FactoryDAO.rollbackTransaction(true);
			log.error(e.getMessage(), e);
			throw e;
		} catch (SecurityException e) {
			e.printStackTrace();
			FactoryDAO.rollbackTransaction(true);
			log.error(e.getMessage(), e);
			throw e;
		} catch (HibernateUtilException e) {
			e.printStackTrace();
			FactoryDAO.rollbackTransaction(true);
			log.error(e.getMessage(), e);
			throw e;
		} catch (NamingException e) {
			e.printStackTrace();
			FactoryDAO.rollbackTransaction(true);
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			FactoryDAO.rollbackTransaction(true);
			log.error(e.getMessage(), e);
			throw new HibernateUtilException(e.getMessage(), e);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	protected ID genID() throws HibernateException, HibernateUtilException {
		ID id = null;
		id = (ID) UUID.randomUUID().toString();
		return id;
	}

	protected abstract void postSave(HashTable<String, Object> dati, T table) throws NamingException,
			HibernateUtilException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;

	protected abstract T newInstance();

	protected abstract D newInstanceDao();

	protected abstract void save(T table, HashTable<String, Object> dati)
			throws HibernateException, HibernateUtilException;

	/**
	 * Metodo per la Ricerca
	 * 
	 * @param tableDao
	 *            Oggetto DAO per la ricerca
	 * @param dati
	 *            Metodi per la ricerca
	 * @param orders
	 *            Metodi di Ordinamento
	 * @param page
	 *            Pagina da ricercare
	 * @param pageSize
	 *            Record per pagine
	 * @return Lista dei record trovati
	 * @throws NamingException
	 *             Eccezioni di Naming
	 * @throws ConfigurationException
	 *             Eccezioni di Configurazione
	 */
	protected abstract List<T> find(D tableDao, HashTable<String, Object> dati, List<Order> orders, int page,
			int pageSize) throws HibernateException, HibernateUtilException;

	protected abstract Criteria rowsCount(D tableDao, HashTable<String, Object> dati);

	public abstract void delete(ID id) throws Exception;

	@SuppressWarnings("unchecked")
	public String toJson(Serializable table) 
			throws SecurityException, IllegalAccessException, IllegalArgumentException, 
			InvocationTargetException, BusinessLogicException {
		String jsonArray = "";
		Class<T> cls = null;
		GregorianCalendar gc = null;
		DecimalFormat df4 = new DecimalFormat("0000");
		DecimalFormat df2 = new DecimalFormat("00");
		Object value = null;
		boolean primo = true;
		String key = null;

		try {
			jsonArray += "  {\n";

			cls = (Class<T>) table.getClass();

			Method m[] = cls.getMethods();
			for (int i = 0; i < m.length; i++) {
				if (m[i].getName().startsWith("get") && 
						!m[i].getName().equalsIgnoreCase("getClass") &&
						!m[i].getName().equalsIgnoreCase("getHandler") &&
						!m[i].getName().equalsIgnoreCase("getHibernateLazyInitializer")
						) {
					if (m[i].getParameterTypes().length==0){
						value = null;
						try{
							value = m[i].invoke(table);
						} catch (Exception e){
							System.out.println(table.getClass().getName()+" - "+m[i]);
							e.printStackTrace();
						}
						if (value != null) {

							key = m[i].getName();
							key = key.substring(3, 4).toLowerCase()+key.substring(4);

							if (!primo) {
								jsonArray += ",\n";
							}
							primo = false;
							jsonArray += "\"" + key + "\": ";
							if (value instanceof String) {
								if (((String) value).indexOf("\"")>-1){
									value = ((String) value).replace("\"", "\\\"");
								}
								jsonArray += "\"" + value + "\"";
							} else if (value instanceof Integer) {
								jsonArray += "\"" + ((Integer) value).toString() + "\"";
							} else if (value instanceof Date) {
								gc = new GregorianCalendar();
								gc.setTimeInMillis(((Date) value).getTime());
								jsonArray += "\"" + df2.format(gc.get(Calendar.DAY_OF_MONTH)) + "/"
										+ df2.format(gc.get(Calendar.MONTH) + 1) + "/" + df4.format(gc.get(Calendar.YEAR)) + " "
										+ df2.format(gc.get(Calendar.HOUR_OF_DAY)) + ":" + df2.format(gc.get(Calendar.MINUTE)) + ":"
										+ df2.format(gc.get(Calendar.SECOND)) + "\"";
							} else if (value instanceof Boolean) {
								jsonArray += "\"" + (((Boolean) value) ? "true" : "false") + "\"";
							} else {
								jsonArray += toJson(key, value);
							}
						}
					}
				}
			}
			jsonArray += "  }";
		} catch (SecurityException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw e;
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (InvocationTargetException e) {
			throw e;
		} catch (BusinessLogicException e) {
			throw e;
		}

		return jsonArray;

	}

	protected abstract String toJson(String key, Object value) 
			throws SecurityException, IllegalAccessException, IllegalArgumentException, 
			InvocationTargetException, BusinessLogicException;
}
