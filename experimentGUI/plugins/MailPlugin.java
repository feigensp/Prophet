package experimentGUI.plugins;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
import experimentGUI.util.ZipFile;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class MailPlugin implements PluginInterface {
	public final static String KEY = "sendmail";
	public final static String SMTP_SERVER = "smtp_server";
	public final static String SMTP_USER = "smtp_user";
	public final static String SMTP_PASS = "smtp_pass";
	public final static String SMTP_SENDER = "smtp_sender";
	public final static String SMTP_RECEIVER = "smtp_receiver";

	private boolean enabled;
	private String smtpServer;
	private String smtpUser;
	private String smtpPass;
	private String smtpSender;
	private String smtpReceiver;

	ExperimentViewer experimentViewer;

	public boolean sendMail(String subject, String text, File attachmentFile) {
		try {
			File ausgabeDatei = new File("logSendMail.txt");
			FileWriter fw = new FileWriter(ausgabeDatei);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("Beginn sendMail\n");
			bw.write("Parameter: subject: " + subject + " - text: " + text + " - attachementFile: "
					+ attachmentFile.getAbsolutePath() + "\n");
			bw.flush();
			MailAuthenticator auth = new MailAuthenticator(smtpUser, smtpPass);
			bw.write("MailAuthenticator auth = new MailAuthenticator(smtpUser, smtpPass);" + "\n");
			bw.flush();
			Properties properties = new Properties();
			bw.write("Properties properties = new Properties();" + "\n");
			bw.flush();
			properties.put("mail.smtp.host", smtpServer);
			bw.write("properties.put(\"mail.smtp.host\", smtpServer);" + "\n");
			bw.flush();
			properties.put("mail.smtp.auth", "true");
			bw.write("properties.put(\"mail.smtp.auth\", \"true\");" + "\n");
			bw.flush();

			Session session = Session.getDefaultInstance(properties, auth);
			bw.write("Session session = Session.getDefaultInstance(properties, auth);" + "\n");
			bw.flush();
			Message msg = new MimeMessage(session);
			bw.write("Message msg = new MimeMessage(session);" + "\n");
			bw.flush();

			MimeMultipart content = new MimeMultipart("alternative");
			bw.write("MimeMultipart content = new MimeMultipart(\"alternative\");" + "\n");
			bw.flush();
			MimeBodyPart message = new MimeBodyPart();
			bw.write("MimeBodyPart message = new MimeBodyPart();" + "\n");
			bw.flush();
			message.setText(text);
			bw.write("message.setText(text);");
			bw.flush();
			message.setHeader("MIME-Version", "1.0" + "\n");
			bw.write("message.setHeader(\"MIME-Version\", \"1.0\");" + "\n");
			bw.flush();
			message.setHeader("Content-Type", message.getContentType());
			bw.write("message.setHeader(\"Content-Type\", message.getContentType());" + "\n");
			bw.flush();
			content.addBodyPart(message);
			bw.write("content.addBodyPart(message);" + "\n");
			bw.flush();
			if (attachmentFile != null) {
				bw.write("if (attachmentFile != null) {" + "\n");
				bw.flush();
				DataSource fileDataSource = new FileDataSource(attachmentFile);
				bw.write("DataSource fileDataSource = new FileDataSource(attachmentFile);" + "\n");
				bw.flush();
				BodyPart messageBodyPart = new MimeBodyPart();
				bw.write("BodyPart messageBodyPart = new MimeBodyPart();" + "\n");
				bw.flush();
				messageBodyPart.setDataHandler(new DataHandler(fileDataSource));
				bw.write("messageBodyPart.setDataHandler(new DataHandler(fileDataSource));" + "\n");
				bw.flush();
				messageBodyPart.setFileName(attachmentFile.getName());
				bw.write("messageBodyPart.setFileName(attachmentFile.getName());" + "\n");
				bw.flush();
				content.addBodyPart(messageBodyPart);
				bw.write("content.addBodyPart(messageBodyPart);" + "\n");
				bw.flush();
			}
			msg.setContent(content);
			bw.write("msg.setContent(content);" + "\n");
			bw.flush();
			msg.setSentDate(new Date());
			bw.write(new Date().toString());
			bw.write("msg.setSentDate(new Date());" + "\n");
			bw.flush();
			msg.setFrom(new InternetAddress(smtpSender));
			bw.write("msg.setFrom(new InternetAddress(smtpSender));" + "\n");
			bw.flush();
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(smtpReceiver, false));
			bw.write("msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(smtpReceiver, false));" + "\n");
			bw.flush();
			msg.setSubject(subject);
			bw.write("msg.setSubject(subject);" + "\n");
			bw.flush();
			Transport.send(msg);
			bw.write("Transport.send(msg);" + "\n");
			bw.flush();
			bw.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public SettingsComponentDescription getSettingsComponentDescription(QuestionTreeNode node) {
		if (node.isExperiment()) {
			SettingsPluginComponentDescription result = new SettingsPluginComponentDescription(KEY,
					"E-Mail versenden");
			result.addSubComponent(new SettingsComponentDescription(SettingsTextField.class, SMTP_SERVER,
					"SMTP-Server:"));
			result.addSubComponent(new SettingsComponentDescription(SettingsTextField.class, SMTP_USER,
					"SMTP-Benutzer:"));
			result.addSubComponent(new SettingsComponentDescription(SettingsPasswordField.class, SMTP_PASS,
					"SMTP-Passwort:"));
			result.addSubComponent(new SettingsComponentDescription(SettingsTextField.class, SMTP_SENDER,
					"Absender:"));
			result.addSubComponent(new SettingsComponentDescription(SettingsTextField.class, SMTP_RECEIVER,
					"Empfänger:"));
			return result;
		}
		return null;
	}

	@Override
	public void experimentViewerRun(ExperimentViewer experimentViewer) {
		this.experimentViewer = experimentViewer;
	}

	@Override
	public Object enterNode(QuestionTreeNode node) {
		try {
			File ausgabeDatei = new File("logEnterNode_" + node.getName() + ".txt");
			FileWriter fw = new FileWriter(ausgabeDatei);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("Beginn: enterNode" + "\n");
			bw.flush();
			if (node.isExperiment()) {
				bw.write("if (node.isExperiment()) {" + "\n");
				bw.flush();
				enabled = Boolean.parseBoolean(node.getAttributeValue(KEY));
				bw.write("enabled = Boolean.parseBoolean(node.getAttributeValue(KEY));" + " --- enabled: "
						+ enabled + "\n");
				bw.flush();
				if (enabled) {
					bw.write("if (enabled) {" + "\n");
					bw.flush();
					QuestionTreeNode attributes = node.getAttribute(KEY);
					bw.write("QuestionTreeNode attributes = node.getAttribute(KEY);" + "\n");
					bw.flush();
					smtpServer = attributes.getAttributeValue(SMTP_SERVER);
					bw.write("smtpServer=attributes.getAttributeValue(SMTP_SERVER);" + " --- " + smtpServer + "\n");
					bw.flush();
					smtpUser = attributes.getAttributeValue(SMTP_USER);
					bw.write("smtpUser=attributes.getAttributeValue(SMTP_USER);" + " --- " + smtpUser + "\n");
					bw.flush();
					smtpPass = SettingsPasswordField.decode(attributes.getAttributeValue(SMTP_PASS));
					bw.write("smtpPass=SettingsPasswordField.decode(attributes.getAttributeValue(SMTP_PASS));"
							+ " --- " + smtpPass + "\n");
					bw.flush();
					smtpSender = attributes.getAttributeValue(SMTP_SENDER);
					bw.write("smtpSender=attributes.getAttributeValue(SMTP_SENDER);" + " --- " + smtpSender + "\n");
					bw.flush();
					smtpReceiver = attributes.getAttributeValue(SMTP_RECEIVER);
					bw.write("smtpReceiver=attributes.getAttributeValue(SMTP_RECEIVER);" + " --- "
							+ smtpReceiver + "\n");
					bw.flush();
					bw.close();
				}
			}
		} catch (Exception e) {

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
		try {

			File ausgabeDatei = new File("logfinishExperiment.txt");
			FileWriter fw = new FileWriter(ausgabeDatei);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("Beginn: finishExperiment" + "\n");
			bw.flush();
			if (enabled) {
				bw.write("if (enabled) {");
				bw.flush();
				File attachmentFile = new File(experimentViewer.getSaveDir().getName() + ".zip");
				bw.write("File attachmentFile = new File(experimentViewer.getSaveDir().getName() + \".zip\");" + "\n");
				bw.flush();
				ZipFile.zipFiles(experimentViewer.getSaveDir(), attachmentFile);
				bw.write("ZipFile.zipFiles(experimentViewer.getSaveDir(), attachmentFile);" + "\n");
				bw.flush();
				if (!this.sendMail(experimentViewer.getSaveDir().getName(), "", attachmentFile)) {
					bw.write("if (!this.sendMail(experimentViewer.getSaveDir().getName(), \"\", attachmentFile)) {" + " --- " + experimentViewer.getSaveDir().getName() + " - " + attachmentFile.getAbsolutePath() + "\n");
					bw.flush();
					bw.close();
					return "E-Mail-Versand gescheitert. Bitte beim Versuchsleiter melden.";
				}
			}
		} catch (Exception e) {

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