/**
 * 
 */
package net.bncf.uol2010.utility.xsd;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import org.apache.log4j.Logger;

import mx.randalf.xsd.ReadXsd;
import net.bncf.uol2010.utility.crypting.Crypting;
import net.bncf.uol2010.utility.crypting.exception.CryptingException;
import net.bncf.uol2010.utility.xsd.authentication.Login;
import net.bncf.uol2010.utility.xsd.authentication.ObjectFactory;
import net.bncf.uol2010.utility.xsd.authentication.Utente;
/**
 * Questo metodo viene utilizzato per gestire il file Xml Extra per Amicus
 * 
 * @author Massimiliano Randazzo
 *
 */
public class UtenteXsd extends ReadXsd<Utente>
{

	private Logger log = Logger.getLogger(UtenteXsd.class);

	/**
	 * Nome del file Xml
	 */
	private String fileXml = null;

	/**
	 * Oggetto rincipale del file Xml
	 */
	private Utente utente = null;

	/**
	 * Costruttore
	 */
	public UtenteXsd()
	{
		log.debug("\n"+"Costrutore");
	}

	/**
	 * Costruttore
	 * @throws CryptingException 
	 */
	public UtenteXsd(String login, String password, String ipClient, String archive, String ipStazione) throws CryptingException
	{
		ObjectFactory of = null;
		Crypting crypting = null;
		
		try
		{
			log.debug("\n"+"UtenteXsd("+login+", "+password+", "+ipClient+", "+archive+", "+ipStazione+") throws CryptingException");
			of = new ObjectFactory();
			utente = of.createUtente();
			utente.setLogin(of.createLogin());
			log.debug("\n"+"utente.getLogin().setLogin("+login+");");
			utente.getLogin().setLogin(login);

			crypting =  new Crypting();
			log.debug("\n"+"utente.getLogin().setPassword("+crypting.encrypt(password)+")");
			utente.getLogin().setPassword(crypting.encrypt(password));

			utente.getLogin().setArchive(archive);
			if (ipStazione != null)
				utente.getLogin().setIpClient(ipStazione);
			log.debug("\n"+"UtenteXsd Stop");
		}
		catch (CryptingException e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
	}

	/**
	 * Questo metodo viene utilizzato per leggere il file Xml
	 * 
	 * @param fileXml File Xml
	 * @throws JAXBException 
	public void read(File fileXml) throws JAXBException
	{
		JAXBContext jc = null;
		Unmarshaller u = null;

		try
		{
			log.debug("read(File "+fileXml+")");
			this.fileXml = fileXml.getAbsolutePath();
			log.debug("fileXml: "+this.fileXml);
			log.debug("fileXml.exists: "+fileXml.exists());
			if (fileXml.exists())
			{
				log.debug("Utente.package: "+Utente.class.getPackage().getName());
				jc = JAXBContext.newInstance(Utente.class.getPackage().getName());

				log.debug("jc.createUnmarshaller()");
				u = jc.createUnmarshaller();

				log.debug("u.unmarshal("+fileXml+")");
				utente = (Utente) u.unmarshal(fileXml);
			}
		}
		catch (JAXBException e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		
	}
	 */

	/**
	 * Questo metodo viene utilizzato per leggere il tracciato Xml
	 * 
	 * @param fileXml Tracciato xml
	 * @throws JAXBException
	public void read(InputStream fileXml) throws JAXBException
	{
		JAXBContext jc = null;
		Unmarshaller u = null;

		try
		{
			log.debug("read(InputStream "+fileXml+");");
			if (fileXml != null)
			{
				log.debug("Utente.package: "+Utente.class.getPackage().getName());
				jc = JAXBContext.newInstance(Utente.class.getPackage().getName());

				log.debug("jc.createUnmarshaller()");
				u = jc.createUnmarshaller();

				log.debug("u.unmarshal("+fileXml+")");
				utente = (Utente) u.unmarshal(fileXml);
			}
		}
		catch (UnmarshalException e){
			log.error("XML: "+getStringFromInputStream(fileXml));
			throw e;
		}
		catch (JAXBException e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		
	}
	 */

	/**
	 * Conversione oggetto Stream in String
	 * 
	 * @param is Oggetto Stream
	 * @return Oggetto convertito
	private static String getStringFromInputStream(InputStream is) {
		 
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
 
		String line;
		try {
 
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
 
		return sb.toString();
 
	}
	 */
 
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

			log.debug("\n"+"Utente.package: "+Utente.class.getPackage().getName());
			jc = JAXBContext.newInstance(Utente.class.getPackage().getName());

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
			if (fOut.exists())
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
			m.marshal( utente, fos );
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
			log.debug("\n"+"Utente.package: "+Utente.class.getPackage().getName());
			jc = JAXBContext.newInstance(Utente.class.getPackage().getName());

			log.debug("\n"+"jc.createMarshaller();");
			m = jc.createMarshaller();

			log.debug("\n"+"m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);");
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);

