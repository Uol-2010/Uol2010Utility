/**
 * 
 */
package net.bncf.uol2010.utility.crypting;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.bncf.uol2010.utility.crypting.exception.CryptingException;

/**
 * @author MRandazzo
 *
 */
public class Crypting
{

	/**
	 * 
	 */
	private Logger log = LogManager.getLogger(Crypting.class);

	private String key = "01234567890abcde";
	private String iv  = "fedcba9876543210";
	
	private String[] decrypt = null;
	
	/**
	 * 
	 */
	public Crypting()
	{
	}

	public Crypting(String key, String iv)
	{
		this.key = key;
		this.iv  = iv;
	}


	public synchronized String encrypt(String data) throws CryptingException
	{
		String key = "";
		String encrypt = "";
		GregorianCalendar gc = new GregorianCalendar();
		DecimalFormat df2 = new DecimalFormat("00");
		DecimalFormat df3 = new DecimalFormat("000");
		DecimalFormat df4 = new DecimalFormat("0000");

		try
		{
			gc.setTimeZone(SimpleTimeZone.getTimeZone("GMT"));
			gc.add(Calendar.SECOND, 40);

			key = df3.format(data.length());
			key += data;
			key += df4.format(gc.get(Calendar.YEAR));
			key += df2.format(gc.get(Calendar.MONTH) + 1);
			key += df2.format(gc.get(Calendar.DAY_OF_MONTH));
			key += df2.format(gc.get(Calendar.HOUR_OF_DAY));
			key += df2.format(gc.get(Calendar.MINUTE));
			key += df2.format(gc.get(Calendar.SECOND));
			log.debug("\n"+"Key: "+key);
			encrypt = encryptData(key);
//			encrypt = encryptData("004VELA20091013170634");
		}
		catch (CryptingException e)
		{
			throw e;
		}
		return encrypt;
	}

	/**
	 * Questo metodo viene utilizzato per la criptazione del testo
	 * 
	 * @param testo
	 * @return
	 * @throws CryptingException
	 */
	private String encryptData(String testo) throws CryptingException
	{
		String ris = null;
		
		try
		{
			log.debug("\n"+"testo: "+testo+" - "+testo.length()+" - "+(testo.length()%16));
			

			while ((testo.length()%16)!=0)
				testo += " ";
			log.debug("\n"+"testo: "+testo+" - "+testo.length());
			Cipher cipher = genCipher(Cipher.ENCRYPT_MODE);
			byte[] encrypted = cipher.doFinal(testo.getBytes());

			ris = bytesToHex(encrypted);
		}
		catch (InvalidKeyException e)
		{
			log.error(e.getMessage(),e);
			throw new CryptingException(e.getMessage());
		}
		catch (NoSuchAlgorithmException e)
		{
			log.error(e.getMessage(),e);
			throw new CryptingException(e.getMessage());
		}
		catch (NoSuchPaddingException e)
		{
			log.error(e.getMessage(),e);
			throw new CryptingException(e.getMessage());
		}
		catch (InvalidAlgorithmParameterException e)
		{
			log.error(e.getMessage(),e);
			throw new CryptingException(e.getMessage());
		}
		catch (IllegalBlockSizeException e)
		{
			log.error(e.getMessage(),e);
			throw new CryptingException(e.getMessage());
		}
		catch (BadPaddingException e)
		{
			log.error(e.getMessage(),e);
			throw new CryptingException(e.getMessage());
		}

		return ris;
	}

	public synchronized void decrypt(String data) throws CryptingException
	{
		String decrypt = "";

		try
		{
			decrypt = decryptData(data);

			log.debug("\n"+"decrypt: "+decrypt);
			this.decrypt = new String[3];
			this.decrypt[0] = decrypt.substring(0, 3);
			decrypt = decrypt.substring(3);
			this.decrypt[1] = decrypt.substring(0, Integer.valueOf(this.decrypt[0]));
			this.decrypt[2] = decrypt.substring(Integer.valueOf(this.decrypt[0]));
			
		}
		catch (CryptingException e)
		{
			throw e;
		}
		catch (NumberFormatException e)
		{
			log.error(e.getMessage(),e);
			throw new CryptingException(e.getMessage());
		}
	}

