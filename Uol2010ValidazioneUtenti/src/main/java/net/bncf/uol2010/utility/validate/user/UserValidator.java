/**
 * 
 */
package net.bncf.uol2010.utility.validate.user;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;

import org.apache.log4j.Logger;

import net.bncf.uol2010.utility.crypting.exception.CryptingException;
import net.bncf.uol2010.utility.validate.user.exception.UserValidatorException;
import net.bncf.uol2010.utility.xsd.UtenteXsd;
import net.bncf.uol2010.utility.xsd.authentication.Utente;

/**
 * Questa classe viene utlizata per la validazione degli utenti
 * 
 * @author Massimiliano Randazzo
 *
 */
public class UserValidator {

	/**
	 * Questa variabile viene utilizzata per loggare l'applicazione
	 */
	private Logger log = Logger.getLogger(UserValidator.class);

	/**
	 * Questa variabile viene uilizzata per gestire il risultato della validazione
	 * dell'utente
	 */
	private Utente utenteXsd = null;

	/**
	 * Questa variabile vine utilizzata per indicare la l'url del modulo di
	 * autenticazione
	 */
	private String urlAuthentication = null;

	/**
	 * Costruttore
	 * 
	 * @param urlAuthentication
	 *            Url del modulo di autenticazione
	 */
	public UserValidator(String urlAuthentication) {
		this.urlAuthentication = urlAuthentication;
	}

