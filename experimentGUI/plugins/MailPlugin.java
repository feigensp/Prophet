package experimentGUI.plugins;

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

import experimentGUI.PluginInterface;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsPluginComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsPasswordField;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsTextField;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;


public class MailPlugin implements PluginInterface {
	public final static String KEY = "sendmail";
	public final static String SMTP_SERVER = "smtp_server";
	public final static String SMTP_USER = "smtp_user";
	public final static String SMTP_PASS = "smtp_pass";
	public final static String SMTP_EMAIL = "smtp_email";
	
	private boolean enabled;
	private String smtpServer;
	private String smtpUser;
	private String smtpPass;
	private String smtpEmail;
	private String experimentCode;
	private String subjectCode;
	
	public boolean sendMail(String recipientsAddress, String subject, String text, String filePath) {
		try {
			MailAuthenticator auth = new MailAuthenticator(smtpUser, smtpPass);
			Properties properties = new Properties();
			properties.put("mail.smtp.host", smtpServer);
			properties.put("mail.smtp.auth", "true");

			Session session = Session.getDefaultInstance(properties, auth);
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
			msg.setFrom(new InternetAddress(smtpEmail));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientsAddress, false));
			msg.setSubject(subject);
			Transport.send(msg);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public SettingsComponentDescription getSettingsComponentDescription(
			QuestionTreeNode node) {
		if (node.isExperiment()) {
			SettingsPluginComponentDescription result = new SettingsPluginComponentDescription(KEY, "E-Mail versenden");
			result.addSubComponent(new SettingsComponentDescription(SettingsTextField.class,SMTP_SERVER, "SMTP-Server:"));
			result.addSubComponent(new SettingsComponentDescription(SettingsTextField.class,SMTP_USER, "SMTP-Benutzer:"));
			result.addSubComponent(new SettingsComponentDescription(SettingsPasswordField.class,SMTP_PASS, "SMTP-Passwort:"));
			result.addSubComponent(new SettingsComponentDescription(SettingsTextField.class,SMTP_EMAIL, "Empfänger:"));		
			return result;
		}
		return null;
	}

	@Override
	public void experimentViewerRun(ExperimentViewer experimentViewer) {
	}

	@Override
	public Object enterNode(QuestionTreeNode node) {
		if (node.isExperiment()) {
			enabled = Boolean.parseBoolean(node.getAttributeValue(KEY));
			if (enabled) {
				QuestionTreeNode attributes = node.getAttribute(KEY);
				smtpServer=attributes.getAttributeValue(SMTP_SERVER);
				smtpUser=attributes.getAttributeValue(SMTP_USER);
				smtpPass=SettingsPasswordField.decode(attributes.getAttributeValue(SMTP_PASS));
				smtpEmail=attributes.getAttributeValue(SMTP_EMAIL);
			}
		}
		return null;
	}

	@Override
	public void exitNode(QuestionTreeNode node, Object pluginData) {
	}

	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public String finishExperiment() {
		if (enabled) {
			
		}
		return null;
	}
	
	static class MailAuthenticator extends Authenticator {
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
}