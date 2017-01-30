/**
 * 
 */
package net.bncf.uol2010.utility.crypting.exception;

/**
 * Questoa classe vine utilizzata per la gestione degli errori generati durante le procedura di criptaggio e decriptaggio
 * 
 * @author Massimiliano Randazzo
 *
 */
public class CryptingException extends Exception
{

	/**
	 * Questa variabile viene utilizzata per indicare il Serial Version UID
	 */
	private static final long serialVersionUID = 6972454717879855460L;

	/**
	 * @param message
	 */
	public CryptingException(String message)
	{
		super(message);
	}

}
