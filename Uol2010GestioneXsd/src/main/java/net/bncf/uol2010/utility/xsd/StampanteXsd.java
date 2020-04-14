/**
 * 
 */
package net.bncf.uol2010.utility.xsd;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.bncf.uol2010.utility.xsd.stampante.Stampante;

/**
 * Questo metodo viene utilizzato per gestire il file Xml Extra per Amicus
 * 
 * @author Massimiliano Randazzo
 *
 */
public class StampanteXsd
{

	private Logger log = LogManager.getLogger(StampanteXsd.class);

	/**
	 * Nome del file Xml
	 */
	private String fileXml = null;

	/**
	 * Oggetto rincipale del file Xml
	 */
	private Stampante stampante = null;

	private boolean fileBackup = true;

	/**
	 * Costruttore
	 */
	public StampanteXsd()
	{
		log.debug("\n"+"Costrutore");
	}

	/**
	 * Questo metodo viene utilizzato per leggere il file Xml
	 * 
	 * @param fileXml File Xml
	 * @throws JAXBException 
	 */
	public void read(File fileXml) throws JAXBException
	{
		JAXBContext jc = null;
		Unmarshaller u = null;

		try
		{
			log.debug("\n"+"read(File "+fileXml+")");
			this.fileXml = fileXml.getAbsolutePath();
			log.debug("\n"+"fileXml: "+this.fileXml);
			log.debug("\n"+"fileXml.exists: "+fileXml.exists());
			if (fileXml.exists())
			{
				log.debug("\n"+"Utente.package: "+Stampante.class.getPackage().getName());
				jc = JAXBContext.newInstance(Stampante.class.getPackage().getName());

				log.debug("\n"+"jc.createUnmarshaller()");
				u = jc.createUnmarshaller();

				log.debug("\n"+"u.unmarshal("+fileXml+")");
				stampante = (Stampante) u.unmarshal(fileXml);
			}
		}
		catch (JAXBException e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		
	}

	/**
	 * Questo metodo viene utilizzato per leggere il tracciato Xml
	 * 
	 * @param fileXml Tracciato xml
	 * @throws JAXBException
	 */
	public void read(InputStream fileXml) throws JAXBException
	{
		JAXBContext jc = null;
		Unmarshaller u = null;

		try
		{
			log.debug("\n"+"read(InputStream "+fileXml+");");
			if (fileXml != null)
			{
				log.debug("\n"+"Utente.package: "+Stampante.class.getPackage().getName());
				jc = JAXBContext.newInstance(Stampante.class.getPackage().getName());

				log.debug("\n"+"jc.createUnmarshaller()");
				u = jc.createUnmarshaller();

				log.debug("\n"+"u.unmarshal("+fileXml+")");
				stampante = (Stampante) u.unmarshal(fileXml);
			}
		}
		catch (JAXBException e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		
	}

	/**
	 * Questo metodo viene utilizzato per scrivere le informazioni nel file Xml Specificando il nuovo nome
	 * 
	 * @param fileXml
	 * @param fileBackup
	 * @throws PropertyException
	 * @throws JAXBException
	 * @throws Exception
	 */
	public void write(String fileXml, boolean fileBackup) throws PropertyException, JAXBException, Exception
	{
		log.debug("\n"+"write("+fileXml+", "+fileBackup+")");
		this.fileBackup=fileBackup;
		write(fileXml);
	}

	/**
	 * Questo metodo viene utilizzato per scrivere le informazioni nel file Xml Specificando il nuovo nome
	 * 
	 * @param fileXml Nome del file Xml
	 * @throws PropertyException
	 * @throws JAXBException
	 * @throws Exception
	 */
	public void write(String fileXml) throws PropertyException, JAXBException, Exception
	{
		log.debug("\n"+"write("+fileXml+")");
		this.fileXml = fileXml;
		write();
	}

	/**
	 * Questo metodo viene utilizzato per scriver le informazioni nel file Xml
	 * 
	 * @throws PropertyException
	 * @throws JAXBException
	 * @throws Exception
	 */
	public void write() throws PropertyException, JAXBException, Exception
	{
		Marshaller m = null;
		JAXBContext jc = null;
		File fOut = null;
		FileOutputStream fos = null;
		File fOutBck = null;
		
		try
		{
			log.debug("\n"+"write()");

			log.debug("\n"+"Utente.package: "+Stampante.class.getPackage().getName());
			jc = JAXBContext.newInstance(Stampante.class.getPackage().getName());

			log.debug("\n"+"jc.createUnmarshaller()");
			m = jc.createMarshaller();

			log.debug("\n"+"m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);");
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);

			log.debug("\n"+"fileXml: "+fileXml);
			fOut =  new File(fileXml);
			if (!fOut.getParentFile().exists())
				if (!fOut.getParentFile().mkdirs())
					throw new Exception("Problemi nella creazone della cartella ["+fOut.getParentFile().getAbsolutePath()+"]");

			log.debug("\n"+"fOut.exists(): "+fOut.exists());
			if (fOut.exists() && fileBackup)
			{
				fOutBck = genFileBck(fOut);
				log.debug("\n"+"fOutBck: "+fOutBck);
				if (!fOutBck.getParentFile().exists())
					if (!fOutBck.getParentFile().mkdirs())
						throw new Exception("Problemi nella creazone della cartella ["+fOutBck.getParentFile().getAbsolutePath()+"]");

				log.debug("\n"+"!fOut.renameTo(fOutBck)");
				if (!fOut.renameTo(fOutBck))
					throw new Exception("Problemi nello spostamento del file "+fOut.getAbsolutePath()+" -> "+fOutBck.getAbsolutePath());
			}

			log.debug("\n"+"FileOutputStream(fOut)");
			fos = new FileOutputStream(fOut);

			log.debug("\n"+"m.marshal( utente, fos )");
			m.marshal( stampante, fos );
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
			if (fos != null)
			{
				fos.flush();
				fos.close();
			}
		}
	}

	/**
	 * Questo metodo viene utilizzato per scriver le informazioni nel file Xml
	 * 
	 * @throws PropertyException
	 * @throws JAXBException
	 * @throws Exception
	 */
	public void write(OutputStream output) throws PropertyException, JAXBException, Exception
	{
		Marshaller m = null;
		JAXBContext jc = null;
		
		try
		{
			log.debug("\n"+"write(OutputStream output)");
			log.debug("\n"+"Utente.package: "+Stampante.class.getPackage().getName());
			jc = JAXBContext.newInstance(Stampante.class.getPackage().getName());

			log.debug("\n"+"jc.createMarshaller();");
			m = jc.createMarshaller();

			log.debug("\n"+"m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);");
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);

			log.debug("\n"+"m.marshal( utente, output );");
			m.marshal( stampante, output );
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

			log.debug("\n"+"Utente.package: "+Stampante.class.getPackage().getName());
			jc = JAXBContext.newInstance(Stampante.class.getPackage().getName());

			log.debug("\n"+"jc.createMarshaller();");
			m = jc.createMarshaller();

			log.debug("\n"+"m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);");
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);

			baos =  new ByteArrayOutputStream();
			log.debug("\n"+"m.marshal( utente, baos );");
			m.marshal( stampante, baos );
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
	 * Questo metodo viene utilizzato per generare il nome del file di backup 
	 * 
	 * @param fOut Nome del file di output
	 * @return
	 */
	private File genFileBck(File fOut)
	{
		String pathXml = null;
		GregorianCalendar gc = new GregorianCalendar();
		DecimalFormat df2 = new DecimalFormat("00");
		DecimalFormat df3 = new DecimalFormat("=00");

		pathXml = fOut.getParentFile().getAbsolutePath();
		pathXml += File.separator;
		pathXml += fOut.getName().replace(".xml", ".bck");
		pathXml += File.separator;
		pathXml += gc.get(Calendar.YEAR);
		pathXml += File.separator;
		pathXml += df2.format(Integer.toString((gc.get(Calendar.MONTH)+1)));
		pathXml += File.separator;
		pathXml += df2.format(Integer.toString((gc.get(Calendar.DAY_OF_MONTH))));
		pathXml += File.separator;
		pathXml += fOut.getName().replace(".xml", "_");
		pathXml += gc.get(Calendar.YEAR);
		pathXml += df2.format(Integer.toString((gc.get(Calendar.MONTH)+1)));
		pathXml += df2.format(Integer.toString((gc.get(Calendar.DAY_OF_MONTH))));
		pathXml += df2.format(Integer.toString((gc.get(Calendar.HOUR_OF_DAY))));
		pathXml += df2.format(Integer.toString((gc.get(Calendar.MINUTE))));
		pathXml += df2.format(Integer.toString((gc.get(Calendar.SECOND))));
		pathXml += df3.format(Integer.toString((gc.get(Calendar.MILLISECOND))));
		pathXml += ".xml";
		return new File(pathXml);
	}

	/**
	 * Questo metodo viene utilizzato per leggere l'ogetto Utente 
	 * 
	 * @return
	 */
	public Stampante getStampante()
	{
		log.debug("\n"+"getStampante()");
		if (stampante == null)
			stampante = new Stampante();
		return stampante;
	}

	/**
	 * Questo metodo viene utilizzato per leggere l'ogetto Utente 
	 * 
	 * @return
	 */
	public void setUtente(Stampante stampante)
	{
		log.debug("\n"+"setUtente()");
		this.stampante = stampante;
	}


	/**
	 * @param stampante the stampante to set
	 */
	public void setStampante(Stampante stampante)
	{
		this.stampante = stampante;
	}
}