			log.debug("\n"+"m.marshal( utente, output );");
			m.marshal( utente, output );
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

			log.debug("\n"+"Utente.package: "+Utente.class.getPackage().getName());
			jc = JAXBContext.newInstance(Utente.class.getPackage().getName());

			log.debug("\n"+"jc.createMarshaller();");
			m = jc.createMarshaller();

			log.debug("\n"+"m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);");
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);

			baos =  new ByteArrayOutputStream();
			log.debug("\n"+"m.marshal( utente, baos );");
			m.marshal( utente, baos );
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
		DecimalFormat df3 = new DecimalFormat("0=0");

		pathXml = fOut.getParentFile().getAbsolutePath();
		pathXml += File.separator;
		pathXml += fOut.getName().replace(".xml", ".bck");
		pathXml += File.separator;
		pathXml += gc.get(Calendar.YEAR);
		pathXml += File.separator;
		pathXml += df2.format((gc.get(Calendar.MONTH)+1));
		pathXml += File.separator;
		pathXml += df2.format((gc.get(Calendar.DAY_OF_MONTH)));
		pathXml += File.separator;
		pathXml += fOut.getName().replace(".xml", "_");
		pathXml += gc.get(Calendar.YEAR);
		pathXml += df2.format((gc.get(Calendar.MONTH)+1));
		pathXml += df2.format((gc.get(Calendar.DAY_OF_MONTH)));
		pathXml += df2.format((gc.get(Calendar.HOUR_OF_DAY)));
		pathXml += df2.format((gc.get(Calendar.MINUTE)));
		pathXml += df2.format((gc.get(Calendar.SECOND)));
		pathXml += df3.format((gc.get(Calendar.MILLISECOND)));
		pathXml += ".xml";
		return new File(pathXml);
	}

	/**
	 * Questo metodo viene utilizzato per leggere l'ogetto Utente 
	 * 
	 * @return
	 */
	public Utente getUtente()
	{
		log.debug("\n"+"getUtente()");
		if (utente == null)
			utente = new Utente();
		return utente;
	}

	/**
	 * Questo metodo viene utilizzato per leggere l'oggetto Login
	 * @return
	 */
	public Login getLogin()
	{
		log.debug("\n"+"getLogin()");
		if (this.getUtente()== null)
			return null;
		else if (this.getUtente().getLogin() == null)
			return null;
		else
			return this.getUtente().getLogin();
	}

	public List<Utente.MsgError> getMsgError()
	{
		log.debug("\n"+"getMsgError()");
		if (this.getUtente()==null)
			return null;
		else if (this.getUtente().getMsgError() == null)
			return null;
		else
			return this.getUtente().getMsgError();
	}

	/**
	 * Questo metodo viene utilizzato per aggiungere un messaggio di errore nella risposta
	 * 
	 * @param msgError Messaggio di Errore
	 */
	public void addMsgError(String msgError)
	{
		log.debug("\n"+"addMsgError("+msgError+")");
		Utente.MsgError msgErr = null;

		if (this.getUtente()!= null)
		{
			msgErr = new Utente.MsgError();
			msgErr.setValue(msgError);
			this.getUtente().getMsgError().add(msgErr);
		}
	}
}
