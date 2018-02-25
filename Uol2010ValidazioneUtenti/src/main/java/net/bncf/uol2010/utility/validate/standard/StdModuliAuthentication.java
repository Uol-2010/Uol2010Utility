/**
 * 
 */
package net.bncf.uol2010.utility.validate.standard;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.SOAPException;

import org.apache.axis.message.MessageElement;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import mx.randalf.hibernate.exception.HibernateUtilException;
import mx.randalf.moduli.servlet.core.StdModuliCore;
import mx.randalf.tools.MD5Tools;
import net.bncf.uol2010.database.schema.servizi.dao.UtenteBibDAO;
import net.bncf.uol2010.database.schema.servizi.dao.UtenteDAO;
import net.bncf.uol2010.database.schema.servizi.entity.Utente;
import net.bncf.uol2010.database.schema.servizi.entity.UtenteBib;
import net.bncf.uol2010.utility.validate.user.UserValidator;
import net.bncf.uol2010.utility.validate.user.exception.UserValidatorException;

/**
 * Questa clase viene utilizzata per estendere le funzionalit&agrave; della
 * classe StdModuli utilizzata per la gestione dei Moduli per le Servlet in modo
 * da gestire il modulo di Autenticazione Utente
 * 
 * @author Massimiliano Randazzo
 *
 */
public abstract class StdModuliAuthentication extends StdModuliCore {

	/**
	 * Questa variabile viene utilizzata per loggare l'applicazione
	 */
	private Logger log = Logger.getLogger(StdModuliAuthentication.class);

	/**
	 * Questa variabie viene utilizzata per mantenere le informazioni relative
	 * all'autenticazione dell'utente
	 */
	private Cookie authentication = null;

	/**
	 * Questa variabile viene utilizzata per indicare l'URL del servizio di
	 * Autenticazione
	 */
	private String urlAuthentication = null;

	/**
	 * Questa variabile viene utilizzata per indicare l'archivio di appartenenza
	 */
	private String archive = "public";

	/**
	 * Questa variabile viene utilizzata per indicare il nome del Cookie di
	 * appartenenza
	 */
	private String cookieName = "BNCF_Authentication";

	/**
	 * Questa variabile viene utilizzata per gestire la risposta
	 * del'autenticazione
	 */
	protected net.bncf.uol2010.utility.xsd.authentication.Utente utenteXsd = null;

	protected boolean forceExecute = false;

	/**
	 * Costruttore
	 * @throws ServletException 
	 * 
	 */
	public StdModuliAuthentication() throws ServletException {
		super(null);
	}

	/**
	 * Questo metodo viene esteso per la gestione del modulo di Autenticazione
	 * Utente della Bncf
	 * 
	 * @see mx.servlet.moduli.standard.StdModuli#esegui(mx.servlet.moduli.servlet.MxMultipartRequest,
	 *      javax.servlet.http.HttpServletResponse, mx.database.ConnectionPool,
	 *      mx.database.ConnectionPool, java.lang.String, mx.security.Access)
	 */
	public void esegui(HttpServletRequest request, HttpServletResponse response, String pathXsl)
			throws ServletException, IOException {
		if ((isAutenticathed(request, response) || forceExecute))
			super.esegui(request, response, pathXsl);
		else
			eseguiAuthentication(request, response, pathXsl, request.getRemoteAddr());
	}

	/**
	 * Questo metodo viene utilizzato per verificare se l'utente si &egrave;
	 * gi&agrave; autenticato
	 * 
	 * @param request
	 * @return
	 */
	private boolean isAutenticathed(HttpServletRequest request, HttpServletResponse response) {
		boolean ris = false;

		log.debug("\n"+"iAuthenticathed()");
		if (request.getParameter("azione") != null && request.getParameter("azione").equals("logout")) {
			logOut(response);
			authentication = null;
		} else if (request.getCookies() != null) {
			for (int x = 0; x < request.getCookies().length; x++) {
				if (request.getCookies()[x].getName().equals(cookieName)) {
					ris = true;
					authentication = request.getCookies()[x];
				}
			}
		}
		log.debug("\n"+"Risultato: " + ris);
		return ris;
	}

	protected boolean isAutenticathed() {
		return authentication != null;
	}

