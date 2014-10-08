package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins;

import java.io.File;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.SpinnerNumberModel;

import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer.EViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.Plugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.mailPlugin.ZFile;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.PluginSettings;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsComboBox;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsPasswordField;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsSpinner;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsTextField;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames.getLocalized;
import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode.Type.EXPERIMENT;

/**
 * A <code>Plugin</code> that can send the folder that results from an experiment run (containing answers.xml and other
 * files) as an attachment to a predefined Email address.
 */
public class MailPlugin implements Plugin {

    private final static String KEY = "sendmail";
    private final static String SMTP_SERVER = "smtp_server";
    private final static String SMTP_USER = "smtp_user";
    private final static String SMTP_PASS = "smtp_pass";
    private final static String SMTP_SENDER = "smtp_sender";
    private final static String SMTP_RECEIVER = "smtp_receiver";
    private final static String SMTP_SERVER_PORT = "smtp_server_port";
    private final static String SMTP_SERVER_SEC = "smtp_server_sec";

    private final static String SEC_NONE = "-";
    private final static String SEC_STARTTLS = "STARTTLS";
    private final static String SEC_SSL_TLS = "SSL/TLS";

    private boolean enabled;
    private String smtpServer;
    private String smtpUser;
    private String smtpPass;
    private String smtpSender;
    private String smtpReceiver;
    private String smtpSec;
    private int smtpPort;

    private EViewer experimentViewer;

    @Override
    public Setting getSetting(QTreeNode node) {

        if (node.getType() != EXPERIMENT) {
            return null;
        }

        Attribute mainAttribute = node.getAttribute(KEY);
        PluginSettings pluginSettings = new PluginSettings(mainAttribute, getClass().getSimpleName(), true);
        pluginSettings.setCaption(getLocalized("MAIL_SEND_MAIL"));

        Attribute subAttribute = mainAttribute.getSubAttribute(SMTP_SENDER);
        Setting subSetting = new SettingsTextField(subAttribute, null);
        subSetting.setCaption(getLocalized("MAIL_SMTP_SENDER") + ":");
        pluginSettings.addSetting(subSetting);

        subAttribute = mainAttribute.getSubAttribute(SMTP_RECEIVER);
        subSetting = new SettingsTextField(subAttribute, null);
        subSetting.setCaption(getLocalized("MAIL_SMTP_RECIPIENT") + ":");
        pluginSettings.addSetting(subSetting);

        subAttribute = mainAttribute.getSubAttribute(SMTP_SERVER);
        subSetting = new SettingsTextField(subAttribute, null);
        subSetting.setCaption(getLocalized("MAIL_SMTP_SERVER") + ":");
        pluginSettings.addSetting(subSetting);

        subAttribute = mainAttribute.getSubAttribute(SMTP_SERVER_PORT);
        subSetting = new SettingsSpinner(subAttribute, null, new SpinnerNumberModel(587, 0, 65535, 1));
        subSetting.setCaption(getLocalized("MAIL_SMTP_SERVER_PORT") + ":");
        pluginSettings.addSetting(subSetting);

        subAttribute = mainAttribute.getSubAttribute(SMTP_SERVER_SEC);
        subSetting = new SettingsComboBox(subAttribute, null, SEC_NONE, SEC_STARTTLS, SEC_SSL_TLS);
        subSetting.setCaption(getLocalized("MAIL_SMTP_SECURITY") + ":");
        pluginSettings.addSetting(subSetting);

        subAttribute = mainAttribute.getSubAttribute(SMTP_USER);
        subSetting = new SettingsTextField(subAttribute, null);
        subSetting.setCaption(getLocalized("MAIL_SMTP_USER") + ":");
        pluginSettings.addSetting(subSetting);

        subAttribute = mainAttribute.getSubAttribute(SMTP_PASS);
        subSetting = new SettingsPasswordField(subAttribute, null);
        subSetting.setCaption(getLocalized("MAIL_SMTP_PASSWORD") + ":");
        pluginSettings.addSetting(subSetting);

        return pluginSettings;
    }

    @Override
    public void experimentViewerRun(EViewer experimentViewer) {
        this.experimentViewer = experimentViewer;
    }

    @Override
    public boolean denyEnterNode(QTreeNode node) {
        return false;
    }

