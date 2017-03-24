/**
 * 
 */
package net.bncf.uol2010.businessLogin.exception;

/**
 * @author massi
 *
 */
public class BusinessLogicException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3768410450602495389L;

	/**
	 * @param message
	 */
	public BusinessLogicException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public BusinessLogicException(String message, Throwable cause) {
		super(message, cause);
	}

}