	/**
	 * Questo metodo vine utilizzato per eseguire tutte le azioni relative alla
	 * autenticazione
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	private void eseguiAuthentication(HttpServletRequest request, HttpServletResponse response, String pathXsl,
			String ipStazione) throws ServletException, IOException {
		String azione = null;

		this.request = request;
		this.response = response;

		try {
			log.debug("\n"+"eseguiAuthentication");
			initAuthentication();

			azione = request.getParameter("_azione_");
			log.debug("\n"+"_azione_: " + azione);

			if (azione == null)
				azione = "show";

			if (azione.equals("show"))
				datiXml.addElement(element);
			else if (azione.equals("check"))
				check(request, response, pathXsl, ipStazione);
			else if (azione.equals("saveChange"))
				saveChange(request, response, pathXsl, ipStazione);
		} catch (ServletException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (authentication == null)
				endEsegui(pathXsl);
		}
	}

	private void saveChange(HttpServletRequest request, HttpServletResponse response, String pathXsl, String ipStazione)
			throws ServletException, IOException {
		ArrayList<MessageElement> nodes = null;

		/*
		 * Regole per la password dele essere compresa tra 8 e 20 caratteri, non
		 * puè contenere ne il nome o il cognome
		 */
		try {
			if (request.getParameter("_passwordAuthentication_").equals(request.getParameter("_confirmPassword_"))) {
				if (this.cookieName.equals("BNCF_AuthenticationBanco"))
					aggPwdBanco(request, response, pathXsl, ipStazione);
				else
					aggPwdPubblico(request, response, pathXsl, ipStazione);
			} else {
				nodes = new ArrayList<MessageElement>();
				datiXml.getConvert().addChildElement(element, nodes, "MessageError", "Nuova password non valida", true);
				datiXml.addElement(element);
			}
		} catch (SOAPException e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		} catch (ServletException e) {
			throw e;
		}
	}

	/**
	 * Questo metodo viene utilizzato per aggiornare la password dell'utente
	 * Pubblico
	 * 
	 * @param access
	 * @param ipStazione
	 * @throws ServletException
	 */
	private void aggPwdPubblico(HttpServletRequest request, HttpServletResponse response, String pathXsl,
			String ipStazione) throws ServletException {
		ArrayList<MessageElement> nodes = null;
		UtenteDAO utenteDAO = null;
		Utente utente = null;
		String md5Db = null;
		String md5Old = null;

		try {
			utenteDAO = new UtenteDAO();
			utente = utenteDAO.findById(request.getParameter("_loginAuthentication_"));
			if (utente != null) {
				md5Db = calcPwd(utente.getPassword());
				md5Old = calcPwd(request.getParameter("_oldPassword_"));

				if (md5Db.equals(md5Old)) {
					utente.setPassword(calcPwd(request.getParameter("_passwordAuthentication_")));
					utenteDAO.update(utente);
					check(request, response, pathXsl, ipStazione);
				} else {
					nodes = new ArrayList<MessageElement>();
					datiXml.getConvert().addChildElement(element, nodes, "MessageError",
							"La Vecchia password non \u00E8 valida", true);
					datiXml.addElement(element);
				}
			} else {
				nodes = new ArrayList<MessageElement>();
				datiXml.getConvert().addChildElement(element, nodes, "MessageError", "Utente indicato non Valido",
						true);
				datiXml.addElement(element);
			}
		} catch (ServletException e) {
			throw e;
		} catch (IOException e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		} catch (SOAPException e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		} catch (HibernateException e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		} catch (HibernateUtilException e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		}
	}

	/**
	 * Questo metodo viene utilizzato per aggiornare la password dell'utente
	 * Personale interno
	 * 
	 * @param access
	 * @param ipStazione
	 * @throws ServletException
	 */
	private void aggPwdBanco(HttpServletRequest request, HttpServletResponse response, String pathXsl,
			String ipStazione) throws ServletException {
		ArrayList<MessageElement> nodes = null;
		UtenteBibDAO utenteBibDAO = null;
		List<UtenteBib> utenteBibs = null;
		String md5Db = null;
		String md5Old = null;

		try {
			utenteBibDAO = new UtenteBibDAO();
			utenteBibs = utenteBibDAO.findByLogin(request.getParameter("_loginAuthentication_"));
			if (utenteBibs != null) {
				for (UtenteBib utenteBib: utenteBibs) {
					md5Db = calcPwd(utenteBib.getPassword());
					md5Old = calcPwd(request.getParameter("_oldPassword_"));
	
					if (md5Db.equals(md5Old)) {
						utenteBib.setPassword(calcPwd(request.getParameter("_passwordAuthentication_")));
						utenteBibDAO.update(utenteBib);
						check(request, response, pathXsl, ipStazione);
					} else {
						nodes = new ArrayList<MessageElement>();
						datiXml.getConvert().addChildElement(element, nodes, "MessageError",
								"La Vecchia password non \u00E8 valida", true);
						datiXml.addElement(element);
					}
				}
			} else {
				nodes = new ArrayList<MessageElement>();
				datiXml.getConvert().addChildElement(element, nodes, "MessageError", "Utente indicato non Valido",
						true);
				datiXml.addElement(element);
			}
		} catch (ServletException e) {
			throw e;
		} catch (IOException e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		} catch (SOAPException e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		} catch (HibernateException e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		} catch (HibernateUtilException e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		}
	}

	/**
	 * Questo metodo viene utilizzato per calcolare la password Criptata
	 * 
	 * @param pwd
	 *            Password da testare
	 * @return Password criptata
	 */
	private String calcPwd(String pwd) {

		if (pwd.length() != 32) {
			try {

				pwd = MD5Tools.checkSum(pwd.getBytes());
			} catch (NoSuchAlgorithmException e) {
				log.error(e.getMessage(),e);
			}
		}
		return pwd;
	}

	/**
	 * Questo metodo viene utilizzato per testare la validit&agrave; del login
	 * dell'utente
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	private void check(HttpServletRequest request, HttpServletResponse response, String pathXsl, String ipStazione)
			throws ServletException, IOException {
		UserValidator userValidator = null;
		ArrayList<MessageElement> nodes = null;
		MessageElement changePwd = null;
		String[] parole = null;
		String cookieValue = "";

		try {
			nodes = new ArrayList<MessageElement>();
			log.debug("\n"+"Url Authentication: " + urlAuthentication);
			userValidator = new UserValidator(urlAuthentication);

			log.debug("\n"+"Login: " + request.getParameter("_loginAuthentication_"));
			log.debug("\n"+"Passowd: " + request.getParameter("_passwordAuthentication_"));
			if (userValidator.validate(request.getParameter("_loginAuthentication_"),
					request.getParameter("_passwordAuthentication_"), request.getLocalAddr(), archive, ipStazione)) {
				log.debug("\n"+"OK");
				// TODO: welcome: 40be4e59b9a2a2b5dffb918c0e86b3d7
				if (request.getParameter("_passwordAuthentication_").equals("welcome")
						|| request.getParameter("_passwordAuthentication_").length() < 8) {
					changePwd = new MessageElement();
					changePwd.setName("changePwd");
					datiXml.getConvert().addChildElement(changePwd, nodes, "_loginAuthentication_",
							userValidator.getUtente().getLogin().getAnagrafica().getLogin().getValue(), false);
					datiXml.getConvert().addChildElement(changePwd, nodes, "_passwordAuthentication_",
							request.getParameter("_passwordAuthentication_"), false);

					parole = userValidator.getUtente().getLogin().getAnagrafica().getCognomeNome().split(" ");
					for (int x = 0; x < parole.length; x++)
						datiXml.getConvert().addChildElement(changePwd, nodes, "parola", parole[x], true);
					element.addChildElement(changePwd);
					datiXml.addElement(element);
				} else {
					cookieValue = userValidator.getUtente().getLogin().getAnagrafica().getLogin().getValue() + "#";
					cookieValue += userValidator.getUtente().getLogin().getAnagrafica().getCognomeNome();
					if (userValidator.getUtente().getLogin().getAutorizzazioni() != null
							&& userValidator.getUtente().getLogin().getAutorizzazioni().getNome() != null)
						cookieValue += "#" + userValidator.getUtente().getLogin().getAutorizzazioni().getNome();

					log.debug("\n"+"CoockieValue: " + cookieValue);
					authentication = new Cookie(cookieName, cookieValue);
					response.addCookie(authentication);
					utenteXsd= userValidator.getUtente();
					super.esegui(request, response, pathXsl);
				}
			} else {
				if (userValidator.getUtente() != null) {
					if (userValidator.getUtente().getMsgError() != null) {
						nodes = new ArrayList<MessageElement>();
						for (int x = 0; x < userValidator.getUtente().getMsgError().size(); x++) {
							log.error("\n"+"MessageError: " + userValidator.getUtente().getMsgError().get(x).getValue());
							datiXml.getConvert().addChildElement(element, nodes, "MessageError",
									userValidator.getUtente().getMsgError().get(x).getValue(), true);
						}
						datiXml.addElement(element);
					}
				}

			}
		} catch (UserValidatorException e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		} catch (ServletException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (SOAPException e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		}
	}

	protected void logOut() {
		this.logOut(response);
	}

	private void logOut(HttpServletResponse response) {
		authentication = new Cookie(cookieName, "");
		authentication.setMaxAge(0);
		response.addCookie(authentication);
	}

	/**
	 * 
	 * @throws ServletException
	 * @see mx.servlet.moduli.standard.StdModuli#endEsegui(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	protected void endEsegui(String pathXsl) throws ServletException {
		MessageElement parametri = null;
		String key = "";
		String[] values = null;
		ArrayList<MessageElement> nodes = null;

		try {
			if (authentication == null) {
				log.debug("\n"+"Authentication non TROVATOOOOOOOOOOOO");
				parametri = new MessageElement();
				parametri.setName("parametri");

				nodes = new ArrayList<MessageElement>();
				for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
					key = (String) e.nextElement();

					if (!key.equals("_azione_") && !key.equals("_loginAuthentication_")
							&& !key.equals("_passwordAuthentication_") && !key.equals("_confirmPassword_")
							&& !key.equals("_Login_") && !key.equals("_Reset_")) {
						values = request.getParameterValues(key);
						for (int x = 0; x < values.length; x++) {
							datiXml.getConvert().addChildElement(parametri, nodes, "parametro", values[x], "key", key,
									false, false);
						}
					}
				}
				datiXml.addElement(parametri);
			}
			super.endEsegui(pathXsl);
		} catch (SOAPException e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		} catch (ServletException e) {
			throw e;
		} catch (NoClassDefFoundError e) {
			log.error(e.getMessage(),e);
			// throw new ServletException(e.getMessage(),e);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage(), e);
		}
	}

	/**
	 * Questo metodo viene utilizzato per inizializzare le informazioni
	 * necessari per l'autenticazione
	 */
	private void initAuthentication() {
		log.debug("\n"+"initAuthentication");
		this.fileXsl = "Authentication.xsl";
		datiXml.setTitle("Authentication");
		datiXml.addStyleSheet("../style/Authentication.css");
		datiXml.addJavaScript("../js/Authentication.js");
		datiXml.addStyleSheet("../style/stdGraph/show/Ricerca.css");
		datiXml.addStyleSheet("../style/stdGraph/show/Risultati.css");

		element = new MessageElement();
		element.setName("authentication");

	}

	/**
	 * Questo metodo viene utilizzato per indicar l'URL di autenticazione.
	 * 
	 * @param urlAuthentication
	 */
	public void setUrlAuthentication(String urlAuthentication) {
		this.urlAuthentication = urlAuthentication;
	}

	/**
	 * Questo metodo restituisce il codice Identificativo dell'Utente
	 * 
	 * @return Codice Identficativo dell'utente
	 */
	protected String getIdUtente() {
		String[] st = null;

		if (authentication != null && authentication.getValue() != null
				&& !authentication.getValue().trim().equals("")) {
			log.debug("\n"+"authentication.getValue: " + authentication.getValue());
			st = authentication.getValue().split("#");
			return st[0];
		} else
			return null;
	}

	/**
	 * Questo metodo restituisce il Cognome nome dell'utente
	 * 
	 * @return Cognome Nome dell'utente
	 */
	protected String getCognomeNome() {
		String[] st = null;

		if (authentication != null && authentication.getValue() != null
				&& !authentication.getValue().trim().equals("")) {
			log.debug("\n"+"authentication.getValue: " + authentication.getValue());
			st = authentication.getValue().split("#");
			return st[1];
		} else
			return null;
	}

	/**
	 * Questo metodo restituisce il Cognome nome dell'utente
	 * 
	 * @return Cognome Nome dell'utente
	 */
	protected String getAutorizzazione() {
		String[] st = null;
		String ris = null;

		if (authentication != null && authentication.getValue() != null
				&& !authentication.getValue().trim().equals("")) {
			log.debug("\n"+"authentication.getValue: " + authentication.getValue());
			st = authentication.getValue().split("#");
			if (st.length > 2)
				ris = st[2];
		}
		return ris;
	}

	/**
	 * Questo metodo viene utilizzato per inizializzare i parametri per il Banco
	 * 
	 * @param archive
	 *            nome dell'archivio di riferimento
	 */
	public void initBanco(String archive) {
		this.archive = archive;
		this.cookieName = "BNCF_AuthenticationBanco";
	}

	/**
	 * Questo metodo viene utilizzato per inizializzare i parametri per la
	 * Richiesta
	 * 
	 * @param archive
	 *            nome dell'archivio di riferimento
	 */
	public void initRichieste(String archive) {
		this.archive = archive;
		this.cookieName = "BNCF_Authentication";
	}
}
