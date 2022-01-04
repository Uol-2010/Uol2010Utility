/**
 * 
 */
package net.bncf.uol2010.utility.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

//import org.apache.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Questa classe viene utilizzata per gestire l'autenticzione del server Smtp
 * 
 * @author Massimiliano Randazzo
 *
 */
class SmtpAuthenticion extends Authenticator
{

	/**
	 * Questa variabile vinee utilizzata per loggare l'applicazione
	 */
	private static Logger log = LogManager.getLogger(SmtpAuthenticion.class);

	/**
	 * Questa variabile viene utilizzata per indicare il login per il server smtp
	 */
	private String smtpLogin = null;

	/**
	 * Questa variabile viene utilizzata per indicare la password per il server smtp
	 */
	private String smtpPassword = null;

	/**
	 * Costruttore
	 * 
	 * @param smtpLogin Questa variabile viene utilizzata per indicare il login per il server smtp
	 * @param smtpPassword Questa variabile viene utilizzata per indicare la password per il server smtp
	 */
	public SmtpAuthenticion(String smtpLogin, String smtpPassword)
	{
		this.smtpLogin = smtpLogin;
		this.smtpPassword = smtpPassword;
	}

	/**
	 * Questo metodo viene utilizzato per reperire le informazioni per l'autenticazione 
	 * 
	 * @see javax.mail.Authenticator#getPasswordAuthentication()
	 */
	@Override
	protected PasswordAuthentication getPasswordAuthentication()
	{
		log.debug("Login: '"+smtpLogin+"' Pasword: '"+smtpPassword+"'");
		return new PasswordAuthentication(smtpLogin, smtpPassword);
	}
}
