package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins;

import java.io.File;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer.ExperimentViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.Plugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.mailPlugin.ZipFile;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTree.QuestionTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.PluginSettings;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsPasswordField;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsTextField;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode.Type;

public class MailPlugin implements Plugin {

    private final static String KEY = "sendmail";
    private final static String SMTP_SERVER = "smtp_server";
    private final static String SMTP_USER = "smtp_user";
    private final static String SMTP_PASS = "smtp_pass";
    private final static String SMTP_SENDER = "smtp_sender";
    private final static String SMTP_RECEIVER = "smtp_receiver";

    private boolean enabled;
    private String smtpServer;
    private String smtpUser;
    private String smtpPass;
    private String smtpSender;
    private String smtpReceiver;

    ExperimentViewer experimentViewer;

    public boolean sendMail(String subject, String text, File attachmentFile) {
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
            message.setHeader("MIME-Version", "1.0" + "\n");
            message.setHeader("Content-Type", message.getContentType());
            content.addBodyPart(message);
            if (attachmentFile != null) {
                DataSource fileDataSource = new FileDataSource(attachmentFile);
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setDataHandler(new DataHandler(fileDataSource));
                messageBodyPart.setFileName(attachmentFile.getName());
                content.addBodyPart(messageBodyPart);
            }
            msg.setContent(content);
            msg.setSentDate(new Date());
            msg.setFrom(new InternetAddress(smtpSender));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(smtpReceiver, false));
            msg.setSubject(subject);
            Transport.send(msg);
            return true;
        } catch (Exception e) {
            //e.getMessage()
            return false;
        }
    }

    @Override
    public Setting getSetting(QTreeNode node) {

        if (node.getType() != Type.EXPERIMENT) {
            return null;
        }

        Attribute mainAttribute = node.getAttribute(KEY);
        PluginSettings pluginSettings = new PluginSettings(mainAttribute, getClass().getSimpleName(), true);
        pluginSettings.setCaption(UIElementNames.MAIL_SEND_MAIL);

        Attribute subAttribute = mainAttribute.getSubAttribute(SMTP_SERVER);
        Setting subSetting = new SettingsTextField(subAttribute, null);
        subSetting.setCaption(UIElementNames.MAIL_SMTP_SERVER + ":");
        pluginSettings.addSetting(subSetting);

        subAttribute = mainAttribute.getSubAttribute(SMTP_USER);
        subSetting = new SettingsTextField(subAttribute, null);
        subSetting.setCaption(UIElementNames.MAIL_SMTP_USER + ":");
        pluginSettings.addSetting(subSetting);

        subAttribute = mainAttribute.getSubAttribute(SMTP_PASS);
        subSetting = new SettingsPasswordField(subAttribute, null);
        subSetting.setCaption(UIElementNames.MAIL_SMTP_PASSWORD + ":");
        pluginSettings.addSetting(subSetting);

        subAttribute = mainAttribute.getSubAttribute(SMTP_SENDER);
        subSetting = new SettingsTextField(subAttribute, null);
        subSetting.setCaption(UIElementNames.MAIL_SMTP_SENDER + ":");
        pluginSettings.addSetting(subSetting);

        subAttribute = mainAttribute.getSubAttribute(SMTP_RECEIVER);
        subSetting = new SettingsTextField(subAttribute, null);
        subSetting.setCaption(UIElementNames.MAIL_SMTP_RECIPIENT + ":");
        pluginSettings.addSetting(subSetting);

        return pluginSettings;
    }

    @Override
    public void experimentViewerRun(ExperimentViewer experimentViewer) {
        this.experimentViewer = experimentViewer;
    }

    @Override
    public boolean denyEnterNode(QTreeNode node) {
        return false;
    }

    @Override
    public void enterNode(QTreeNode node) {
        try {
            if (node.isExperiment()) {
                enabled = Boolean.parseBoolean(node.getAttributeValue(KEY));
                if (enabled) {
                    QuestionTreeNode attributes = node.getAttribute(KEY);
                    smtpServer = attributes.getAttributeValue(SMTP_SERVER);
                    smtpUser = attributes.getAttributeValue(SMTP_USER);
                    smtpPass = SettingsPasswordField.decode(attributes.getAttributeValue(SMTP_PASS));
                    smtpSender = attributes.getAttributeValue(SMTP_SENDER);
                    smtpReceiver = attributes.getAttributeValue(SMTP_RECEIVER);
                }
            }
        } catch (Exception e) {

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
        try {
            if (enabled) {
                File attachmentFile = new File(experimentViewer.getSaveDir().getName() + ".zip");
                ZipFile.zipFiles(experimentViewer.getSaveDir(), attachmentFile);
                if (!this.sendMail(experimentViewer.getSaveDir().getName(), "", attachmentFile)) {
                    return UIElementNames.MAIL_MESSAGE_COULD_NOT_SEND_MAIL;
                }
            }
        } catch (Exception e) {
            return UIElementNames.MAIL_MESSAGE_COULD_NOT_SEND_MAIL;
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
         *         String, der Username fuer den Mailaccount.
         * @param password
         *         String, das Passwort fuer den Mailaccount.
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
