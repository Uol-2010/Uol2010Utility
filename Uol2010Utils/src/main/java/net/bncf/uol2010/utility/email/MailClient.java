/**
 * 
 */
package net.bncf.uol2010.utility.email;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeBodyPart;

import org.apache.log4j.Logger;

/**
 * Questa classe viene utilizzato per eseguire l'invio della Email
 * 
 * @author Massimiliano Randazzo
 * 
 */
public class MailClient
{

	/**
	 * Questa variabile vinee utilizzata per loggare l'applicazione
	 */
	private static Logger log = Logger.getLogger(MailClient.class);

	/**
	 * Questa variabile viene utilizzata per indicare il nome del server Smtp
	 */
	private String smtpServer = null;

	/**
	 * Questa variabile viene utilizzata per indicare la porta del server Smtp
	 */
	private String smtpPort = null;

	/**
	 * Questa variabile viene utilizzata per indicare il login per il server smtp
	 */
	private String smtpLogin = null;

	/**
	 * Questa variabile viene utilizzata per indicare la password per il server smtp
	 */
	private String smtpPassword = null;

	/**
	 * Questa variabile viene utilizzata per indicare la lista dei file da inviare nella email
	 */
	private List<String> attachments = null;

	/**
	 * Questa variabile viene utilizzata per indicare il mittente della email
	 */
	private String from = null;
	
	/**
	 * Questa variabile viene utilizzata per indicare la lista dei destinatari della email
	 */
	private List<String> to = null;

	/**
	 * Questa variabile viene utilizzata per indicare la lista dei conoscenti della email
	 */
	private List<String> cc = null;

	/**
	 * Questa variabile viene utilizzata per indicare la lista dei conoscenti nascosti della email
	 */
	private List<String> ccn = null;

	/**
	 * Costruttore
	 * 
	 * @param smtpServer Server Smtp 
	 */
	public MailClient(String smtpServer)
	{
		log.debug("Costruttore  smtpServer: "+smtpServer);
		this.smtpServer = smtpServer;
	}

	/**
	 * Costruttore
	 * 
	 * @param smtpServer Server Smtp 
	 */
	public MailClient(String smtpServer, String smtpPort)
	{
		log.debug("Costruttore  smtpServer: "+smtpServer);
		log.debug("Costruttore  smtpPort: "+smtpPort);
		this.smtpServer = smtpServer;
		this.smtpPort = smtpPort;
	}

	/**
	 * Questo metodo viene utilizzato per indicare le informazioni per il login verso il server Smtp
	 * 
	 * @param smtpLogin Login server Smtp
	 * @param smtpPassword Password server Smtp
	 */
	public void setSmtpAuthentication(String smtpLogin, String smtpPassword)
	{
		log.debug("setSmtpAuthentication('"+smtpLogin+"', '"+smtpPassword+"')");
		this.smtpLogin = smtpLogin;
		this.smtpPassword = smtpPassword;
	}

	/**
	 * Questo metodo viene utilizzato per aggiungere la lista dei file da attaccare per l'invio nel messaggio
	 * 
	 * @param attachment Nom del file da attaccare
	 */
	public void addAttachments(String attachment)
	{
		log.debug("addAttachments('"+attachment+"')");
		if (attachments == null)
			attachments = new ArrayList<String>();
		attachments.add(attachment);
	}

	/**
	 * Questo metodo viene utilizzato per aggiungere la lista dei mittenti della messaggio
	 * 
	 * @param attachment Nom del file da attaccare
	 */
	public void addTo(String to)
	{
		String[] tos = null;
		log.debug("addTo('"+to+"')");
		if (this.to == null)
			this.to = new ArrayList<String>();
		
		tos = to.split(" ");
		for(int x=0; x<tos.length; x++){
			if (tos[x].indexOf("@")>-1){
				this.to.add(tos[x]);
			}
		}
	}

	/**
	 * Questo metodo viene utilizzata per indicare il mittente della email
	 * @param from
	 */
	public void setFrom(String from)
	{
		log.debug("setFrom('"+from+"')");
		this.from=from;
	}

	/**
	 * Questo metodo viene utilizzato per aggiungere la lista dei conoscenti della messaggio
	 * 
	 * @param attachment Nom del file da attaccare
	 */
	public void addCc(String cc)
	{
		log.debug("addCc('"+cc+"')");
		if (this.cc == null)
			this.cc = new ArrayList<String>();
		this.cc.add(cc);
	}

	/**
	 * Questo metodo viene utilizzato per aggiungere la lista dei conoscenti nascosti della messaggio
	 * 
	 * @param attachment Nom del file da attaccare
	 */
	public void addCcn(String ccn)
	{
		log.debug("addCcn('"+ccn+"')");
		if (this.ccn == null)
			this.ccn = new ArrayList<String>();
		this.ccn.add(ccn);
	}

