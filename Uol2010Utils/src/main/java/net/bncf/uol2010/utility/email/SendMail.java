/**
 * 
 */
package net.bncf.uol2010.utility.email;

import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

/**
 * Questa classe viene utilizzata per indicare la email in formato SSL
 * 
 * @author Massimiliano Randazzo
 *
 */
class SendMail
{

	/**
	 * Questa variabile vinee utilizzata per loggare l'applicazione
	 */
	private static Logger log = Logger.getLogger(SendMail.class);

	/**
	 * Questo metodo viene utilizzato per inviare il messaggio
	 * 
	 * @param subject Soggetto del messaggio
	 * @param messageBody Testo del messaggio
	 * @throws MessagingException
	 * @throws AddressException
	 */
	public static void sendMail(String smtpServer, String from,
			List<String> to, List<String> cc, List<String> ccn, List<String> attachments, String subject, String messageBody) throws MessagingException
	{
		Properties props = new Properties();
		Address[] aTo = null;
		Address[] aCc = null;
		Address[] aCcn = null;
//		SmtpAuthenticion auth = null;
		Session session = null;
		Message msg = null;
//		String smtpPort= "465";

		try
		{
			log.debug("Definizione Proprieta connessione");
//			props.put("mail.smtp.user", smtpLogin);
			props.put("mail.smtp.host", smtpServer);
//			props.put("mail.smtp.port", smtpPort);
//			props.put("mail.smtp.starttls.enable","true");
			props.put("mail.smtp.debug", "false");
//			props.put("mail.smtp.auth", "true");
//			props.put("mail.smtp.socketFactory.port", smtpPort);
//			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//			props.put("mail.smtp.socketFactory.fallback", "false");

//			log.debug("Definizione moduli di autenticazione");
//			auth = new SmtpAuthenticion(smtpLogin, smtpPassword);

			log.debug("Definizione Sessione");
			session = Session.getInstance(props);
			session.setDebug(true);

			log.debug("Inizializzazione Messaggio");
			msg = new MimeMessage(session);

			log.debug("Sogetto: "+subject);
			msg.setSubject(subject);

			log.debug("Mittente: "+from);
			msg.setFrom(new InternetAddress(from));

			if (to != null)
			{
				log.debug("Definizioni del destinatari");
				aTo = new Address[to.size()];
				for (int x=0; x<to.size(); x++)
				{
					log.debug("Destinatari: "+to.get(x));
					aTo[x]=new InternetAddress(to.get(x));
				}
				msg.addRecipients(Message.RecipientType.TO, aTo);
			}

			if (cc != null)
			{
				log.debug("Definizione del conoscente");
				aCc = new Address[to.size()];
				for (int x=0; x<cc.size(); x++)
				{
					log.debug("Conoscente: "+cc.get(x));
					aCc[x]=new InternetAddress(cc.get(x));
				}
				msg.addRecipients(Message.RecipientType.CC, aCc);
			}

			if (ccn != null)
			{
				log.debug("Definizione del conoscente nascosto");
				aCcn = new Address[to.size()];
				for (int x=0; x<ccn.size(); x++)
				{
					log.debug("Conoscente nascosto: "+ccn.get(x));
					aCcn[x]=new InternetAddress(ccn.get(x));
				}
				msg.addRecipients(Message.RecipientType.BCC, aCcn);
			}


			// Create a message part to represent the body text
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			log.debug("Definizione del testo: "+messageBody);
			messageBodyPart.setContent(messageBody, "text/plain");

			// use a MimeMultipart as we need to handle the file attachments
			MimeMultipart multipart = new MimeMultipart();

			// add the message body to the mime message
			multipart.addBodyPart(messageBodyPart);

			if (attachments != null)
			{
				log.debug("Collegamento dei files al messaggio");
				// add any file attachments to the message
				MailClient.addAttachments(multipart, attachments);
			}

			// Put all message parts in the message
			msg.setContent(multipart);

			log.debug("Invio del Messaggio");
			Transport.send(msg);
			log.debug("Messaggio Inviato");
		}
		catch (AddressException e)
		{
			throw e;
		}
		catch (MessagingException e)
		{
			throw e;
		}

	}

}