    @Override
    public void enterNode(QTreeNode node) {

        if (node.getType() != EXPERIMENT) {
            return;
        }

        enabled = Boolean.parseBoolean(node.getAttribute(KEY).getValue());

        if (enabled) {
            Attribute attributes = node.getAttribute(KEY);

            smtpServer = attributes.getSubAttribute(SMTP_SERVER).getValue();
            smtpUser = attributes.getSubAttribute(SMTP_USER).getValue();
            smtpPass = SettingsPasswordField.decode(attributes.getSubAttribute(SMTP_PASS).getValue());
            smtpSender = attributes.getSubAttribute(SMTP_SENDER).getValue();
            smtpReceiver = attributes.getSubAttribute(SMTP_RECEIVER).getValue();
            smtpSec = attributes.getSubAttribute(SMTP_SERVER_SEC).getValue();
            smtpPort = Integer.parseInt(attributes.getSubAttribute(SMTP_SERVER_PORT).getValue());
        }
    }

    @Override
    public String denyNextNode(QTreeNode currentNode) {
        return null;
    }

    @Override
    public void exitNode(QTreeNode node) {

    }

    @Override
    public String finishExperiment() {

        if (enabled) {
            ZFile attachment = new ZFile(experimentViewer.getSaveDir().toURI());

            try {
                sendEMail(experimentViewer.getSaveDir().getName(), getMailText(), attachment.zip().orElse(null));
            } catch (MessagingException e) {
                System.err.println("Could not send the EMail containing the zipped experiment results.");
                System.err.println(e.getMessage());

                return getLocalized("MAIL_MESSAGE_COULD_NOT_SEND_MAIL");
            }
        }

        return null;
    }

    private String getMailText() {
        return "";
    }

    /**
     * Sends an EMail with the given <code>subject</code>, <code>text</code> and optionally an attachment file to
     * the address configured via the <code>Plugin</code> settings.
     *
     * @param subject
     *         the subject line for the EMail
     * @param text
     *         the text for the EMail
     * @param attachmentFile
     *         the attachment file or <code>null</code> for no attachment file
     *
     * @throws MessagingException
     *         if there is an error creating or sending the EMail
     */
    private void sendEMail(String subject, String text, File attachmentFile) throws MessagingException {
        MailAuthenticator auth = new MailAuthenticator(smtpUser, smtpPass);
        Properties properties = new Properties();

        properties.put("mail.smtp.host", smtpServer);
        properties.put("mail.smtp.port", String.valueOf(smtpPort));

        if (smtpSec.equals(SEC_STARTTLS) || smtpSec.equals(SEC_SSL_TLS)) {
            properties.put("mail.smtp.starttls.enable","true");
            properties.put("mail.smtp.auth", "true");  // If you need to authenticate
        }

        if (smtpSec.equals(SEC_SSL_TLS)) {
            properties.put("mail.smtp.socketFactory.port", String.valueOf(smtpPort));
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.socketFactory.fallback", "false");
        }

        Session session = Session.getDefaultInstance(properties, auth);
        Message message = new MimeMessage(session);

        MimeMultipart content = new MimeMultipart();
        MimeBodyPart textPart = new MimeBodyPart();

        textPart.setText(text);
        content.addBodyPart(textPart);

        if (attachmentFile != null) {
            DataSource fileDataSource = new FileDataSource(attachmentFile);
            BodyPart attachmentPart = new MimeBodyPart();

            attachmentPart.setDataHandler(new DataHandler(fileDataSource));
            attachmentPart.setFileName(attachmentFile.getName());
            content.addBodyPart(attachmentPart);
        }

        message.setContent(content);
        message.setSentDate(new Date());
        message.setFrom(new InternetAddress(smtpSender));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(smtpReceiver, false));
        message.setSubject(subject);

        Transport.send(message);
    }

    /**
     * An <code>Authenticator</code> that can be initialized with a username and password and will henceforth
     * return an instance of <code>PasswordAuthentication</code> containing that information.
     */
    private static class MailAuthenticator extends Authenticator {

        private final PasswordAuthentication auth;

        /**
         * Constructs a new <code>MailAuthenticator</code> that will provide a <code>PasswordAuthentication</code>
         * containing the given username and password upon request.
         *
         * @param userName the username for the authentication
         * @param password the password for the authentication
         */
        public MailAuthenticator(String userName, String password) {
            auth = new PasswordAuthentication(userName, password);
        }

        /**
         * Returns the <code>PasswordAuthentication</code> constructed from the username and password this instance
         * of <code>MailAuthenticator</code> was initialized with.
         *
         * @return the <code>PasswordAuthentication</code> containing the username and password
         */
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return auth;
        }
    }
}