	private  String decryptData(String testo) throws CryptingException
	{
		String ris = null;
		try
		{
			log.debug("\n"+"testoXX: "+testo);
			Cipher cipher = genCipher(Cipher.DECRYPT_MODE);
			byte[] outText = cipher.doFinal(hexToBytes(testo));
			ris = new String(outText).trim();
			log.debug("\n"+"risXX: "+ris);
		}
		catch (InvalidKeyException e)
		{
			log.error(e.getMessage(),e);
			throw new CryptingException(e.getMessage());
		}
		catch (NoSuchAlgorithmException e)
		{
			log.error(e.getMessage(),e);
			throw new CryptingException(e.getMessage());
		}
		catch (NoSuchPaddingException e)
		{
			log.error(e.getMessage(),e);
			throw new CryptingException(e.getMessage());
		}
		catch (InvalidAlgorithmParameterException e)
		{
			log.error(e.getMessage(),e);
			throw new CryptingException(e.getMessage());
		}
		catch (IllegalBlockSizeException e)
		{
			log.error(e.getMessage(),e);
			throw new CryptingException(e.getMessage());
		}
		catch (BadPaddingException e)
		{
			log.error(e.getMessage(),e);
			throw new CryptingException(e.getMessage());
		}
		return ris;
	}

	private Cipher genCipher(int mode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException
	{
		SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
		IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		cipher.init(mode, keyspec, ivspec);
		return cipher;

	}

	/**
	 * Questo metodo viene utilizzato per convertire un byte Array in Esadecimale
	 * 
	 * @param data Testo da convertire
	 * @return Testo convertito
	 */
	private String bytesToHex(byte[] data) 
	{
		if (data==null) 
		{
			return null;
		} 
		else 
		{
			int len = data.length;
			String str = "";
			for (int i=0; i<len; i++) 
			{
				if ((data[i]&0xFF)<16)
					str = str + "0" + java.lang.Integer.toHexString(data[i]&0xFF);
				else
					str = str + java.lang.Integer.toHexString(data[i]&0xFF);
			}
			return str;
		}
	}

	/**
	 * Questo metodo converte un testo esadecimale in byte Array
	 * 
	 * @param str Testo da convertire
	 * @return Testo convertito
	 */
	private byte[] hexToBytes(String str)
	{
		if (str == null)
		{
			return null;
		}
		else if (str.length() < 2)
		{
			return null;
		}
		else
		{
			int len = str.length() / 2;
			byte[] buffer = new byte[len];
			for (int i = 0; i < len; i++)
			{
				buffer[i] = (byte) Integer
						.parseInt(str.substring(i * 2, i * 2 + 2), 16);
			}
			return buffer;
		}
	}

	public String getPassword()
	{
		return decrypt[1];
	}

	public boolean isValid()
	{
		GregorianCalendar gcNow = null;
		GregorianCalendar gcCheck = null;
		boolean ris = false;
		
		if (decrypt != null)
		{
			gcCheck = new GregorianCalendar(Integer.valueOf(decrypt[2].substring(0,4)),
					Integer.valueOf(decrypt[2].substring(4,6))-1,
					Integer.valueOf(decrypt[2].substring(6,8)),
					Integer.valueOf(decrypt[2].substring(8,10)),
					Integer.valueOf(decrypt[2].substring(10,12)),
					Integer.valueOf(decrypt[2].substring(12,14)));
			gcCheck.setTimeZone(SimpleTimeZone.getTimeZone("GMT"));
			gcNow = new GregorianCalendar(SimpleTimeZone.getTimeZone("GMT"));
			log.debug("\n"+"gcCheck: "+gcCheck.toString()+" - "+gcCheck.getTimeInMillis());
			log.debug("\n"+"gcNow: "+gcNow.toString()+" - "+gcNow.getTimeInMillis());
			log.debug("\n"+"Diff.: "+(gcCheck.getTimeInMillis()-gcNow.getTimeInMillis()));
			log.debug("\n"+"Test.: "+(gcCheck.getTimeInMillis()>=gcNow.getTimeInMillis()));
			if (gcCheck.getTimeInMillis()>=gcNow.getTimeInMillis())
				ris=true;
		}
		return ris;
	}
}
