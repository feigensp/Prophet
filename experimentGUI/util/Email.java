package experimentGUI.util;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author zeja modified
 */
public class Email {

	private static final String password = "2010ErstellterExperimentVerteiler.";
	private static final String username = "Verteiler.Experiment";
	private static final String userAddress = "Verteiler.Experiment@web.de";
	// GeheimFrage: Lieblingsfilm: Das Experiment
	private static final String smtpHost = "smtp.web.de";

	public void sendMail(String recipientsAddress, String subject, String text, String filePath) {
		MailAuthenticator auth = new MailAuthenticator(username, password);
		Properties properties = new Properties();
		properties.put("mail.smtp.host", smtpHost);
		properties.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(properties, auth);
		try {
			Message msg = new MimeMessage(session);

			MimeMultipart content = new MimeMultipart("alternative");
			MimeBodyPart message = new MimeBodyPart();
			message.setText(text);
			message.setHeader("MIME-Version", "1.0");
			message.setHeader("Content-Type", message.getContentType());
			content.addBodyPart(message);
			if (filePath != null) {
				DataSource fileDataSource = new FileDataSource(filePath);
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setDataHandler(new DataHandler(fileDataSource));
				messageBodyPart.setFileName(new File(filePath).getName());
				content.addBodyPart(messageBodyPart);
			}

			msg.setContent(content);
			msg.setSentDate(new Date());
			msg.setFrom(new InternetAddress(userAddress));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientsAddress, false));

			msg.setSubject(subject);

			Transport.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class MailAuthenticator extends Authenticator {
		private final String user;
		private final String password;

		/**
		 * Der Konstruktor erzeugt ein MailAuthenticator Objekt<br>
		 * aus den beiden Parametern user und passwort.
		 * 
		 * @param user
		 *            String, der Username fuer den Mailaccount.
		 * @param password
		 *            String, das Passwort fuer den Mailaccount.
		 */
		public MailAuthenticator(String user, String password) {
			this.user = user;
			this.password = password;
		}

		/**
		 * Diese Methode gibt ein neues PasswortAuthentication Objekt zurueck.
		 * 
		 * @see javax.mail.Authenticator#getPasswordAuthentication()
		 */
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(this.user, this.password);
		}
	}

	public static void main(String[] args) {
		String recipientsAddress = "hasselbe@gmail.com"; // somereceiver@web.de
		String subject = "Test";
		String text = "text";

		new Email().sendMail(recipientsAddress, subject, text, null);
	}
}