	/**
	 * Questo metodo viene utilizzato per inviare il messaggio
	 * 
	 * @param subject Soggetto del messaggio
	 * @param messageBody Testo del messaggio
	 * @throws MessagingException
	 * @throws AddressException
	 */
	public void sendMail(String subject, String messageBody) throws MessagingException, AddressException
	{
		if (smtpLogin != null &&
				smtpPassword != null)
			SendSSLMail.sendMail(smtpServer, smtpPort, smtpLogin, smtpPassword, from, to, cc, ccn, attachments, subject, messageBody);
		else
			SendMail.sendMail(smtpServer, from, to, cc, ccn, attachments, subject, messageBody);
	}

	/**
	 * Questo metodo viene utilizzato per collegare i files al mesaggio
	 * 
	 * @param multipart Messaggio a cui collegare i files
	 * @throws MessagingException 
	 * @throws MessagingException
	 * @throws AddressException
	 */
	protected static void addAttachments(Multipart multipart, List<String> attachments) throws MessagingException
	{
		String filename = null;
		MimeBodyPart attachmentBodyPart = null;
		DataSource source = null;
		File f = null;
		
		try
		{
			log.debug("Numero file da colelgare: "+attachments.size());
			for (int i = 0; i < attachments.size(); i++)
			{
				filename = attachments.get(i);
				f = new File (filename);
				if (f.exists())
				{
					attachmentBodyPart = new MimeBodyPart();

					// use a JAF FileDataSource as it does MIME type detection
					source = new FileDataSource(filename);
					attachmentBodyPart.setDataHandler(new DataHandler(source));

					// assume that the filename you want to send is the same as the
					// actual file name - could alter this to remove the file path
					attachmentBodyPart.setFileName(f.getName());

					// add the attachment
					multipart.addBodyPart(attachmentBodyPart);
				}
			}
		}
		catch (MessagingException e)
		{
			throw e;
		}
	}

	/**
	 * Questo metodo viene invocato quando l'applicazione viene utilizzata come applicazione
	 * 
	 * @param args lista degli argomenti dell'applicazione
	 */
	public static void main(String[] args)
	{
		String smtpServer = null;
		String smtpPort = null;
		String smtpLogin = null;
		String smtpPassword = null;
		String from = null;
		List<String> to = null;
		List<String> cc = null;
		List<String> ccn = null;
		String subject = null;
		String body = null;
		List<String> attachment = null;
		MailClient client = null;

		try
		{

			for (int x=0; x<args.length; x++)
			{
				log.debug(x+" -> "+args[x]);
				log.debug((x+1)+" -> "+args[x+1]);
				if (args[x].endsWith("-s"))
				{
					x++;
					smtpServer = args[x];
				}
				else if (args[x].endsWith("-sport"))
				{
					x++;
					smtpPort = args[x];
				}
				else if (args[x].endsWith("-sl"))
				{
					x++;
					smtpLogin = args[x];
				}
				else if (args[x].endsWith("-sp"))
				{
					x++;
					smtpPassword = args[x];
				}
				else if (args[x].endsWith("-f"))
				{
					x++;
					from = args[x];
				}
				else if (args[x].endsWith("-to"))
				{
					x++;
					if (to== null)
						to = new ArrayList<String>();
					to.add(args[x]);
				}
				else if (args[x].endsWith("-cc"))
				{
					x++;
					if (cc== null)
						cc = new ArrayList<String>();
					cc.add(args[x]);
				}
				else if (args[x].endsWith("-ccn"))
				{
					x++;
					if (ccn== null)
						ccn = new ArrayList<String>();
					ccn.add(args[x]);
				}
				else if (args[x].endsWith("-sub"))
				{
					x++;
					subject = args[x];
				}
				else if (args[x].endsWith("-b"))
				{
					x++;
					body = args[x];
				}
				else if (args[x].endsWith("-a"))
				{
					x++;
					if (attachment == null)
						attachment = new ArrayList<String>();
					attachment.add(args[x]);
				}
			}

			if (smtpServer != null && from != null && body != null)
			{
				if (smtpPort != null)
					client = new MailClient(smtpServer, smtpPort);
				else
					client = new MailClient(smtpServer);

				if (smtpLogin != null &&
						smtpPassword != null)
					client.setSmtpAuthentication(smtpLogin, smtpPassword);

				client.setFrom(from);
				if (to != null)
				{
					for (int x=0; x<to.size(); x++)
						client.addTo(to.get(x));
				}

				if (cc != null)
				{
					for (int x=0; x<cc.size(); x++)
						client.addCc(cc.get(x));
				}

				if (ccn != null)
				{
					for (int x=0; x<ccn.size(); x++)
						client.addCc(ccn.get(x));
				}

				if (attachment != null)
				{
					for (int x=0; x<attachment.size(); x++)
						client.addAttachments(attachment.get(x));
				}

				client.sendMail(subject, body);
			}
			else
			{
				
			}
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}

	}
}
