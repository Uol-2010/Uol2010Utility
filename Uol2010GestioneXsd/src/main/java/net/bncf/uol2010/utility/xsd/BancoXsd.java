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

import org.apache.log4j.Logger;

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
	private Logger log = Logger.getLogger(BancoXsd.class);

	/**
	 * Questa variabile viene utilizzata per gestire la richiesta del materiale
	 */
	private Banco banco = null;

	/**
	 * 
	 */
	public BancoXsd()
	{
		log.debug("Cotruttore");
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
			log.error(e);
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
				log.error(e);
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
			log.debug("read(InputStream "+input+")");
			if (input != null)
			{
				log.debug("Richieste.package: "+Banco.class.getPackage().getName());
				jc = JAXBContext.newInstance(Banco.class.getPackage().getName());

				log.debug("jc.createUnmarshaller");
				u = jc.createUnmarshaller();

				log.debug("u.unmarchal("+input+")");
				banco = (Banco)u.unmarshal(input);
			}
		}
		catch (JAXBException e)
		{
			log.error(e);
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
			log.debug("writeToString()");

			log.debug("Utente.package: "+Banco.class.getPackage().getName());
			jc = JAXBContext.newInstance(Banco.class.getPackage().getName());

			log.debug("jc.createMarshaller();");
			m = jc.createMarshaller();

			log.debug("m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);");
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);

			baos =  new ByteArrayOutputStream();
			log.debug("m.marshal( utente, baos );");
			m.marshal( banco, baos );
		}
		catch (PropertyException e)
		{
			log.error(e);
			throw e;
		}
		catch (JAXBException e)
		{
			log.error(e);
			throw e;
		}
		catch (Exception e)
		{
			log.error(e);
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
