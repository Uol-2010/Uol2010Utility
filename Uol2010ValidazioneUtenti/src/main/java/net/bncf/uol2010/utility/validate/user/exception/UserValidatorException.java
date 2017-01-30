/**
 * 
 */
package net.bncf.uol2010.utility.validate.user.exception;

/**
 * Questo metodo viene utilizzato per gestire gli Errori del modulo di Validazione Utente
 * 
 * @author Massimiliano Randazzo
 *
 */
public class UserValidatorException extends Exception
{

	/**
	 * Questa variabile viene utilizzata per indicar il Serial Version UID
	 */
	private static final long serialVersionUID = -5035097951671956013L;

	/**
	 * Costruttore
	 * 
	 * @param message Messaggio di Errore
	 */
	public UserValidatorException(String message)
	{
		super(message);
	}

	/**
	 * Costruttore
	 * 
	 * @param message Messaggio di Errore
	 */
	public UserValidatorException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
