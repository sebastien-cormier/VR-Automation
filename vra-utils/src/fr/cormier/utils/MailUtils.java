package fr.cormier.utils;

import javax.mail.internet.*;
import javax.mail.*;

import java.util.*;

public class MailUtils {

	private final static String MAILER_VERSION = "Java";

	
	private final static boolean DEBUG = false;
	


	public static boolean sendEmail(String to, String subject, String body) {
		boolean result = false;
		try {
			final String username = "postmaster@famille-cormier.fr";
			final String password = "4G7qucQ4";

			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "mailserver");
			props.put("mail.smtp.port", "587");

			Session session = Session.getInstance(props,
					  new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(username, password);
						}
					  });
			
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("postmaster@famille-cormier.fr"));
			InternetAddress[] internetAddresses = new InternetAddress[1];
			internetAddresses[0] = new InternetAddress(to);
			message.setRecipients(Message.RecipientType.TO, internetAddresses);
			message.setSubject(subject);
			message.setText(body);
			message.setHeader("X-Mailer", MAILER_VERSION);
			message.setSentDate(new Date());
			session.setDebug(DEBUG);
			Transport.send(message);
			result = true;
			
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return result;
	}

}
