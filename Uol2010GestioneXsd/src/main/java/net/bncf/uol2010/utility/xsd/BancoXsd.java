/**
 * 
 */
package net.bncf.uol2010.utility.xsd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.bncf.uol2010.utility.xsd.banco.Banco;

/**
 * @author massi
 *
 */
public class BancoXsd
{

	/**
	 * Questa variabile viene utlizzata per loggare l'applicazione
	 */
	private Logger log = LogManager.getLogger(BancoXsd.class);

	/**
	 * Questa variabile viene utilizzata per gestire la richiesta del materiale
	 */
	private Banco banco = null;

	/**
	 * 
	 */
	public BancoXsd()
	{
		log.debug("\n"+"Cotruttore");
	}

	public void readXml(String xml) throws JAXBException
	{
		ByteArrayInputStream bais = null;
		
		try
		{
			bais = new ByteArrayInputStream(xml.getBytes());
			read(bais);
		}
		catch (JAXBException e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			try
			{
				if (bais != null)
					bais.close();
			}
			catch (IOException e)
			{
				log.error(e.getMessage(), e);
				throw new JAXBException(e.getMessage());
			}
		}
	}

	public void read(InputStream input) throws JAXBException
	{
		JAXBContext jc = null;
		Unmarshaller u = null;

		try
		{
			log.debug("\n"+"read(InputStream "+input+")");
			if (input != null)
			{
				log.debug("\n"+"Richieste.package: "+Banco.class.getPackage().getName());
				jc = JAXBContext.newInstance(Banco.class.getPackage().getName());

				log.debug("\n"+"jc.createUnmarshaller");
				u = jc.createUnmarshaller();

				log.debug("\n"+"u.unmarchal("+input+")");
				banco = (Banco)u.unmarshal(input);
			}
		}
		catch (JAXBException e)
		{
			log.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Questo metodo viene utilizzato per scriver le informazioni nel file Xml
	 * 
	 * @throws PropertyException
	 * @throws JAXBException
	 * @throws Exception
	 */
	public String writeToString() throws PropertyException, JAXBException, Exception
	{
		Marshaller m = null;
		JAXBContext jc = null;
		ByteArrayOutputStream baos = null;
		
		try
		{
			log.debug("\n"+"writeToString()");

			log.debug("\n"+"Utente.package: "+Banco.class.getPackage().getName());
			jc = JAXBContext.newInstance(Banco.class.getPackage().getName());

			log.debug("\n"+"jc.createMarshaller();");
			m = jc.createMarshaller();

			log.debug("\n"+"m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);");
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);

			baos =  new ByteArrayOutputStream();
			log.debug("\n"+"m.marshal( utente, baos );");
			m.marshal( banco, baos );
		}
		catch (PropertyException e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		catch (JAXBException e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (baos != null)
				baos.flush();
		}
		if (baos !=null)
			return baos.toString();
		else
			return null;
	}

	/**
	 * @return the richieste
	 */
	public void setBanco(Banco richieste)
	{
		this.banco = richieste;
	}

	/**
	 * @return the richieste
	 */
	public Banco getBanco()
	{
		return banco;
	}
}