	/**
	 * Questo mtodo viene invocato quando la classe viene utilizzata come
	 * applicazione
	 * 
	 * @param args
	 *            Lista argomenti passati all'applicazione
	 */
	public static void main(String[] args) {
		UserValidator userValidator = null;

		try {
			userValidator = new UserValidator("http://authentication.bncf.lan/Authentication/servlet/Authentication");
			// userValidator = new
			// UserValidator("http://192.168.254.100:8080/Authentication/servlet/Authentication");
			// userValidator = new
			// UserValidator("http://localhost:8080/Authentication/servlet/Authentication");
			// userValidator = new
			// UserValidator("http://192.168.233.165/Authentication/servlet/Authentication");
			// md5 = new MD5();
			// md5.Update("VELA".getBytes());
			// if (userValidator.validate("CFU8", md5.asHex(), "192.168.1.85", "public2010",
			// "192.168.1.188"))
			if (userValidator.validate("Randazzo", "Andromeda", "192.168.1.85", "intranet2010", "192.168.1.188"))
			// if (userValidator.validate("Randazzo", "Andromeda", "127.0.0.1", "intranet",
			// "192.168.1.188"))
			// if (userValidator.validate("CFU8", "VELA", "127.0.0.1", "public",
			// "192.168.1.188"))
			{
				System.out.println("Esito Positivo");
				// System.out.println("Cod_Bib_Ut:
				// "+userValidator.getUtenteXsd().getLogin().getAnagrafica().getLogin().getCodBibUt());
				// System.out.println("Cod_Utente:
				// "+userValidator.getUtenteXsd().getLogin().getAnagrafica().getLogin().getCodUtente());
				System.out.println(
						"Id Utente: " + userValidator.getUtente().getLogin().getAnagrafica().getLogin().getValue());
				System.out.println(
						"Cognome Nome: " + userValidator.getUtente().getLogin().getAnagrafica().getCognomeNome());
				if (userValidator.getUtente().getLogin().getArchive().startsWith("intranet")) {
					System.out.println("Nome Autorizzazione: "
							+ userValidator.getUtente().getLogin().getAutorizzazioni().getNome());
					for (int x = 0; x < userValidator.getUtente().getLogin().getAutorizzazioni().getDiritto()
							.size(); x++)
						System.out.println("Diritto " + (x + 1) + "/"
								+ userValidator.getUtente().getLogin().getAutorizzazioni().getDiritto().size() + " = "
								+ userValidator.getUtente().getLogin().getAutorizzazioni().getDiritto().get(x).getID()
								+ " -> " + userValidator.getUtente().getLogin().getAutorizzazioni().getDiritto().get(x)
										.getValue());
				}
			} else {
				System.out.println("Esisto Negativo");
				if (userValidator.getUtente() != null) {
					if (userValidator.getUtente().getMsgError() != null) {
						for (int x = 0; x < userValidator.getUtente().getMsgError().size(); x++) {
							System.out.println(userValidator.getUtente().getMsgError().get(x).getValue());
						}
					}
				}
			}
			/*
			 * if (userValidator.validate("CFU8", "VELA", "127.0.0.1", "public")) {
			 * System.out.println("Esito Positivo");
			 * System.out.println("Cod_Bib_Ut: "+userValidator.getUtenteXsd().getLogin().
			 * getAnagrafica().getLogin().getCodBibUt());
			 * System.out.println("Cod_Utente: "+userValidator.getUtenteXsd().getLogin().
			 * getAnagrafica().getLogin().getCodUtente());
			 * System.out.println("Id Utente: "+userValidator.getUtenteXsd().getLogin().
			 * getAnagrafica().getLogin().getValue());
			 * System.out.println("Cognome Nome: "+userValidator.getUtenteXsd().getLogin().
			 * getAnagrafica().getCognomeNome()); } else {
			 * System.out.println("Esisto Negativo"); if (userValidator.getUtenteXsd() !=
			 * null) { if (userValidator.getUtenteXsd().getMsgError() != null) { for (int
			 * x=0; x<userValidator.getUtenteXsd().getMsgError().size(); x++) {
			 * System.out.println(userValidator.getUtenteXsd().getMsgError().get(x).getValue
			 * ()); } } } }
			 */
		} catch (UserValidatorException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Questo metodo viene utilizzato per eseguire la validazione dell'utente
	 * 
	 * @param login
	 *            Login da verificare
	 * @param password
	 *            Password da verificare
	 * @param ipClient
	 *            Indirizzo IP del client chiamante
	 * @param archive
	 *            Tipo di archivio da verificare
	 * @return Indica se la verifica &egrave; stata positiva o negativa
	 * @throws UserValidatorException
	 */
	public boolean validate(String login, String password, String ipClient, String archive)
			throws UserValidatorException {
		return validate(login, password, ipClient, archive, null);
	}

	/**
	 * Questo metodo viene utilizzato per eseguire la validazione dell'utente
	 * 
	 * @param login
	 *            Login da verificare
	 * @param password
	 *            Password da verificare
	 * @param ipClient
	 *            Indirizzo IP del client chiamante
	 * @param archive
	 *            Tipo di archivio da verificare
	 * @return Indica se la verifica &egrave; stata positiva o negativa
	 * @throws UserValidatorException
	 */
	public boolean validate(String login, String password, String ipClient, String archive, String ipStazione)
			throws UserValidatorException {
		boolean ris = false;
		URL url = null;
		URLConnection urlConnection = null;
		UtenteXsd utenteXsd = null;
		InputStream is = null;
		OutputStreamWriter osw = null;
		String xml = "";

		try {
			utenteXsd = new UtenteXsd(login, password, ipClient, archive, ipStazione);
			log.debug("\n"+"Url Authentication: " + urlAuthentication);

			url = new URL(urlAuthentication);
			urlConnection = url.openConnection();
			urlConnection.setDoOutput(true);
			osw = new OutputStreamWriter(urlConnection.getOutputStream());
			xml = utenteXsd.writeToString();
			log.debug("\n"+"Xml: " + xml);
			osw.write("Xml=" + xml);
			osw.flush();
			osw.close();
			// this.utenteXsd = new UtenteXsd();
			is = urlConnection.getInputStream();
			this.utenteXsd = utenteXsd.read(is);

			if (this.utenteXsd.getMsgError() == null || this.utenteXsd.getMsgError().size() == 0)
				ris = true;
			else {
				for (int x = 0; x < this.utenteXsd.getMsgError().size(); x++)
					log.info("\n"+"Msg Err: " + this.utenteXsd.getMsgError().get(x).getId() + " - "
							+ this.utenteXsd.getMsgError().get(x).getValue());
			}
		} catch (MalformedURLException e) {
			log.error(e.getMessage(), e);
			throw new UserValidatorException(e.getMessage());
		} catch (PropertyException e) {
			log.error(e.getMessage(), e);
			throw new UserValidatorException(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new UserValidatorException(e.getMessage());
		} catch (JAXBException e) {
			log.error(e.getMessage(), e);
			throw new UserValidatorException(e.getMessage());
		} catch (CryptingException e) {
			log.error(e.getMessage(), e);
			throw new UserValidatorException(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new UserValidatorException(e.getMessage(), e);
		} finally {
			try {
				if (is != null)
					is.close();
				is = null;
				osw = null;
				System.gc();
			} catch (IOException e) {
				log.error(e);
				throw new UserValidatorException(e.getMessage());
			}
		}

		return ris;
	}

	/**
	 * Questo metodo viene utilizzato per restituire la risposta della validazione
	 * 
	 * @return
	 */
	public Utente getUtente() {
		return utenteXsd;
	}
}
